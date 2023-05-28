package com.exless.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.exless.LoginActivity
import com.exless.R
import com.exless.Register_Activity

class onbonding3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onbonding3)

        supportActionBar?.hide()

        val buttomnext = findViewById<Button>(R.id.next)
        buttomnext.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}