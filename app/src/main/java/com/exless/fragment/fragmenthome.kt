package com.exless.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exless.R
import com.exless.adapter.adapter_berita
import com.exless.adapter.adapter_resep
import com.exless.databinding.PemberitahuanMakananterisiBinding
import com.exless.`object`.object_berita
import com.exless.`object`.object_resep
import com.exless.view.Tambahbahan_Activity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class fragmenthome : Fragment(R.layout.fragment_home) {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var userRef: DatabaseReference
    private lateinit var currentUser: FirebaseUser
    private lateinit var textViewNama: TextView
    private lateinit var pemberitahuanMakananterisiBinding: PemberitahuanMakananterisiBinding

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

        // Mendapatkan nama pengguna setelah login dengan Google
        val googleSignInAccount: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(requireContext())
        if (googleSignInAccount != null) {
            val fullName = googleSignInAccount.displayName
            textViewNama = rootView.findViewById(R.id.nama)
            textViewNama.text = fullName
        }

        //menampilkan aktivitas makanan
        val pemberitahuanMakananterisiLayout = rootView.findViewById<View>(R.id.pemberitahuan_terisi)
        val pemberitahuanMakananexpired = rootView.findViewById<View>(R.id.pemberitahuan_expired)
        val pemberitahuanKosong = rootView.findViewById<View>(R.id.aktifitas_kosong)
        userRef = database.reference.child("Users").child(currentUser.uid).child("Inventory")
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    pemberitahuanMakananterisiLayout.visibility = View.VISIBLE
                    pemberitahuanMakananexpired.visibility = View.VISIBLE
                    pemberitahuanKosong.visibility = View.GONE
                }else{
                    pemberitahuanMakananterisiLayout.visibility = View.GONE
                    pemberitahuanMakananexpired.visibility = View.GONE
                    pemberitahuanKosong.visibility = View.VISIBLE
                }
            }
            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })

        return rootView



    }
}