package com.exless.view

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.workDataOf
import java.util.Calendar
import java.util.concurrent.TimeUnit

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val desiredCalendar = Calendar.getInstance()
        desiredCalendar.set(Calendar.HOUR_OF_DAY, 23)
        desiredCalendar.set(Calendar.MINUTE, 5)
        desiredCalendar.set(Calendar.SECOND, 0)
        val currentTimeMillis = System.currentTimeMillis()
        // Start the WorkManager periodic task here
        val workRequest = PeriodicWorkRequest.Builder(
            TodoWorker::class.java,
            15, // Repeat interval (in days)
            TimeUnit.MINUTES
        )
            .setInitialDelay(5,TimeUnit.SECONDS)
            .setInputData(workDataOf("TITLE" to "sekarang $currentTimeMillis", "MESSAGE" to "tujuan $desiredCalendar"))
            .build()

        // Enqueue the work request
        WorkManager.getInstance(context).enqueue(workRequest)
    }
}