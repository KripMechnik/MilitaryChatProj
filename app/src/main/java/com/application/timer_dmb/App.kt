package com.application.timer_dmb

import android.Manifest
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.BackoffPolicy
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.application.timer_dmb.presentation.widget.TimeCounterWorker
import dagger.hilt.android.HiltAndroidApp
import java.time.Duration
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltAndroidApp
class App : Application(), Configuration.Provider{

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel("1")
        val workRequest = PeriodicWorkRequestBuilder<TimeCounterWorker>(
            repeatInterval = 15,
            repeatIntervalTimeUnit = TimeUnit.MINUTES,
            flexTimeInterval = 0L,
            flexTimeIntervalUnit = TimeUnit.MINUTES
        )
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                Duration.ofSeconds(1)
            )
            .build()

        val workManager = WorkManager.getInstance(applicationContext)
        workManager.enqueueUniquePeriodicWork(
            "timeCount",
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }

    private fun createNotificationChannel(channelId: String) {
        val channel = NotificationChannel(
            channelId,
            "Custom Notifications",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Channel for custom notifications"
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}