package com.exless.view

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exless.R
import com.exless.adapter.AdapterDetailBerita
import com.exless.`object`.Datarv_detailberita
import com.google.firebase.database.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.InputStream

class DetailBeritaActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterDetailBerita
    private lateinit var databaseRef: DatabaseReference
    private lateinit var array: ArrayList<Datarv_detailberita>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_berita)

        recyclerView = findViewById(R.id.detailberita)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        array = ArrayList()
        adapter = AdapterDetailBerita(array)
        recyclerView.adapter = adapter

        // Inisialisasi dan mengambil referensi dari Firebase Realtime Database
        databaseRef = FirebaseDatabase.getInstance().getReference("Berita")
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                array.clear()
                for (dataSnapshot in snapshot.children) {
                    val title = dataSnapshot.child("title").getValue(String::class.java)
                    val description = dataSnapshot.child("description").getValue(String::class.java)
                    val imageView = dataSnapshot.child("imageView").getValue(Int::class.java)

                    if (title != null && description != null && imageView != null) {
                        val data = Datarv_detailberita(title, description, imageView)
                        array.add(data)

                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
}