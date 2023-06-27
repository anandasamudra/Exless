package com.exless.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exless.R
import com.exless.adapter.adapter_detail_menu_resep
import com.exless.model.model_detail_menu_resep
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class detail_menu_resep : AppCompatActivity() {

    private lateinit var tv_namamasakan: TextView
    private lateinit var iv_gambarmasakan: ImageView
    private lateinit var databaseReference: DatabaseReference
    private lateinit var adapterBahan: adapter_detail_menu_resep
    private lateinit var daftarBahan: MutableList<model_detail_menu_resep>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = true
        }
        setContentView(R.layout.activity_detail_menu_resep)
        supportActionBar?.hide()

        val tombolkembali: ImageView = findViewById(R.id.kembali)
        tombolkembali.setOnClickListener {
            val intent = Intent(this, MenuResep::class.java)
            finish()
        }

        // Mendapatkan data yang dikirim dari MenuResep activity
        val namaMasakan = intent.getStringExtra("Nama")
        val gambarMasakan = intent.getStringExtra("Gambar")
        val keyMasakan = intent.getStringExtra("key")

        // Menampilkan TextView dan ImageView
        tv_namamasakan = findViewById(R.id.tv_namamasakan)
        iv_gambarmasakan = findViewById(R.id.iv_gambarmasakan)
        tv_namamasakan.text = "$namaMasakan"
        Picasso.get().load(gambarMasakan).into(iv_gambarmasakan)

        // Implement RecyclerView and Adapter
        val recyclerView: RecyclerView = findViewById(R.id.rv_bahan)
        recyclerView.layoutManager = LinearLayoutManager(this)
        daftarBahan = mutableListOf()
        adapterBahan = adapter_detail_menu_resep(daftarBahan)
        recyclerView.adapter = adapterBahan

        // Implement Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference("Resep/Makanan/$keyMasakan/Bahan")
        databaseReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val key = snapshot.key
                val Nama = snapshot.child("NamaBahan").value.toString()
                val Jumlah = snapshot.child("Jumlah").value.toString()
                val Gambar = snapshot.child("GambarBahan").value.toString()
                val bahan = model_detail_menu_resep(key, Nama, Jumlah, Gambar)
                daftarBahan.add(bahan)
                adapterBahan.notifyItemInserted(daftarBahan.size - 1)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val key = snapshot.key
                val index = daftarBahan.indexOfFirst { it.key == key }
                if (index != -1) {
                    val Nama = snapshot.child("NamaBahan").value.toString()
                    val Jumlah = snapshot.child("Jumlah").value.toString()
                    val Gambar = snapshot.child("GambarBahan").value.toString()
                    daftarBahan[index].NamaBahan = Nama
                    daftarBahan[index].Jumlah = Jumlah
                    daftarBahan[index].GambarBahan = Gambar
                    adapterBahan.notifyItemChanged(index)
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val key = snapshot.key
                val index = daftarBahan.indexOfFirst { it.key == key }
                if (index != -1) {
                    daftarBahan.removeAt(index)
                    adapterBahan.notifyItemRemoved(index)
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

    }
}
