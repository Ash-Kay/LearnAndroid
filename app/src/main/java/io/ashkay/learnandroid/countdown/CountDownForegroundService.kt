package io.ashkay.learnandroid.countdown

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.CountDownTimer
import android.os.IBinder
import androidx.core.app.NotificationCompat
import io.ashkay.learnandroid.countdown.CountDownActivity.Companion.COUNT_DOWN_CHANNEL
import kotlinx.coroutines.flow.MutableStateFlow


class CountDownForegroundService : Service() {

    val TAG = "CountDownForegroundService:"
    val duration: Long = 30 * 10_00
    val timeLeft = MutableStateFlow(-1)
    var countDownTimer: CountDownTimer? = null

    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun getService(): CountDownForegroundService = this@CountDownForegroundService
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground()
//        startCountdown(duration)
        return START_STICKY
    }

    private fun startForeground() {
        try {
            val notification = NotificationCompat.Builder(this, COUNT_DOWN_CHANNEL)
                .setContentTitle("Count Down Timer")
                .setWhen(System.currentTimeMillis() + duration)
                .setChronometerCountDown(true)
                .setUsesChronometer(true)
                .setAutoCancel(true)
                .setOngoing(true)
                .build()

            startForeground(1, notification)
        } catch (e: Exception) {
            println("$TAG Error: $e")
        }
    }

    fun startCountdown(millisInFuture: Long) {
        if (timeLeft.value > 0)
            return

        countDownTimer = object : CountDownTimer(millisInFuture, 1_000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft.value = (millisUntilFinished / 1000).toInt()
                println("$TAG timeleft : $millisUntilFinished")
            }

            override fun onFinish() {
                timeLeft.value = 0
                stopSelf()
            }
        }.start()
    }

    fun stopCountDown() {
        countDownTimer?.cancel()
    }

    override fun onDestroy() {
        println("$TAG onDestroy")
        countDownTimer?.cancel()
        super.onDestroy()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        println("$TAG onUnbind")
        return super.onUnbind(intent)
    }
}