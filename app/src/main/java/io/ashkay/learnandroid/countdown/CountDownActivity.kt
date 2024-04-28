package io.ashkay.learnandroid.countdown

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import io.ashkay.learnandroid.databinding.ActivityCountDownBinding
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class CountDownActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCountDownBinding
    val TAG = "CountDownActivity:"

    private var boundService: CountDownForegroundService? = null
    val isBound = MutableStateFlow(false)

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as CountDownForegroundService.LocalBinder
            boundService = binder.getService()
            isBound.value = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            boundService = null
            isBound.value = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCountDownBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= VERSION_CODES.O) {
            val channel = NotificationChannel(
                COUNT_DOWN_CHANNEL,
                "Countdown Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            NotificationManagerCompat.from(this).createNotificationChannel(channel)
        }

        val intent = Intent(this, CountDownForegroundService::class.java)
        //starts a started service, then binds as bound service destroy if no client
        if (VERSION.SDK_INT >= VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
        bindService(intent, serviceConnection, BIND_AUTO_CREATE)

        binding.btnToggleCountDown.setOnClickListener {
            if (isBound.value) {
                boundService?.startCountdown(30 * 1_000)
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                isBound.collect {
                    if (it) {
                        boundService?.timeLeft?.collect {
                            binding.countDownText.text = "${it} s"
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
    }

    companion object {
        const val COUNT_DOWN_CHANNEL = "COUNT_DOWN_CHANNEL"
    }

}