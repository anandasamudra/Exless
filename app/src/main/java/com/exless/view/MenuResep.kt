package com.exless.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.RecyclerView
import com.exless.R
import com.exless.adapter.adapter_menu_resep
import com.exless.model.model_menu_resep
import com.google.firebase.database.*

class MenuResep : AppCompatActivity() {

    private lateinit var adapterMenuResep: adapter_menu_resep
    private lateinit var daftarMenuResep: MutableList<model_menu_resep>
    private lateinit var tombolkembali: ImageView
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = true
        }
        setContentView(R.layout.activity_menu_resep)
        supportActionBar?.hide()

        val tombolkembali: ImageView = findViewById(R.id.kembali)
        tombolkembali.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            finish()
        }

        // implementasi RecyclerView dan Adapter
        val recyclerView: RecyclerView = findViewById(R.id.rv_menu_resep)
        daftarMenuResep = mutableListOf()
        adapterMenuResep = adapter_menu_resep(daftarMenuResep)
        recyclerView.adapter = adapterMenuResep

        // implenetasi Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference("Resep/Makanan")
        databaseReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val key = snapshot.key ?: "key"
                val Gambar = snapshot.child("Gambar").value.toString()
                val Nama = snapshot.child("Nama").value.toString()
                val menuResep = model_menu_resep(key, Nama, Gambar)
                println("ini keyyyyy "+ key)
                daftarMenuResep.add(menuResep)
                adapterMenuResep.notifyItemInserted(daftarMenuResep.size - 1)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val key = snapshot.key ?: "key"
                val index = daftarMenuResep.indexOfFirst { it.key == key }
                if (index != -1) {
                    val nama = snapshot.child("Nama").value.toString()
                    val gambar = snapshot.child("Gambar").value.toString()
                    val menuResep = model_menu_resep(key, nama, gambar)
                    daftarMenuResep[index] = menuResep
                    adapterMenuResep.notifyItemChanged(index)
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val key = snapshot.key ?: "key"
                val index = daftarMenuResep.indexOfFirst { it.key == key }
                if (index != -1) {
                    daftarMenuResep.removeAt(index)
                    adapterMenuResep.notifyItemRemoved(index)
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}
