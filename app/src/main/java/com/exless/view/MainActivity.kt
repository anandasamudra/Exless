package com.exless.view



import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.exless.R
import com.exless.adapter.adapter_jenisbahan
import com.exless.adapter.adapter_seeexpiredsimpanan
import com.exless.fragment.fragmentbelanja
import com.exless.fragment.fragmenthome
import com.exless.fragment.fragmentkomunitas
import com.exless.fragment.fragmentloading
import com.exless.fragment.fragmentsimpanan
import com.exless.`object`.Datarv_jenisbahan
import com.exless.`object`.Datarv_seeexperired
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
//get days
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import com.jakewharton.threetenabp.AndroidThreeTen
import java.time.format.DateTimeParseException
import java.util.Calendar

import android.animation.ObjectAnimator
import android.os.Handler
import android.view.animation.LinearInterpolator
import com.exless.fragment.fragmentempty


class MainActivity : AppCompatActivity() {
    private lateinit var dbref : DatabaseReference
    private lateinit var dbquery : Query
    lateinit var jumbahanmain : ArrayList<String>
     lateinit var bahanarraylist : ArrayList<Datarv_jenisbahan>
    private lateinit var rv_list_jenisbahan: RecyclerView
    //
    private lateinit var dbrefex : DatabaseReference
    private lateinit var dbqueryex : Query
    lateinit var jumbahanmainex : ArrayList<String>
    lateinit var bahanarraylistex : ArrayList<Datarv_seeexperired>
    private lateinit var rv_list_jenisbahanex: RecyclerView
//    private lateinit var profilehome : ImageView
    var i : Int = 0
    private var isDataFetched = false

    @SuppressLint("MissingInflatedId")
    private lateinit var firebaseAuth: FirebaseAuth
    @SuppressLint("MissingInflatedId")
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
// Initialize ThreeTenABP
        AndroidThreeTen.init(this)
//bottom navigation fragment \/\/\/
        val fraghome = fragmenthome()
        val fragbel = fragmentbelanja()
        val fragsim = fragmentsimpanan()
        val fragkom = fragmentkomunitas()
        val fragload = fragmentloading()
        val fragtrans = fragmentempty()
        val desiredFragmentTag = intent.getStringExtra("fragment")

        setfragmentinisiasi(fraghome)//waasda
        if (desiredFragmentTag == "inventory") {//safe loading to simpanan
            setfragment2(fragload)
            supportFragmentManager.beginTransaction().apply {
                hide(fraghome)
                commit()
            }
            Handler().postDelayed({
                // Change to another fragment here
                println("dari inven")
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragment_framelayout2,fragsim)
                    hide(fraghome)
                    hide(fragload)//
                    commit()
                    findViewById<BottomNavigationView>(R.id.bottomNavigationView_layout).selectedItemId =R.id.inventory
                }
            }, 200) // Adjust the delay time as needed

        }
        else{
            println("ini mulaiiiiiasdklasdaslkdh alsdfhads kfsadjfgas")
            setfragment2(fragtrans)
        }


        findViewById<BottomNavigationView>(R.id.bottomNavigationView_layout).setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    supportFragmentManager.beginTransaction().apply {
                        show(fraghome)
                        replace(R.id.fragment_framelayout2,fragtrans)
                        commit()
                    }
                    true
                }
                R.id.inventory -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragment_framelayout2,fragsim)
                        show(fragsim)
                        hide(fraghome)
                        commit()
                    }
                    true
                }
                R.id.shop -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragment_framelayout2,fragbel)
                        hide(fraghome)
                        commit()
                    }
                    true
                }
                R.id.comunity -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragment_framelayout2,fragkom)
                        hide(fraghome)
                        commit()
                    }
                    true
                }
                else -> false
            }
        }
//        val rotationAnimator = ObjectAnimator.ofFloat(findViewById(R.id.img_loading), "rotation", 0f, 180f)
//        rotationAnimator.duration = 100000
//        rotationAnimator.interpolator = LinearInterpolator()
//
//        // Start the rotation animation
//        rotationAnimator.start()
// bottom navigation fragment /\/\/\

        //Jumlah makanan di kategori fragment simpanan \/\/\/
        val fragmensimpan = layoutInflater.inflate(R.layout.fragment_simpanan, null)
        rv_list_jenisbahan = fragmensimpan.findViewById(R.id.rv_jenisbahan)
        rv_list_jenisbahan.setHasFixedSize(true)
        jumbahanmain = ArrayList()
        bahanarraylist = ArrayList<Datarv_jenisbahan>()
        getbahandata()
        //
        val fragmensimpanex = layoutInflater.inflate(R.layout.fragment_simpanan, null)
        rv_list_jenisbahanex = fragmensimpanex.findViewById(R.id.rv_seeexpired)
        rv_list_jenisbahanex.setHasFixedSize(true)
        jumbahanmainex = ArrayList()
        bahanarraylistex = ArrayList<Datarv_seeexperired>()
        getbahandataex()

        try {
        getphoto()
        }
        catch (e:Exception){
        }

        //Jumlah makanan di kategori fragment simpanan /\/\/\
    }

    //bottom navigation fragment \/\/\/
    private fun setfragmentinisiasi(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_framelayout,fragment)
            commit()
            println("setfragmen inisiasi")
        }


    private fun setfragment2(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_framelayout2,fragment)

            commit()
        }

    // bottom navigation fragment /\/\/\
    fun toprofile(view: View) {
        startActivity(Intent(this, Profile_Activity::class.java))
        finish()
    }
    fun toaddbahan(view: View) {
        startActivity(Intent(this, TambahbahanMain_Activity::class.java))
        finish()
    }
    fun toaddbahansimpan(view: View) {
        startActivity(Intent(this, TambahbahanSimpan_Activity::class.java))
        findViewById<BottomNavigationView>(R.id.bottomNavigationView_layout).selectedItemId =R.id.inventory

    }
//    fun toseeitem(view: View) {ga di pake
//        startActivity(Intent(this, seeitems_Activity::class.java))
//        finish()
//    }
    fun tosimpanan(view: View) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_framelayout2,fragmentsimpanan())
            show(fragmentsimpanan())
            hide(fragmenthome())
            commit()
        }
        findViewById<BottomNavigationView>(R.id.bottomNavigationView_layout).selectedItemId =R.id.inventory
    }
    fun toseeexpired(view: View) {
        startActivity(Intent(this, SeeExpiredActivity::class.java))
        finish()
    }
    fun toseeexpiredmain(view: View) {
        startActivity(Intent(this, SeeExpiredMainActivity::class.java))
        finish()
    }
    fun detailberita(view: View) {
        startActivity(Intent(this,DetailBeritaActivity::class.java))
        finish()
    }
    interface PhotoUploadCallback {
        fun onPhotoUploadComplete()
    }
    fun getphoto() {
        if (!isDataFetched) {
            // Set the flag to true to indicate that the function has been called

            val currentuser = FirebaseAuth.getInstance().currentUser?.uid.toString()
            dbref = FirebaseDatabase.getInstance().getReference("/Users/$currentuser/photos")
            dbref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.child("imageUrl").getValue() != null) {
                        val imageUrl = snapshot.child("imageUrl").getValue(String::class.java)

                        if (!imageUrl.isNullOrEmpty()) {
                            if (!isDataFetched) {
                                Glide.with(this@MainActivity)
                                    .load(imageUrl)
                                    .into(findViewById(R.id.img_userphotohome))

                                isDataFetched = true


                            }
                        } else {
                            // Handle the case when the image URL is not available
                        }

                        // Notify the listener that the upload is complete
//                        callback() // Call the callback function
                    }


                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle the database error
//                    callback()
                }
            })
            i++


        }

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
//                println("countmain"+count)
                jumbahanmain.add(count)
                val bahanList = Datarv_jenisbahan(
                    dataTitle[i],
                    "",
                    dataimage.getResourceId(i, -1)
                )
                bahanList.description = "Kamu mempunyai $count macam"
                bahanarraylist.add(bahanList)

            }
            override fun onCancelled(error: DatabaseError) {
                // Handle the error
            }
        })
        }
        showRecylerview()
    }///////////////////////////////////////////////////////
    @SuppressLint("SuspiciousIndentation")
    private fun getbahandataex() {
        val currentuser = FirebaseAuth.getInstance().currentUser?.uid.toString()
        dbrefex = FirebaseDatabase.getInstance().getReference("/Users/$currentuser/Inventory")
        dbrefex.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (bahanSnapshot in snapshot.children){
                        val namabahan = bahanSnapshot.child("nama").getValue().toString()
                        //compare today date with expired date \/\/\/
                        var tglkadal : String
                        if (bahanSnapshot.child("tglkadaluarsa").getValue().toString() != "") {
                            tglkadal = bahanSnapshot.child("tglkadaluarsa").getValue().toString()

                            try {
                                val startDateStr = getCurrentDate()
                                val endDateStr = tglkadal
                                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                                val startDate = LocalDate.parse(startDateStr, formatter)
                                val endDate = LocalDate.parse(endDateStr, formatter)

                                if (endDate.isBefore(startDate)) {
                                    tglkadal = "EXPIRED"
                                } else {
                                    val days = ChronoUnit.DAYS.between(startDate, endDate)
                                    tglkadal = days.toString() + " hari"
                                }

//                                println("Number of days: $tglkadal")
                            } catch (e: DateTimeParseException) {
                                // Handle the case when parsing the dates fails
                                tglkadal = "EXPIRED"
//                                println("Invalid date format")
                            } catch (e: Exception) {
                                // Handle any other exceptions that might occur
                                tglkadal = "EXPIRED"
//                                println("An error occurred: ${e.message}")
                            }
                        }

                        else{
                            tglkadal ="-"
                        }
//                        println("Number of dayssadasda f: $tglkadal")
                        //compare today date with expired date /\/\/\
                        val jumlah = bahanSnapshot.child("jumlah").getValue().toString()
                        val bahanList = Datarv_seeexperired(
                            namabahan,
                            tglkadal,
                            jumlah,
                            0
                        )
                        bahanarraylistex.add(bahanList)

                    }
//                    bahanrecylerview.adapter = adapter_bahan(bahanarraylist)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        showRecylerviewex()
    }
    fun showRecylerview(){
        rv_list_jenisbahan.layoutManager = LinearLayoutManager(this)
        rv_list_jenisbahan.adapter= adapter_jenisbahan(bahanarraylist)
    }
    fun showRecylerviewex(){
        rv_list_jenisbahanex.layoutManager = LinearLayoutManager(this)
        rv_list_jenisbahanex.adapter= adapter_seeexpiredsimpanan(bahanarraylistex)
    }
    fun getBahanArrayList(): ArrayList<Datarv_jenisbahan> {
        return bahanarraylist
    }
    fun getBahanArrayListex(): ArrayList<Datarv_seeexperired> {
        return bahanarraylistex
    }
    //Jumlah makanan di kategori fragment simpanan /\/\/\
    private fun getCurrentDate(): String {
        val c = Calendar.getInstance()
        val day = c.get(Calendar.DAY_OF_MONTH)
        val month = c.get(Calendar.MONTH) + 1 // Months are zero-based, so add 1
        val year = c.get(Calendar.YEAR)
        return String.format("%02d/%02d/%04d", day, month, year)
    }

    //shared preference untuk mencegah back ke login \/\/\/
    override fun onBackPressed() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        startActivity(intent)
    }
}