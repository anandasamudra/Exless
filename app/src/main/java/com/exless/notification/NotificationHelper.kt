package com.exless.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.exless.R
import com.exless.view.SplassScreen

class NotificationHelper(val context:Context) {
    private val CHANNEL_ID ="todo_channel_id"
    private val NOTIFICATION_ID = 1

    private fun notificationChannel() {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val channel = NotificationChannel(CHANNEL_ID,CHANNEL_ID,
                NotificationManager.IMPORTANCE_DEFAULT).apply {
                    description = "Todo channel description"
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    @SuppressLint("MissingPermission")
    fun createNotification(title:String, message:String) {
        notificationChannel()
        val intent = Intent(context, SplassScreen::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE )//
        // Inflate the custom layout
        val contentView = RemoteViews(context.packageName, R.layout.notification)

// Set the content of the custom layout
        contentView.setTextViewText(R.id.title, title)
        contentView.setTextViewText(R.id.description, message)

// Create the notification with the custom view
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.logo)
            .setStyle(NotificationCompat.InboxStyle()
                .addLine(title)
                .addLine(message)
                .setSummaryText("Sudah makan?"))
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID,notification)//missiong permision
    }
}