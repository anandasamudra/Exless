package com.exless.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.exless.notification.ExpiredWorker
import com.google.firebase.database.DatabaseReference
import java.util.concurrent.TimeUnit

class AlarmReceiver : BroadcastReceiver() {
    private lateinit var dbref : DatabaseReference

    override fun onReceive(context: Context, intent: Intent) {
        val workRequest = PeriodicWorkRequest.Builder(
            ExpiredWorker::class.java,
            15, // Repeat interval (in hours)
            TimeUnit.MINUTES
        ).build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }

}