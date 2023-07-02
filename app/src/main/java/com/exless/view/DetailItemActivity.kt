package com.exless.view

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowInsetsControllerCompat
import com.exless.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide


class DetailItemActivity : AppCompatActivity() {
    private lateinit var dbref : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_item)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)//disable auto darkmode
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = true
        }
        supportActionBar?.hide()
        val namabahan = intent.getStringExtra("nama_bahan")
        findViewById<TextView>(R.id.tv_detailtitle).setText(namabahan)
        getbahandata(namabahan.toString())

println(namabahan+"ini nama bahan")
        val imageName =namabahan!!.replace("\\s+".toRegex(), "").toLowerCase()
        val imageResId = resources.getIdentifier(imageName, "drawable", packageName)
        println(imageName)
        println(imageResId)
        if (imageResId != 0) {
            findViewById<ImageView>(R.id.img_detail).setImageResource(imageResId)
        } else {
            findViewById<ImageView>(R.id.img_detail).setImageResource(R.drawable.a1)
        }

    }

    fun backtohome(view: View) {
        finish()
    }

    private fun getbahandata(nama: String) {
        dbref = FirebaseDatabase.getInstance().getReference("/Food/$nama")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists() && nama != "") {
                    val detailItemsMap = HashMap<String, String>()
                    detailItemsMap["detailitem1"] = snapshot.child("1").getValue().toString()
                    detailItemsMap["detailitem2"] = snapshot.child("2").getValue().toString()
                    detailItemsMap["detailitem3"] = snapshot.child("3").getValue().toString()
                    detailItemsMap["detailitem4"] = snapshot.child("4").getValue().toString()
                    detailItemsMap["detailitem5"] = snapshot.child("5").getValue().toString()

                    val tvDetail1 = findViewById<TextView>(R.id.tv_detail1)
                    val tvDetail2 = findViewById<TextView>(R.id.tv_detail2)
                    val tvDetail3 = findViewById<TextView>(R.id.tv_detail3)
                    val tvDetail4 = findViewById<TextView>(R.id.tv_detail4)
                    val tvDetail5 = findViewById<TextView>(R.id.tv_detail5)

                    val one = SpannableStringBuilder(tvDetail1.text)
                    one.append(snapshot.child("1").getValue().toString())
                    one.setSpan(
                        StyleSpan(android.graphics.Typeface.BOLD),
                        0,
                        tvDetail1.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    val two = SpannableStringBuilder(tvDetail2.text)
                    two.append(snapshot.child("2").getValue().toString())
                    two.setSpan(
                        StyleSpan(android.graphics.Typeface.BOLD),
                        0,
                        tvDetail2.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )

                    val three = SpannableStringBuilder(tvDetail3.text)
                    three.append(snapshot.child("3").getValue().toString())
                    three.setSpan(
                        StyleSpan(android.graphics.Typeface.BOLD),
                        0,
                        tvDetail3.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )

                    val four = SpannableStringBuilder(tvDetail4.text)
                    four.append(snapshot.child("4").getValue().toString())
                    four.setSpan(
                        StyleSpan(android.graphics.Typeface.BOLD),
                        0,
                        tvDetail4.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )

                    val five = SpannableStringBuilder(tvDetail5.text)
                    five.append(snapshot.child("5").getValue().toString())
                    five.setSpan(
                        StyleSpan(android.graphics.Typeface.BOLD),
                        0,
                        tvDetail5.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    tvDetail1.text = one
                    tvDetail2.text = two
                    tvDetail3.text = three
                    tvDetail4.text = four
                    tvDetail5.text = five

                    findViewById<ConstraintLayout>(R.id.dt2).visibility = View.VISIBLE
                    findViewById<TextView>(R.id.dt3).visibility = View.VISIBLE
                    findViewById<ConstraintLayout>(R.id.dt4).visibility = View.VISIBLE
                    findViewById<ConstraintLayout>(R.id.dt6).visibility = View.GONE
                }
                else{

                    findViewById<ConstraintLayout>(R.id.dt2).visibility = View.GONE
                    findViewById<TextView>(R.id.dt3).visibility = View.GONE
                    findViewById<ConstraintLayout>(R.id.dt4).visibility = View.GONE
                    findViewById<ConstraintLayout>(R.id.dt6).visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}