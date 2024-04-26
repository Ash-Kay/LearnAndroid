package io.ashkay.coroutine

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import io.ashkay.coroutine.databinding.ActivityCoroutineMainBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

class CoroutineMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCoroutineMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoroutineMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        checkingChildJobs()
//        checkJobCancel()
        looperHandlerQueue()
    }

    private fun looperHandlerQueue() {
        thread {
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
                repeat(1_000) {
                    delay(10)
                    println("${JobCancelTag}: ${it}")
                }
            }

            delay(1000)
            parentJob.cancel()
        }


    }


    companion object {
        const val ChildJobTag = "ChildJobTag"
        const val JobCancelTag = "JobCancelTag"
        const val LooperHandlerQueue = "LooperHandlerQueue"
    }
}