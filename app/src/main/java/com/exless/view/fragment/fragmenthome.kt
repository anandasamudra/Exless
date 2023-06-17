package com.exless.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exless.R
import com.exless.view.adapter.adapter_berita
import com.exless.view.adapter.adapter_resep
import com.exless.view.`object`.object_berita
import com.exless.view.`object`.object_resep
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class fragmenthome : Fragment(R.layout.fragment_home) {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var userRef: DatabaseReference
    private lateinit var currentUser: FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        // recycleview resep makanan
        val recyclerViewresep = rootView.findViewById<RecyclerView>(R.id.rv_list_resep)
        recyclerViewresep.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerViewresep.adapter = adapter_resep(object_resep.DaftarResep)

        // recycleview berita
        val recyclerViewberita = rootView.findViewById<RecyclerView>(R.id.rv_list_berita)
        recyclerViewberita.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerViewberita.adapter = adapter_berita(object_berita.DaftarBerita)

        // Mendapatkan nama pengguna terdaftar
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        currentUser = firebaseAuth.currentUser!!

        userRef = database.reference.child("users").child(currentUser.uid)
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val fullName = snapshot.child("fullName").value.toString()
                    val textViewNama = rootView.findViewById<TextView>(R.id.nama)
                    textViewNama.text = fullName
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })

        return rootView
    }
}