package io.ashkay.learnandroid.flow

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import io.ashkay.learnandroid.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.launch
import kotlin.random.Random

class FlowPlaygroundActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_flow_playground)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lifecycleScope.launch {
//            squareEvenNumbers()
//            bufferTest()
//            searchSimulate()
            handleUnstableFlow()
        }
    }

    private suspend fun squareEvenNumbers() {
        val range = 0..10

        range.asFlow().filter { it % 2 == 0 }.map { it * it }.collect {
            println(it)
        }
    }

    private fun slowFlow(): Flow<Int> = flow {
        for (i in 1..10) {
            delay(300)
            emit(i)
        }
    }

    private suspend fun noBufferTest() {
        var timestamp = System.currentTimeMillis()
        slowFlow()
            .collect {
                delay(600)
                val curr = System.currentTimeMillis()
                println("$it : ${curr - timestamp}") //~900ms delay
                timestamp = curr
            }
    }

    private suspend fun bufferTest() {
        var timestampBuffer = System.currentTimeMillis()
        slowFlow()
            .buffer()
            .collect {
                delay(600)
                val curr = System.currentTimeMillis()
                println("$it : ${curr - timestampBuffer}") //~900ms for 1st event rest 600ms delay
                timestampBuffer = curr
            }
    }

    private val queryFlow = flow {
        emit("k")
        delay(100)
        emit("ko")
        delay(100)
        emit("kot")
        delay(500)
        emit("kotl")
        delay(100)
        emit("kotli")
        delay(100)
        emit("kotlin")
    }

    private fun search(query: String): Flow<String> = flow {
        emit("Searching for $query...")
        delay(200)
        emit("|> Results for $query")
    }

    private suspend fun searchSimulate() {
        queryFlow.flatMapLatest { query ->
            search(query)
        }.collect {
            println(it)
        }
    }

    private fun unstableFlow(): Flow<Int> = flow {
        if (Random.nextBoolean()) throw RuntimeException("Fail")
        emit(42)
    }

    private suspend fun handleUnstableFlow() {
        unstableFlow()
            .retry(3) {
                println("Retrying...")
                delay(1000)
                true
            }
            .catch { e -> println("Error: $e") }
            .collect { value -> println("Value: $value") }
    }
}