package com.exless.view

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.exless.R
import com.exless.`object`.datarv_bahan
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Calendar

class TambahbahanSimpan_Activity : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    @SuppressLint("MissingInflatedId", "SetTextI18n", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        // disable auto dark mode
        setContentView(R.layout.activity_tambahbahan)
        supportActionBar?.hide()

        database = FirebaseDatabase.getInstance().reference

        findViewById<Button>(R.id.bt_addseeitem).setOnClickListener {
            val name = findViewById<AutoCompleteTextView>(R.id.auto_namabahan).text.toString()
            val jenissim = findViewById<AutoCompleteTextView>(R.id.auto_jenispenyimpanan).text.toString()

            val currentuser = FirebaseAuth.getInstance().currentUser?.uid.toString()

            val itemRef = database.child("/Users/$currentuser/Inventory/$name$jenissim")
            itemRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        Toast.makeText(
                            this@TambahbahanSimpan_Activity,
                            "Bahan sudah ada di inventory",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val jenisbah = findViewById<AutoCompleteTextView>(R.id.auto_jenismakan).text.toString()
                        val tglkadal = findViewById<EditText>(R.id.et_tglkadal).text.toString()
                        val tglbeli = findViewById<EditText>(R.id.et_tglpembel).text.toString()
                        val jumlah = findViewById<EditText>(R.id.et_jumlah).text.toString()
                        val satuan = findViewById<AutoCompleteTextView>(R.id.auto_satuan).text.toString()

                        database.child("/Users/$currentuser/Inventory/$name$jenissim")
                            .setValue(datarv_bahan(name, jenisbah, tglbeli, tglkadal, "$jumlah $satuan", jenissim))
                        Toast.makeText(this@TambahbahanSimpan_Activity, "Bahan telah ditambahkan", Toast.LENGTH_SHORT).show()
//                        startActivity(Intent(this@TambahbahanSimpan_Activity, MainActivity::class.java))// ternyata ga perlu make hehe
                        finish()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        }

        // Adapter dropdown
        val items = resources.getStringArray(R.array.Jenis_makanan)
        val adapter = ArrayAdapter(this, R.layout.list_dropdown, items)
        val autocomplete = findViewById<AutoCompleteTextView>(R.id.auto_jenismakan)
        autocomplete.setAdapter(adapter)

        val items2 = resources.getStringArray(R.array.Jenis_penyimpanan)
        val adapter2 = ArrayAdapter(this, R.layout.list_dropdown, items2)
        val autocomplete2 = findViewById<AutoCompleteTextView>(R.id.auto_jenispenyimpanan)
        autocomplete2.setAdapter(adapter2)

        val items4 = resources.getStringArray(R.array.satuan)
        val adapter4 = ArrayAdapter(this, R.layout.list_dropdown, items4)
        val autocomplete4 = findViewById<AutoCompleteTextView>(R.id.auto_satuan)
        autocomplete4.setAdapter(adapter4)

        val items3 = resources.getStringArray(R.array.nama_bahan)
        val adapter3 = ArrayAdapter(this, R.layout.list_dropdown, items3)
        val autocomplete3 = findViewById<AutoCompleteTextView>(R.id.auto_namabahan)
        autocomplete3.setAdapter(adapter3)


        // Calendar picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        findViewById<ImageView>(R.id.iv_tglkadal).setOnClickListener {
            val dpd = DatePickerDialog(
                this,
                android.R.style.Theme_Holo_Dialog,
                DatePickerDialog.OnDateSetListener { view: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                    val formattedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
                    findViewById<EditText>(R.id.et_tglkadal).setText(formattedDate)
                }, year, month, day
            )
            dpd.show()
        }

        findViewById<ImageView>(R.id.iv_tglpembel).setOnClickListener {
            val dpd = DatePickerDialog(
                this,
                android.R.style.Theme_Holo_Dialog,
                DatePickerDialog.OnDateSetListener { view: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                    val formattedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
                    findViewById<EditText>(R.id.et_tglpembel).setText(formattedDate)
                }, year, month, day
            )
            dpd.show()
        }

        findViewById<ImageView>(R.id.back_tambahbahan).setOnClickListener {
            finish()
        }
    }
}
