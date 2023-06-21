package com.exless.view

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exless.R
import com.exless.adapter.AdapterDetailBerita
import com.exless.`object`.Datarv_detailberita
import com.google.firebase.database.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.InputStream

class DetailBeritaActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterDetailBerita
    private lateinit var database: DatabaseReference
    private lateinit var dataListener: ValueEventListener

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_berita)

        recyclerView = findViewById(R.id.detailberita)

        val layoutManager = GridLayoutManager(this, 2) // Sesuaikan dengan jumlah kolom yang diinginkan
        recyclerView.layoutManager = layoutManager

        adapter = AdapterDetailBerita(this, mutableListOf())
        recyclerView.adapter = adapter
        val storageRef: StorageReference = FirebaseStorage.getInstance().reference
        database = FirebaseDatabase.getInstance().reference.child("Berita")
        val imageRef: StorageReference = storageRef.child("berita1.jpg")

        val imageResource = R.drawable.berita1 // Ganti dengan ID gambar dari resource Android Studio
        val file: InputStream = resources.openRawResource(imageResource)

        val uploadTask = imageRef.putStream(file)
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            imageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                val imageUrl = downloadUri.toString()

                // Selanjutnya, simpan imageUrl bersama dengan teks ke Realtime Database
            } else {
                // Penanganan kesalahan saat mengunggah gambar
            }

        }

        // Membaca data dari Firebase
        dataListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataList = mutableListOf<Datarv_detailberita>()
                for (dataSnapshot in snapshot.children) {
                    val imageView = dataSnapshot.child("Berita 1").getValue(Int::class.java) ?: 0
                    val tanggal = dataSnapshot.child("Tanggal Upload").getValue(String::class.java) ?: ""
                    val data = Datarv_detailberita(imageView, tanggal)
                    dataList.add(data)
                }
                adapter.setData(dataList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        }

        database.addValueEventListener(dataListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        database.removeEventListener(dataListener)
    }
}