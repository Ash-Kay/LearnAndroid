package io.ashkay.learnandroid.scheduler

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import io.ashkay.learnandroid.databinding.ActivityBackgroundWorkerBinding
import java.util.concurrent.TimeUnit

class BackgroundWorkerActivity : AppCompatActivity() {
    lateinit var binding: ActivityBackgroundWorkerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBackgroundWorkerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnTogglePeriodicWorker.setOnClickListener {
            scheduleWork()
        }
    }

    private fun schedulePeriodicWork() {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<ServerSyncWorker>(5, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            /* uniqueWorkName = */ "MyWorkerPeriodic",
            /* existingPeriodicWorkPolicy = */ ExistingPeriodicWorkPolicy.UPDATE,
            /* periodicWork = */ workRequest
        )
    }

    private fun scheduleWork() {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        val workRequest1 = OneTimeWorkRequestBuilder<ServerSyncWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST) //!expedited!
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniqueWork(
            /* uniqueWorkName = */ "MyWorker",
            /* existingWorkPolicy = */ ExistingWorkPolicy.REPLACE,
            /* work = */ workRequest1
        )
    }
}