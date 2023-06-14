package com.exless.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import com.exless.R
import com.exless.view.fragment.fragmentbelanja
import com.exless.view.fragment.fragmenthome
import com.exless.view.fragment.fragmentkomunitas
import com.exless.view.fragment.fragmentsimpanan
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = true
        }
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

//bottom navigation fragment \/\/\/
        val fraghome = fragmenthome()
        val fragbel = fragmentbelanja()
        val fragsim = fragmentsimpanan()
        val fragkom = fragmentkomunitas()
        setfragment(fraghome)
        findViewById<BottomNavigationView>(R.id.bottomNavigationView_layout).setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home -> setfragment(fraghome)
                R.id.inventory -> setfragment(fragsim)
                R.id.shop -> setfragment(fragbel)
                R.id.comunity -> setfragment(fragkom)
            }
            true
        }
        // bottom navigation fragment /\/\/\

    }
    //bottom navigation fragment \/\/\/
    private fun setfragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_framelayout,fragment)
            commit()
        }
    // bottom navigation fragment /\/\/\
    fun totambah(view: View) {
        startActivity(Intent(this, Tambahbahan_Activity::class.java))
        finish()
    }
    fun toseeitem(view: View) {
        startActivity(Intent(this, seeitems_Activity::class.java))
        finish()
    }
}