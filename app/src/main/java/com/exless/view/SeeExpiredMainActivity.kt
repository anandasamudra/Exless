package com.exless.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exless.R
import com.exless.adapter.adapter_seeexpiredori
import com.exless.`object`.Datarv_seeexperired
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.jakewharton.threetenabp.AndroidThreeTen
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar

class SeeExpiredMainActivity : AppCompatActivity() {
    private lateinit var dbrefex: DatabaseReference
    private lateinit var dbqueryex: Query
    lateinit var jumbahanmainex: ArrayList<String>
    private lateinit var rv_list_jenisbahan: RecyclerView
    private var jenisbahanarraylist = ArrayList<Datarv_seeexperired>()
    private lateinit var bahanrecylerview: RecyclerView
    private lateinit var bahanarrayliste: ArrayList<Datarv_seeexperired>
    private lateinit var bahanViewModel: BahanViewModel
    private var dataList: ArrayList<Datarv_seeexperired>? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_see_expired)
        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = true
        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) // disable auto dark mode
        // Initialize ThreeTenABP
        AndroidThreeTen.init(this)
        rv_list_jenisbahan = findViewById(R.id.rv_seeexpiredori)
        rv_list_jenisbahan.setHasFixedSize(true)
        jumbahanmainex = ArrayList()
        bahanarrayliste = ArrayList<Datarv_seeexperired>()
        getbahandataex()
    }

    fun toback(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    @SuppressLint("SuspiciousIndentation")
    private fun getbahandataex() {
        val currentuser = FirebaseAuth.getInstance().currentUser?.uid.toString()
        dbrefex = FirebaseDatabase.getInstance().getReference("/Users/$currentuser/Inventory")
        dbrefex.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    bahanarrayliste.clear() // Clear the list before adding new items
                    for (bahanSnapshot in snapshot.children) {
                        val namabahan = bahanSnapshot.child("nama").getValue().toString()
                        //compare today date with expired date \/\/\/
                        var tglkadal: String
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
                            } catch (e: Exception) {
                                // Handle any exceptions that might occur
                                tglkadal = "EXPIRED"
                                println("An error occurred: ${e.message}")
                            }
                        } else {
                            tglkadal = "-"
                        }

                        println("Number of days: $tglkadal")
                        //compare today date with expired date /\/\/\
                        val jumlah = bahanSnapshot.child("jumlah").getValue().toString()
                        val bahanList = Datarv_seeexperired(
                            namabahan,
                            tglkadal,
                            jumlah,
                            0
                        )
                        bahanarrayliste.add(bahanList)
                        println(bahanarrayliste)
                        println(namabahan)
                    }
                    sortBahanArrayByExpirationDate() // Sort the bahanarrayliste
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the cancellation
            }
        })

        showRecylerviewex()
    }

    private fun sortBahanArrayByExpirationDate() {
        bahanarrayliste.sortWith(Comparator { bahan1, bahan2 ->
            when {
                bahan1.tglkadaluarsa == "EXPIRED" && bahan2.tglkadaluarsa != "EXPIRED" -> -1 // bahan1 is "EXPIRED" and bahan2 is not, so bahan1 comes first
                bahan1.tglkadaluarsa != "EXPIRED" && bahan2.tglkadaluarsa == "EXPIRED" -> 1 // bahan2 is "EXPIRED" and bahan1 is not, so bahan2 comes first
                else -> bahan1.tglkadaluarsa.compareTo(bahan2.tglkadaluarsa) // Compare other cases based on the expiration date string
            }
        })
    }


    fun showRecylerviewex() {
        rv_list_jenisbahan.layoutManager = LinearLayoutManager(this)
        rv_list_jenisbahan.adapter = adapter_seeexpiredori(bahanarrayliste)
    }

    private fun getCurrentDate(): String {
        val c = Calendar.getInstance()
        val day = c.get(Calendar.DAY_OF_MONTH)
        val month = c.get(Calendar.MONTH) + 1 // Months are zero-based, so add 1
        val year = c.get(Calendar.YEAR)
        return String.format("%02d/%02d/%04d", day, month, year)
    }
    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
