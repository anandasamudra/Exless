package com.exless.notification

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.exless.R
import com.exless.view.SplassScreen

class NotificationHelper(val context: Context) {
    private val CHANNEL_ID = "todo_channel_id"
    private val NOTIFICATION_ID = 1

    private fun notificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_ID,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Todo channel description"
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("MissingPermission")
    private fun hasPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.VIBRATE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun showPermissionRequestNotification() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.logonotif)
            .setContentTitle("Permission Required")
            .setContentText("Please grant notification permission")
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        val notification = notificationBuilder.build()

        try {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_NOTIFICATION_POLICY) == PackageManager.PERMISSION_GRANTED) {
                NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
            } else {
                throw SecurityException("Notification permission not granted")
            }
        } catch (e: SecurityException) {
            // Handle the security exception here
            e.printStackTrace()
            // You can choose to log the error, show a toast, or handle it in any other way
        }
    }


    @SuppressLint("MissingPermission")
    fun createNotification(title: String, message: String) {
        notificationChannel()

        try {
            if (hasPermission()) {
                val intent = Intent(context, SplassScreen::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                }
                val pendingIntent = PendingIntent.getActivity(context, 0, intent,
                    PendingIntent.FLAG_IMMUTABLE)
                // Inflate the custom layout
                val contentView = RemoteViews(context.packageName, R.layout.notification)

                // Set the content of the custom layout
                contentView.setTextViewText(R.id.titlenotif, title)
                contentView.setTextViewText(R.id.description, message)

                // Create the notification with the custom view
                val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.logonotif)
                    .setCustomContentView(contentView)
                    .setStyle(NotificationCompat.InboxStyle()
                        .addLine(title)
                        .addLine(message)
                        .setSummaryText("Sudah makan?"))
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    notificationBuilder.setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN)
                }

                val notification = notificationBuilder.build()

                NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
            } else {
                showPermissionRequestNotification()
            }
        } catch (e: SecurityException) {
            // Handle the security exception (e.g., show an error message)
            e.printStackTrace()
        }
    }
}
