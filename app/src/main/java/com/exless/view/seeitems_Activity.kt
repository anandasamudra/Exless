package com.exless.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exless.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class seeitems_Activity : AppCompatActivity() {

    private lateinit var dbref : DatabaseReference
    private lateinit var bahanrecylerview : RecyclerView
    private lateinit var bahanarraylist : ArrayList<datarv_bahan>
    private lateinit var namaBahan: String
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seeitems)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);//disable auto darkmode
        supportActionBar?.hide()
        findViewById<ImageView>(R.id.back_seeitem).setOnClickListener{
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
        findViewById<ImageView>(R.id.bt_addbahan).setOnClickListener{
            startActivity(Intent(this, Tambahbahan_Activity::class.java))
            finish()
        }
        //Recylerview \/\/\/
        retrieveDataFromIntent()
        bahanrecylerview = findViewById(R.id.recyclerView_seeitem)
        bahanrecylerview.layoutManager = LinearLayoutManager(this)
        bahanrecylerview.setHasFixedSize(true)

        bahanarraylist = arrayListOf<datarv_bahan>()
        val namabahan = intent.getStringExtra("nama_bahan")
        var text = findViewById<TextView>(R.id.tvtitleseeitem).setText(namabahan)
        getbahandata(namabahan.toString())

    }

    private fun getbahandata(nama : String) {
//
        var currentuser = FirebaseAuth.getInstance().currentUser?.uid.toString()
        dbref = FirebaseDatabase.getInstance().getReference("/Users/"+currentuser+"/Inventory")
        dbref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (bahanSnapshot in snapshot.children){
                        if (nama == bahanSnapshot.child("jenismakanan").getValue().toString()){
                            val bahan = bahanSnapshot.getValue(datarv_bahan::class.java)
                            bahanarraylist.add(bahan!!)
                        }

                    }
                    bahanrecylerview.adapter = adapter_bahan(bahanarraylist)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    //Recylerview /\/\/\
    private fun retrieveDataFromIntent() {
        val intent = intent
        if (intent.hasExtra("nama_bahan")) {
            namaBahan = intent.getStringExtra("nama_bahan").toString()
        } else {
            // Handle the case when the intent extra is not available
        }
    }
}