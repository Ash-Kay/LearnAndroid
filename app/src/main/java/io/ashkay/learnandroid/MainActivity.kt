package io.ashkay.learnandroid

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.ashkay.coroutine.CoroutineMainActivity
import io.ashkay.di.DiMainActivity
import io.ashkay.learnandroid.databinding.ActivityMainBinding
import io.ashkay.learnandroid.lifecycle.LifecycleActivity
import io.ashkay.learnandroid.viewstubbing.ViewStubbingExample

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setup()
    }

    private fun setup() {
        binding.startViewStubbingExample.setOnClickListener {
            startActivity(Intent(this, ViewStubbingExample::class.java))
        }
        binding.startCoroutineExample.setOnClickListener {
            startActivity(Intent(this, CoroutineMainActivity::class.java))
        }
        binding.startDiExample.setOnClickListener {
            startActivity(Intent(this, DiMainActivity::class.java))
        }
        binding.startLifeCycleExample.setOnClickListener {
            startActivity(Intent(this, LifecycleActivity::class.java))
        }
    }


}