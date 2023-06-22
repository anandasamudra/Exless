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
import com.exless.adapter.adapter_jenisbahan
import com.exless.adapter.adapter_seeexpiredsimpanan
import com.exless.fragment.fragmentbelanja
import com.exless.fragment.fragmenthome
import com.exless.fragment.fragmentkomunitas
import com.exless.fragment.fragmentsimpanan
import com.exless.`object`.Datarv_jenisbahan
import com.exless.`object`.Datarv_seeexperired
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
//get days
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import com.jakewharton.threetenabp.AndroidThreeTen
import java.time.format.DateTimeParseException
import java.util.Calendar


class MainActivity : AppCompatActivity() {
    private lateinit var dbref : DatabaseReference
    private lateinit var dbquery : Query
    lateinit var jumbahanmain : ArrayList<String>
     lateinit var bahanarraylist : ArrayList<Datarv_jenisbahan>
    private lateinit var rv_list_jenisbahan: RecyclerView
    //
    private lateinit var dbrefex : DatabaseReference
    private lateinit var dbqueryex : Query
    lateinit var jumbahanmainex : ArrayList<String>
    lateinit var bahanarraylistex : ArrayList<Datarv_seeexperired>
    private lateinit var rv_list_jenisbahanex: RecyclerView

    @SuppressLint("MissingInflatedId")
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)//disable auto darkmode
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = true
        }
        setContentView(R.layout.activity_main)
        firebaseAuth = FirebaseAuth.getInstance()
        supportActionBar?.hide()
// Initialize ThreeTenABP
        AndroidThreeTen.init(this)
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

        //Jumlah makanan di kategori fragment simpanan \/\/\/
        val fragmensimpan = layoutInflater.inflate(R.layout.fragment_simpanan, null)
        rv_list_jenisbahan = fragmensimpan.findViewById(R.id.rv_jenisbahan)
        rv_list_jenisbahan.setHasFixedSize(true)
        jumbahanmain = ArrayList()
        bahanarraylist = ArrayList<Datarv_jenisbahan>()
        getbahandata()
        //
        val fragmensimpanex = layoutInflater.inflate(R.layout.fragment_simpanan, null)
        rv_list_jenisbahanex = fragmensimpanex.findViewById(R.id.rv_seeexpired)
        rv_list_jenisbahanex.setHasFixedSize(true)
        jumbahanmainex = ArrayList()
        bahanarraylistex = ArrayList<Datarv_seeexperired>()
        getbahandataex()

        //Jumlah makanan di kategori fragment simpanan /\/\/\
    }

    //bottom navigation fragment \/\/\/
    private fun setfragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_framelayout,fragment)
            commit()
        }
    // bottom navigation fragment /\/\/\
    fun toprofile(view: View) {
        startActivity(Intent(this, Profile_Activity::class.java))
        finish()
    }
    fun toaddbahan(view: View) {
        startActivity(Intent(this, TambahbahanMain_Activity::class.java))
        finish()
    }
    fun toseeitem(view: View) {
        startActivity(Intent(this, seeitems_Activity::class.java))
        finish()
    }
    fun tosimpanan(view: View) {
        setfragment(fragmentsimpanan())
        findViewById<BottomNavigationView>(R.id.bottomNavigationView_layout).selectedItemId =R.id.inventory
    }
    fun toseeexpired(view: View) {
        startActivity(Intent(this, SeeExpiredActivity::class.java))
        finish()
    }
    fun detailberita(view: View) {
        startActivity(Intent(this,DetailBeritaActivity::class.java))
        finish()
    }

    //Jumlah makanan di kategori fragment simpanan \/\/\/
    @SuppressLint("SuspiciousIndentation")
    private fun getbahandata() {
        val currentuser = FirebaseAuth.getInstance().currentUser?.uid.toString()
        dbref = FirebaseDatabase.getInstance().getReference("/Users/$currentuser/Inventory")
        val dataTitle = resources.getStringArray(R.array.data_name)
        val dataimage = resources.obtainTypedArray(R.array.data_photo)
        for (i in dataTitle.indices) {
        dbquery = dbref.orderByChild("jenismakanan").equalTo(dataTitle[i])
        dbquery.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val count: String = snapshot.childrenCount.toString()
//                println("countmain"+count)
                jumbahanmain.add(count)
                val bahanList = Datarv_jenisbahan(
                    dataTitle[i],
                    "",
                    dataimage.getResourceId(i, -1)
                )
                bahanList.description = "Kamu mempunyai $count macam"
                bahanarraylist.add(bahanList)
//                println(bahanarraylist)

//                println(jumbahanmain+"ini datanya cokkkk")
//                println("done datachange")

            }
            override fun onCancelled(error: DatabaseError) {
                // Handle the error
            }
        })
        }
        showRecylerview()
    }///////////////////////////////////////////////////////
    @SuppressLint("SuspiciousIndentation")
    private fun getbahandataex() {
        val currentuser = FirebaseAuth.getInstance().currentUser?.uid.toString()
        dbrefex = FirebaseDatabase.getInstance().getReference("/Users/$currentuser/Inventory")
        dbrefex.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (bahanSnapshot in snapshot.children){
                        val namabahan = bahanSnapshot.child("nama").getValue().toString()
                        //compare today date with expired date \/\/\/
                        var tglkadal : String
                        if (bahanSnapshot.child("tglkadaluarsa").getValue().toString() != "") {
                            tglkadal = bahanSnapshot.child("tglkadaluarsa").getValue().toString()

                            try {
                                val startDateStr = getCurrentDate()
                                val endDateStr = tglkadal
                                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                                val startDate = LocalDate.parse(startDateStr, formatter)
                                val endDate = LocalDate.parse(endDateStr, formatter)

                                if (endDate.isBefore(startDate)) {
                                    tglkadal = "EXPIRED"
                                } else {
                                    val days = ChronoUnit.DAYS.between(startDate, endDate)
                                    tglkadal = days.toString() + " hari"
                                }

                                println("Number of days: $tglkadal")
                            } catch (e: DateTimeParseException) {
                                // Handle the case when parsing the dates fails
                                tglkadal = "EXPIRED"
                                println("Invalid date format")
                            } catch (e: Exception) {
                                // Handle any other exceptions that might occur
                                tglkadal = "EXPIRED"
                                println("An error occurred: ${e.message}")
                            }
                        }

                        else{
                            tglkadal ="-"
                        }
                        println("Number of dayssadasda f: $tglkadal")
                        //compare today date with expired date /\/\/\
                        val jumlah = bahanSnapshot.child("jumlah").getValue().toString()
                        val bahanList = Datarv_seeexperired(
                            namabahan,
                            tglkadal,
                            jumlah,
                            0
                        )
                        bahanarraylistex.add(bahanList)
                        println(bahanarraylistex)
                        println(namabahan)
                    }
//                    bahanrecylerview.adapter = adapter_bahan(bahanarraylist)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        showRecylerviewex()
    }
    fun showRecylerview(){
        rv_list_jenisbahan.layoutManager = LinearLayoutManager(this)
        rv_list_jenisbahan.adapter= adapter_jenisbahan(bahanarraylist)
    }
    fun showRecylerviewex(){
        rv_list_jenisbahanex.layoutManager = LinearLayoutManager(this)
        rv_list_jenisbahanex.adapter= adapter_seeexpiredsimpanan(bahanarraylistex)
    }
    fun getBahanArrayList(): ArrayList<Datarv_jenisbahan> {
        return bahanarraylist
    }
    fun getBahanArrayListex(): ArrayList<Datarv_seeexperired> {
        return bahanarraylistex
    }
    //Jumlah makanan di kategori fragment simpanan /\/\/\
    private fun getCurrentDate(): String {
        val c = Calendar.getInstance()
        val day = c.get(Calendar.DAY_OF_MONTH)
        val month = c.get(Calendar.MONTH) + 1 // Months are zero-based, so add 1
        val year = c.get(Calendar.YEAR)
        return String.format("%02d/%02d/%04d", day, month, year)
    }


}