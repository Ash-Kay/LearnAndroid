package io.ashkay.learnandroid.scheduler

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class ServerSyncWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        println("ServerSyncWorker: did work at ${System.currentTimeMillis()}")
        return Result.success()
    }
}