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
import com.exless.fragment.fragmentsimpanan
import com.exless.notification.AlarmReceiver
import com.exless.`object`.Datarv_jenisbahan
import com.exless.`object`.Datarv_seeexperired
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
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
// Initialize ThreeTenABP
        AndroidThreeTen.init(this)
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
        //
        val fragmensimpanex = layoutInflater.inflate(R.layout.fragment_simpanan, null)
        rv_list_jenisbahanex = fragmensimpanex.findViewById(R.id.rv_seeexpired)
        rv_list_jenisbahanex.setHasFixedSize(true)
        jumbahanmainex = ArrayList()
        bahanarraylistex = ArrayList<Datarv_seeexperired>()
        getbahandataex()
//        getphoto()
        // Retrieve the image URL from the intent
//        profilehome = findViewById(R.id.img_userphotohome)

        try {
//            var imageUrl = intent.getIntExtra("imageUrl",0)
//
//            println("ini inisiasi\n\n\n\n"+imageUrl)
//            if (imageUrl == 1){
//                getphoto {
//                    imageUrl =0
//                    println("ini setelahnya ="+imageUrl)
//                }
//
//            }
getphoto()
//
//// Load the image using Glide or any other image loading library
//            if (imageUrl == null){
////            Glide.with(this@MainActivity)
////                .load("https://firebasestorage.googleapis.com/v0/b/exless-455f4.appspot.com/o/images%2F9e0f5391-8809-4e8f-9ee6-407a25191438?alt=media&token=3fb11ed0-00fc-4eff-9ba4-2e4e46e1e2c4")
////                .into(findViewById(R.id.img_userphotohome))
////println(imageUrl)
//            }
//            if (imageUrl != null){
////            Glide.with(this)
////                .load(imageUrl)
////                .into(findViewById(R.id.img_userphotohome))
////            println("haii"+imageUrl)
//            }
//            Glide.with(this)
//                .load("https://example.com/image.jpg") // Replace with your image URL
//                .into(findViewById(R.id.img_userphotohome))
//getphoto()
            println("ini I\n\n\n\n"+i)
        }
        catch (e:Exception){
            println("Erornya adalah = \n\n"+e)
        }

        //Jumlah makanan di kategori fragment simpanan /\/\/\

        //alarm
        val desiredCalendar = Calendar.getInstance()
        desiredCalendar.set(Calendar.HOUR_OF_DAY, 5)
        desiredCalendar.set(Calendar.MINUTE, 25)
        desiredCalendar.set(Calendar.SECOND, 0)

        val currentCalendar = Calendar.getInstance()
        val currentTimeMillis = System.currentTimeMillis()

// Check if the desired date and time have already passed
        if (currentCalendar.before(desiredCalendar)) {
            // Create an intent for the AlarmReceiver
            val alarmIntent = Intent(this, AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0)

            // Schedule the one-time alarm
            val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, desiredCalendar.timeInMillis, pendingIntent)
        } else {
            println("bakekok")
        }
    }

    //bottom navigation fragment \/\/\/
    private fun setfragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_framelayout,fragment)
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
    fun toseeitem(view: View) {
        startActivity(Intent(this, seeitems_Activity::class.java))
        finish()
    }
    fun tosimpanan(view: View) {
        setfragment(fragmentsimpanan())
        findViewById<BottomNavigationView>(R.id.bottomNavigationView_layout).selectedItemId =R.id.inventory
    }
    fun toseeexpired(view: View) {
        startActivity(Intent(this, SeeExpiredActivity::class.java))
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
                                println("Lahhhhh\n\n\n\nsdfsdfsfs\n\n\ngdfgdfgfd")
                                isDataFetched = true
                                println(isDataFetched)

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

        // Clear the existing data
        jumbahanmain.clear()
        bahanarraylist.clear()

        for (i in dataTitle.indices) {
            dbquery = dbref.orderByChild("jenismakanan").equalTo(dataTitle[i])
            dbquery.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val count: String = snapshot.childrenCount.toString()
                    jumbahanmain.add(count)
                    val bahanList = Datarv_jenisbahan(
                        dataTitle[i],
                        "",
                        dataimage.getResourceId(i, -1)
                    )
                    bahanList.description = "Kamu mempunyai $count macam"
                    bahanarraylist.add(bahanList)

                    // Update the RecyclerView adapter
                    rv_list_jenisbahan.adapter?.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle the error
                }
            })

            // Listen for child removed event
            dbquery.addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    // Not used
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    // Not used
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    // Child removed from the database, update the RecyclerView
                    getbahandata()
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    // Not used
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
        dbrefex.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Clear the existing data
                bahanarraylistex.clear()

                if (snapshot.exists()) {
                    for (bahanSnapshot in snapshot.children) {
                        val namabahan = bahanSnapshot.child("nama").getValue().toString()
                        var tglkadal: String
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
                        } else {
                            tglkadal = "-"
                        }

                        val jumlah = bahanSnapshot.child("jumlah").getValue().toString()
                        val bahanList = Datarv_seeexperired(
                            namabahan,
                            tglkadal,
                            jumlah,
                            0
                        )
                        bahanarraylistex.add(bahanList)
                    }
                } else {
                    // Handle the case when there are no ingredients
                }

                // Update the RecyclerView adapter
                rv_list_jenisbahanex.adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error
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