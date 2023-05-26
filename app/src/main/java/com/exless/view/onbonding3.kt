package com.exless.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.exless.R

class onbonding3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onbonding3)

        supportActionBar?.hide()

        val buttomnext = findViewById<Button>(R.id.next)
        buttomnext.setOnClickListener {
            val intent = Intent(this, onbonding2::class.java)
            startActivity(intent)
        }
    }
}