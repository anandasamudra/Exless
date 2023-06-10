package com.exless.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.exless.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//bottom navigation \/\/\/
//        val satufrag = firstfragment()
//        val duafrag = secondfragment()
//        setfragment(satufrag)
//        findViewById<BottomNavigationView>(R.id.bottomNavigationView).setOnNavigationItemSelectedListener {
//            when(it.itemId){
//                R.id.home -> setfragment(satufrag)
//                R.id.dua -> setfragment(duafrag)
//            }
//            true
//        }
        // bottom navigation /\/\/\
    }
}