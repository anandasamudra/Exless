package com.exless.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.widget.ButtonBarLayout
import com.exless.R

class onbonding1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onbonding1)

        supportActionBar?.hide()
        val buttomskip = findViewById<Button>(R.id.skip)
        buttomskip.setOnClickListener{
            val intent = Intent(this, onbonding2::class.java)
            startActivity(intent)
        }

        val buttomnext = findViewById<Button>(R.id.next)
        buttomnext.setOnClickListener {
            val intent = Intent(this, onbonding2::class.java)
            startActivity(intent)
        }
    }
}