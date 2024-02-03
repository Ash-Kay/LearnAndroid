package io.ashkay.learnandroid

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.ashkay.learnandroid.databinding.ActivityMainBinding
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
    }


}