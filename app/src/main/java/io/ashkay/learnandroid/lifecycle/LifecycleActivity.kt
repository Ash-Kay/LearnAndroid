package io.ashkay.learnandroid.lifecycle

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import io.ashkay.learnandroid.R
import io.ashkay.learnandroid.databinding.ActivityLifecycleBinding

class LifecycleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLifecycleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLifecycleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycle.addObserver(MyLifecycleObserver())

        supportFragmentManager.commit {
            add<FirstFragment>(R.id.fragmentContainer)
            addToBackStack(null)
        }

        binding.btnAdd.setOnClickListener {
            supportFragmentManager.commit {
                add<SecondFragment>(R.id.fragmentContainer)
                addToBackStack(null)
            }
        }

        binding.btnReplace.setOnClickListener {
            supportFragmentManager.commit {
                replace<SecondFragment>(R.id.fragmentContainer)
                addToBackStack(null)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}