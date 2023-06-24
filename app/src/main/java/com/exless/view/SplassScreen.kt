package com.exless.view

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.core.view.WindowInsetsControllerCompat
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.exless.R
import java.util.Calendar
import java.util.concurrent.TimeUnit


class SplassScreen : AppCompatActivity() {
    private lateinit var alarmManager: AlarmManager
    private lateinit var alarmIntent: Intent
    private lateinit var pendingIntent: PendingIntent
    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //adaptasi statusbar
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = true
        }
        setContentView(R.layout.activity_splass_screen)
        //hiden action bar
        supportActionBar?.hide()
        //menyimpan data riwayt login dengan sharedpreferences
        val handler= Handler(Looper.getMainLooper())
        handler.postDelayed({
            if (getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isLogin", false)) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, onbonding1::class.java))
                finish()
            }
        }, 3000)
//        NotificationHelper(this).createNotification("hai",
//            "huha!")
//val workreq = OneTimeWorkRequestBuilder<TodoWorker>()
//    .setInitialDelay(10,TimeUnit.SECONDS)
//    .setInputData(workDataOf("TITLE" to "ta", "MESSAGE" to "a new a"))
//    .build()
//        WorkManager.getInstance(this).enqueue(workreq)
        ///////////////////
//        val workRequest = PeriodicWorkRequest.Builder(
//            TodoWorker::class.java,
//            15, // Repeat interval (in days)
//            TimeUnit.MINUTES
//        ).setInputData(workDataOf("TITLE" to "v", "MESSAGE" to "a new v"))
//            .build()
//
//        // Enqueue the work request
//        WorkManager.getInstance(this).enqueue(workRequest)
        // Schedule periodic task using AlarmManager
//        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        alarmIntent = Intent(this, AlarmReceiver::class.java)
//        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0)
//
//        val calendar = Calendar.getInstance()
//        calendar.timeInMillis = System.currentTimeMillis()

//        // Set the desired time for the periodic task (e.g., 8:00 AM)
//        calendar.set(Calendar.HOUR_OF_DAY, 22)
//        calendar.set(Calendar.MINUTE,27)
//        calendar.set(Calendar.SECOND, 0)
//
//// Check if the desired time has already passed for today
//        if (calendar.timeInMillis < System.currentTimeMillis()) {
//            // If the time has already passed, schedule the task for the next day
//            calendar.add(Calendar.DAY_OF_YEAR, 1)
//        }
//
//// Set the repeating interval (24 hours)
//        val intervalMillis: Long = 1 * 1000 // 24 hours
//
//// Schedule the periodic task
//        alarmManager.setRepeating(
//            AlarmManager.RTC_WAKEUP,
//            calendar.timeInMillis,
//            intervalMillis,
//            pendingIntent
//        )
        // Set the desired time for the one-time alarm
//        val calendar = Calendar.getInstance()
//        calendar.timeInMillis = System.currentTimeMillis()
//        calendar.set(Calendar.HOUR_OF_DAY, 22)
//        calendar.set(Calendar.MINUTE, 0)
//        calendar.set(Calendar.SECOND, 43)
//
//// Create an intent for the AlarmReceiver
//        val alarmIntent = Intent(this, AlarmReceiver::class.java)
//        val pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0)
//
//// Schedule the one-time alarm
//        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
//        //
//        val desiredCalendar = Calendar.getInstance()
//        desiredCalendar.set(Calendar.HOUR_OF_DAY, 23)
//        desiredCalendar.set(Calendar.MINUTE, 45)
//        desiredCalendar.set(Calendar.SECOND, 0)
//
//        val currentCalendar = Calendar.getInstance()
//        val currentTimeMillis = System.currentTimeMillis()
//
//// Check if the desired date and time have already passed
//        if (currentCalendar.before(desiredCalendar)) {
//            // Create an intent for the AlarmReceiver
//            val alarmIntent = Intent(this, AlarmReceiver::class.java)
//            val pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0)
//
//            // Schedule the one-time alarm
//            val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP, desiredCalendar.timeInMillis, pendingIntent)
//        } else {
//            println("bakekok")
//        }
        val workRequest = PeriodicWorkRequest.Builder(
            ExpiredWorker::class.java,
            24, // Repeat interval (in hours)
            TimeUnit.HOURS
        ).build()

        WorkManager.getInstance(this).enqueue(workRequest)
    }
    }
