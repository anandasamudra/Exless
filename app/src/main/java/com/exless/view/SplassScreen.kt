package com.exless.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.core.view.WindowInsetsControllerCompat
import com.exless.R


class SplassScreen : AppCompatActivity() {
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
        NotificationHelper(this).createNotification("hai",
            "huha!")

    }
}