package com.exless.view

import AdapterDetailBerita
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exless.R
import com.exless.`object`.Datarv_detailberita
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DetailBeritaActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterDetailBerita
    private lateinit var databaseRef: DatabaseReference
    private lateinit var array: ArrayList<Datarv_detailberita>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_berita)
        findViewById<ImageView>(R.id.back_detailberita).setOnClickListener{
            startActivity(Intent( this,MainActivity::class.java))
        }
        recyclerView = findViewById(R.id.detailberita)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        array = ArrayList()
        adapter = AdapterDetailBerita(array)
        recyclerView.adapter = adapter

        databaseRef = FirebaseDatabase.getInstance().getReference("Berita")
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                array.clear()
                for (dataSnapshot in snapshot.children) {
                    val title = dataSnapshot.child("title").getValue(String::class.java)
                    val description = dataSnapshot.child("description").getValue(String::class.java)
                    val imageUrl = dataSnapshot.child("imageUrl").getValue(String::class.java)
                    val newsUrl = dataSnapshot.child("newsUrl").getValue(String::class.java)
                    if (title != null && description != null && imageUrl != null && newsUrl != null) {
                        val data = Datarv_detailberita(title, description, imageUrl,newsUrl)
                        array.add(data)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }
}
