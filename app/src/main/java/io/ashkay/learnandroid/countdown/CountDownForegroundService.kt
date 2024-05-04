package io.ashkay.learnandroid.countdown

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.CountDownTimer
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import io.ashkay.learnandroid.R
import io.ashkay.learnandroid.countdown.CountDownActivity.Companion.COUNT_DOWN_CHANNEL
import kotlinx.coroutines.flow.MutableStateFlow


class CountDownForegroundService : Service() {

    val TAG = "CountDownForegroundService:"
    val timeLeft = MutableStateFlow(-1)
    var countDownTimer: CountDownTimer? = null
    val notificationId = 1

    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun getService(): CountDownForegroundService = this@CountDownForegroundService
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground()
        return START_STICKY
    }

    private fun startForeground() {
        startForeground(notificationId, getNotification(""))
    }

    private fun getNotification(timeLeft: String): Notification {
        val openCountDownActivity = Intent(this, CountDownActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(this, 0, openCountDownActivity, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(this, COUNT_DOWN_CHANNEL)
            .setContentTitle("Count Down Timer")
            .setContentText("Time Left => $timeLeft")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .build()
    }

    private fun updateNotification(timeLeft: String) {
        val notification = getNotification(timeLeft)
        val mNotificationManager = NotificationManagerCompat.from(this)

        try {
            mNotificationManager.notify(notificationId, notification)
        } catch (e: Exception) {
            println(e)
        }
    }

    fun startCountdown(millisInFuture: Long) {
        if (timeLeft.value > 0)
            return

        countDownTimer = object : CountDownTimer(millisInFuture, 1_000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft.value = (millisUntilFinished / 1000).toInt()
                updateNotification(timeLeft.value.toString())
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