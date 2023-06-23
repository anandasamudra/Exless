package com.exless.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
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
import com.exless.model.model_resep
import com.exless.`object`.object_berita
import com.exless.view.MenuResep
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class fragmenthome : Fragment(R.layout.fragment_home) {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var userRef: DatabaseReference
    private lateinit var currentUser: FirebaseUser
    private lateinit var textViewNama: TextView
    private lateinit var textViewfullresep: TextView

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        // pindah ke activity resep makanan activity
        textViewfullresep = rootView.findViewById(R.id.fullresep)
        textViewfullresep.setOnClickListener {
            val intent = Intent(requireContext(), MenuResep::class.java)
            startActivity(intent)
        }
        // recycleview resep makanan
        val recyclerViewresep = rootView.findViewById<RecyclerView>(R.id.rv_list_resep)
        recyclerViewresep.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        database = FirebaseDatabase.getInstance()
        userRef = database.reference.child("Resep").child("Makanan")
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val daftarResep = mutableListOf<model_resep>()

                    for (resepSnapshot in snapshot.children) {
                        val key = resepSnapshot.key
                        val durasi = resepSnapshot.child("Durasi").getValue(String::class.java)
                        val gambar = resepSnapshot.child("Gambar").getValue(String::class.java)
                        val resep = model_resep(key, durasi, gambar)
                        daftarResep.add(resep)
                    }

                    val adapter = adapter_resep(daftarResep)
                    recyclerViewresep.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })

        // recycleview berita
        val recyclerViewberita = rootView.findViewById<RecyclerView>(R.id.rv_list_berita)
        recyclerViewberita.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerViewberita.adapter = adapter_berita(object_berita.DaftarBerita)

        // Mendapatkan nama pengguna terdaftar
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        currentUser = firebaseAuth.currentUser!!

        userRef = database.reference.child("Users").child(currentUser.uid)
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val fullName = snapshot.child("FullName").value.toString()
                    textViewNama = rootView.findViewById<TextView>(R.id.nama)
                    textViewNama.text = fullName
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })

        // Mendapatkan nama pengguna setelah login dengan Google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val googleSignInAccount: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(requireContext())
            if (googleSignInAccount != null) {
                val FullName = googleSignInAccount.displayName
                textViewNama = rootView.findViewById(R.id.nama)
                textViewNama.text = FullName
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
                } else {
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
