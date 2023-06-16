package com.exless.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exless.R
import com.exless.view.fragment.fragmentbelanja
import com.exless.view.fragment.fragmenthome
import com.exless.view.fragment.fragmentkomunitas
import com.exless.view.fragment.fragmentsimpanan
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var rv_list_jenisbahan: RecyclerView
    private var jenisbahanarraylist = ArrayList<Datarv_jenisbahan>()
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)//disable auto darkmode
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
        rv_list_jenisbahan = findViewById(R.id.rv_jenisbahan)
        rv_list_jenisbahan.setHasFixedSize(true)

        jenisbahanarraylist.addAll(listbahanarray)
        showRecylerview()
    }
    private val listbahanarray: ArrayList<Datarv_jenisbahan>
        get() {
            val dataTitle = resources.getStringArray(R.array.data_name)
            val datadesk = resources.getStringArray(R.array.data_description)
            val dataimage = resources.obtainTypedArray(R.array.data_photo)
            val datalist = ArrayList<Datarv_jenisbahan>()

            for (i in dataTitle.indices){
                val bahanlist = Datarv_jenisbahan(
                    dataTitle[i],
                    datadesk[i],
                    dataimage.getResourceId(i, -1)
                )
                datalist.add(bahanlist)
            }
            return datalist
        }
    fun showRecylerview(){
        rv_list_jenisbahan.layoutManager = LinearLayoutManager(this)
        rv_list_jenisbahan.adapter=adapter_jenisbahan(jenisbahanarraylist)
    }
    //bottom navigation fragment \/\/\/
    private fun setfragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_framelayout,fragment)
            commit()
        }
    // bottom navigation fragment /\/\/\
    fun toaddbahan(view: View) {
        startActivity(Intent(this, Tambahbahan_Activity::class.java))
        finish()
    }
    fun toseeitem(view: View) {
        startActivity(Intent(this, seeitems_Activity::class.java))
        finish()
    }
}