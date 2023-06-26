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
import com.exless.notification.AlarmReceiver
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



    }
    }
