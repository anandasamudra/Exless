package com.exless.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.exless.R

class seeitems_Activity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seeitems)
        findViewById<ImageView>(R.id.bt_addbahan).setOnClickListener{
            startActivity(Intent(this, Tambahbahan_Activity::class.java))
            finish()
        }
    }
}