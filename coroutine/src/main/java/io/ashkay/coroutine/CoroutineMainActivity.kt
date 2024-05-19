package io.ashkay.coroutine

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import io.ashkay.coroutine.databinding.ActivityCoroutineMainBinding
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
    }

    private fun looperHandlerQueue() {
        val th = thread {
            Looper.prepare()
            val handler1 = Handler()

            handler1.post {
                repeat(50) {
                    Thread.sleep(50)
                    println("${LooperHandlerQueue} [1] ${it}")
                }
            }
            handler1.post {
                repeat(50) {
                    Thread.sleep(50)
                    println("${LooperHandlerQueue} [2] ${it}")
                }
            }

            val handler2 = Handler()

            handler2.post {
                repeat(50) {
                    Thread.sleep(50)
                    println("${LooperHandlerQueue} [3] ${it}")
                }
            }
            handler2.post {
                repeat(50) {
                    Thread.sleep(50)
                    println("${LooperHandlerQueue} [4] ${it}")
                }
            }
            Looper.loop()
        }
    }

    fun checkingChildJobs() {
        val parentJob = lifecycleScope.launch {
            val childJob1 = launch {
                delay(1000)
                println("${ChildJobTag}: Hi i'm child job 1")
            }
            val childJob2 = launch {
                delay(2000)
                println("${ChildJobTag}: Hi i'm child job 2")
            }
        }

        println("${ChildJobTag}: Parent job's children" + parentJob.children.toList())
    }

    private fun checkJobCancel() {
        lifecycleScope.launch {
            val parentJob = launch {
                val child1 = launch {
                    repeat(100) {
                        delay(10)
                        println("${JobCancelTag}: Child 1 : ${it}")
                        if (it == 40) {
                            println("${JobCancelTag}: throwing exception in Child 1")
                            throw Exception("Error") // will stop all the sibling coroutine
                        }
                    }
                }
                val child2 = launch {
                    repeat(100) {
                        delay(10)
                        println("${JobCancelTag}: Child 2 : ${it}")
                        if (it == 20) {
                            println("${JobCancelTag}: cancelling in Child 2")
                            cancel() //other coroutines will keep running
                        }
                    }
                }
                val child3 = launch {
                    repeat(100) {
                        delay(10)
                        println("${JobCancelTag}: Child 3 : ${it}")
                    }
                }
            }
        }


    }


    companion object {
        const val ChildJobTag = "ChildJobTag"
        const val JobCancelTag = "JobCancelTag"
        const val LooperHandlerQueue = "LooperHandlerQueue"
    }
}