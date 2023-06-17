package com.exless.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exless.R
import com.exless.view.fragment.fragmentbelanja
import com.exless.view.fragment.fragmenthome
import com.exless.view.fragment.fragmentkomunitas
import com.exless.view.fragment.fragmentsimpanan
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private lateinit var dbref : DatabaseReference
    private lateinit var dbquery : Query
    lateinit var jumbahanmain : ArrayList<String>
     lateinit var bahanarraylist : ArrayList<Datarv_jenisbahan>
    private lateinit var rv_list_jenisbahan: RecyclerView

    @SuppressLint("MissingInflatedId")
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)//disable auto darkmode
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = true
        }
        setContentView(R.layout.activity_main)
        firebaseAuth = FirebaseAuth.getInstance()
        supportActionBar?.hide()

//bottom navigation fragment \/\/\/
        val fraghome = fragmenthome()
        val fragbel = fragmentbelanja()
        val fragsim = fragmentsimpanan()
        val fragkom = fragmentkomunitas()
        setfragment(fraghome)
        findViewById<BottomNavigationView>(R.id.bottomNavigationView_layout).setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home -> setfragment(fraghome)
                R.id.inventory -> setfragment(fragsim)
                R.id.shop -> setfragment(fragbel)
                R.id.comunity -> setfragment(fragkom)
            }
            true
        }
        // bottom navigation fragment /\/\/\

        //Jumlah makanan di kategori fragment simpanan \/\/\/
        val fragmensimpan = layoutInflater.inflate(R.layout.fragment_simpanan, null)
        rv_list_jenisbahan = fragmensimpan.findViewById(R.id.rv_jenisbahan)
        rv_list_jenisbahan.setHasFixedSize(true)
        jumbahanmain = ArrayList()
        bahanarraylist = ArrayList<Datarv_jenisbahan>()
        getbahandata()
        //Jumlah makanan di kategori fragment simpanan /\/\/\
    }

    //bottom navigation fragment \/\/\/
    private fun setfragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_framelayout,fragment)
            commit()
        }
    // bottom navigation fragment /\/\/\
    fun toaddbahan(view: View) {
        startActivity(Intent(this, Tambahbahan_Activity::class.java))
        finish()
    }
    fun toseeitem(view: View) {
        startActivity(Intent(this, seeitems_Activity::class.java))
        finish()
    }

    //Jumlah makanan di kategori fragment simpanan \/\/\/
    @SuppressLint("SuspiciousIndentation")
    private fun getbahandata() {
        val currentuser = FirebaseAuth.getInstance().currentUser?.uid.toString()
        dbref = FirebaseDatabase.getInstance().getReference("/Users/$currentuser/Inventory")
        val dataTitle = resources.getStringArray(R.array.data_name)
        val dataimage = resources.obtainTypedArray(R.array.data_photo)
        for (i in dataTitle.indices) {
        dbquery = dbref.orderByChild("jenismakanan").equalTo(dataTitle[i])
        dbquery.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val count: String = snapshot.childrenCount.toString()
                println("countmain"+count)
                jumbahanmain.add(count)
                val bahanList = Datarv_jenisbahan(
                    dataTitle[i],
                    "",
                    dataimage.getResourceId(i, -1)
                )
                bahanList.description = "Kamu mempunyai $count macam"
                bahanarraylist.add(bahanList)
               println(bahanarraylist)


                println(jumbahanmain+"ini datanya cokkkk")
                println("done datachange")

            }
            override fun onCancelled(error: DatabaseError) {
                // Handle the error
            }
        })
        }
        showRecylerview()
    }

    fun showRecylerview(){
        rv_list_jenisbahan.layoutManager = LinearLayoutManager(this)
        rv_list_jenisbahan.adapter= adapter_jenisbahan(bahanarraylist)
    }
    fun getBahanArrayList(): ArrayList<Datarv_jenisbahan> {
        return bahanarraylist
    }
    //Jumlah makanan di kategori fragment simpanan /\/\/\
}