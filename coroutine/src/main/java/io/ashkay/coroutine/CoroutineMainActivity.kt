package io.ashkay.coroutine

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import io.ashkay.coroutine.databinding.ActivityCoroutineMainBinding
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.concurrent.thread

class CoroutineMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCoroutineMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoroutineMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startChildJob.setOnClickListener {
            checkingChildJobs()
        }
        binding.startJobCancel.setOnClickListener {
            checkJobCancel()
        }
        binding.startHandler.setOnClickListener {
            looperHandlerQueue()
        }
        binding.startDispatcherExample.setOnClickListener {
            checkDifferentDispatchersWork()
        }
    }

    private fun looperHandlerQueue() {
        val th = thread {
            Looper.prepare()
            val handler1 = Handler()

            handler1.post {
                repeat(50) {
                    Thread.sleep(50)
                    println("${CoroutineMainTag} [1] ${it}")
                }
            }
            handler1.post {
                repeat(50) {
                    Thread.sleep(50)
                    println("${CoroutineMainTag} [2] ${it}")
                }
            }

            val handler2 = Handler()

            handler2.post {
                repeat(50) {
                    Thread.sleep(50)
                    println("${CoroutineMainTag} [3] ${it}")
                }
            }
            handler2.post {
                repeat(50) {
                    Thread.sleep(50)
                    println("${CoroutineMainTag} [4] ${it}")
                }
            }
            Looper.loop()
        }
    }

    private fun checkingChildJobs() {
        val parentJob = lifecycleScope.launch {
            val childJob1 = launch {
                delay(1000)
                println("${CoroutineMainTag}: Hi i'm child job 1")
            }
            val childJob2 = launch {
                delay(2000)
                println("${CoroutineMainTag}: Hi i'm child job 2")
            }
            val childJob3 = launch(Job()) { //providing new Job so it doesn't inherit
                delay(2000)
                println("${CoroutineMainTag}: Hi i'm NOT a child job as I don't inherit job")
            }
        }

        println("${CoroutineMainTag}: Parent job's children count " + parentJob.children.toList().size)
        println("${CoroutineMainTag}: Parent job's children " + parentJob.children.toList())
    }

    private fun checkJobCancel() {
        val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            println("${CoroutineMainTag}: parent caught exception : $throwable")
        }

        lifecycleScope.launch(coroutineExceptionHandler) {
            val parentJob = launch {
                //NOTE: adding coroutineExceptionHandler here (in parentJob) will be of no use as
                // exception will still propagate to root launching parent

                val child1 = launch {
                    repeat(100) {
                        delay(10)
                        println("${CoroutineMainTag}: Child 1 : ${it}")
                        if (it == 40) {
                            println("${CoroutineMainTag}: throwing exception in Child 1")
                            throw Exception("Error") // will stop all the sibling coroutine
                        }
                    }
                }
                val child2 = launch {
                    repeat(100) {
                        delay(10)
                        println("${CoroutineMainTag}: Child 2 : ${it}")
                        if (it == 20) {
                            println("${CoroutineMainTag}: cancelling in Child 2")
                            cancel() //other coroutines will keep running
                        }
                    }
                }
                val child3 = launch {
                    repeat(100) {
                        delay(10)
                        println("${CoroutineMainTag}: Child 3 : ${it}")
                    }
                }
            }
        }
    }

    private fun checkDifferentDispatchersWork() {
        lifecycleScope.launch {
            println("${CoroutineMainTag}: Unspecified thread is ${Thread.currentThread().name}")

            withContext(Dispatchers.Default) {
                println("${CoroutineMainTag}: Dispatchers.Default thread is ${Thread.currentThread().name}")
            }


            withContext(Dispatchers.IO) {
                repeat(8) {
                    println("${CoroutineMainTag}: Dispatchers.IO thread working $it")
                    Thread.sleep(1000)
                }
                println("${CoroutineMainTag}: Dispatchers.IO thread is ${Thread.currentThread().name}")
            }


            withContext(Dispatchers.Main) {
                repeat(8) {
                    println("${CoroutineMainTag}: Dispatchers.Main thread working [Main Thread Blocked] $it")
                    Thread.sleep(1000)
                }
                println("${CoroutineMainTag}: Dispatchers.Main thread is ${Thread.currentThread().name}")
            }

            withContext(Dispatchers.Unconfined) {
                println("${CoroutineMainTag}: Dispatchers.Unconfined thread is ${Thread.currentThread().name}")
            }
        }
    }


    companion object {
        const val CoroutineMainTag = "CoroutineMainTag"
    }
}