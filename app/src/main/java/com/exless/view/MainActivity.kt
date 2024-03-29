package com.exless.view



import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
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
import com.bumptech.glide.Glide
import com.exless.R
import com.exless.adapter.adapter_jenisbahan
import com.exless.adapter.adapter_seeexpiredsimpanan
import com.exless.fragment.fragmentbelanja
import com.exless.fragment.fragmenthome
import com.exless.fragment.fragmentkomunitas
import com.exless.fragment.fragmentloading
import com.exless.fragment.fragmentsimpanan
import com.exless.notification.AlarmReceiver
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
//get days
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import com.jakewharton.threetenabp.AndroidThreeTen
import java.time.format.DateTimeParseException
import java.util.Calendar

import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.widget.Toast
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
    private var doubleBackToExitPressedOnce = false
    //    private lateinit var profilehome : ImageView
    var i : Int = 0
    private var isDataFetched = false

    @SuppressLint("MissingInflatedId")
    private lateinit var firebaseAuth: FirebaseAuth
    @SuppressLint("MissingInflatedId", "UnspecifiedImmutableFlag")
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
// inisialisasi untuk hitung tanggal
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
//                        show(fragsim)
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


        //Jumlah makanan di kategori fragment simpanan /\/\/\
        //dapatkan bahan dari profile \/\/\/
        try {
            getphoto()
        }
        catch (e:Exception){
            println("Erornya adalah = \n\n"+e)
        }
//dapatkan bahan dari profile /\/\/\
        //alarm \/\/\/

        val desiredCalendar = Calendar.getInstance()
        desiredCalendar.set(Calendar.HOUR_OF_DAY, 8)
        desiredCalendar.set(Calendar.MINUTE, 0)
        desiredCalendar.set(Calendar.SECOND, 0)

        val currentCalendar = Calendar.getInstance()
        val currentTimeMillis = System.currentTimeMillis()

// cek udah lewat tanggalnya
        if (currentCalendar.before(desiredCalendar)) {
            // Create an intent for the AlarmReceiver
            val alarmIntent = Intent(this, AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent,
                PendingIntent.FLAG_MUTABLE)

            // Schedule the one-time alarm
            val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, desiredCalendar.timeInMillis, pendingIntent)
        } else {
            println("bakekok")
        }
        //alarm /\/\/\
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
    //button
    fun toprofile(view: View) {
        startActivity(Intent(this, Profile_Activity::class.java))
        finish()
    }
    fun tocomunity(view: View) {
        val url = "http://www.google.com"//link website kalau udah ada
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    fun toaddbahan(view: View) {
        startActivity(Intent(this, TambahbahanMain_Activity::class.java))
        finish()
    }
    fun toaddbahansimpan(view: View) {
        startActivity(Intent(this, TambahbahanSimpan_Activity::class.java))
        findViewById<BottomNavigationView>(R.id.bottomNavigationView_layout).selectedItemId =R.id.inventory

    }
    fun tosimpanan(view: View) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_framelayout2,fragmentsimpanan())
            show(fragmentsimpanan())
            hide(fragmenthome())
            commit()
        }
        findViewById<BottomNavigationView>(R.id.bottomNavigationView_layout).selectedItemId =R.id.inventory
    }

    fun toseeexpiredmain(view: View) {
        startActivity(Intent(this, SeeExpiredMainActivity::class.java))
        finish()
    }
    fun detailberita(view: View) {
        startActivity(Intent(this,DetailBeritaActivity::class.java))
        finish()
    }
    fun getphoto() {
        if (!isDataFetched) {
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
                                println("Lahhhhh\n\n\n\nsdfsdfsfs\n\n\ngdfgdfgfd")
                                isDataFetched = true
                                println(isDataFetched)

                            }
                        } else {

                        }

                    }


                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
            i++
            println("ini i upload\n\n\n\n\n" + i)
            println(isDataFetched)
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

                            // Compare today's date with the expired date
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
                            } catch (e: DateTimeParseException) {
                                tglkadal = "EXPIRED"
                            } catch (e: Exception) {
                                tglkadal = "EXPIRED"
                            }
                        }

                        else{
                            tglkadal ="-"
                        }
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
        val month = c.get(Calendar.MONTH) + 1
        val year = c.get(Calendar.YEAR)
        return String.format("%02d/%02d/%04d", day, month, year)
    }

    //shared preference untuk mencegah back ke login \/\/\/
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }
}
