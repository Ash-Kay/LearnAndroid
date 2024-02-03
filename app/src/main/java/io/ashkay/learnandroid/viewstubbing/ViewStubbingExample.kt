package io.ashkay.learnandroid.viewstubbing

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.ashkay.learnandroid.databinding.ActivityStubbingExampleBinding
import io.ashkay.learnandroid.databinding.LayoutStubBinding

class ViewStubbingExample : AppCompatActivity() {
    private lateinit var binding: ActivityStubbingExampleBinding
    private lateinit var stubBinding: LayoutStubBinding

    private var toggle = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStubbingExampleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.stub.setOnInflateListener { stub, inflated ->
            stubBinding = LayoutStubBinding.bind(inflated)
            stubBinding.text.text = "INFLATED STUB VIEW!!!"
        }

        binding.btnInflate.setOnClickListener {
            runCatching {
                binding.stub.inflate()
            }.onFailure {
                Toast.makeText(this, "Crash: ${it.message}", Toast.LENGTH_LONG).show()
                finish()
            }
        }

        binding.btnVisible.setOnClickListener {
            if (toggle) {
                binding.stub.visibility = View.VISIBLE
            } else {
                binding.stub.visibility = View.GONE
            }
            toggle = !toggle
        }
    }
}