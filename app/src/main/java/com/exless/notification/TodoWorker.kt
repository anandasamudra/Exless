package com.exless.notification

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class TodoWorker(val context: Context, val params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        val title = inputData.getString("TITLE").toString()
        val message = inputData.getString("MESSAGE").toString()
        NotificationHelper(context).createNotification(title, message)
        return Result.success()
    }
}