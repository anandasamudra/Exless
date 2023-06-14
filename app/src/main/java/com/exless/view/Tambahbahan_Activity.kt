package com.exless.view

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.exless.R
import java.util.Calendar

class Tambahbahan_Activity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambahbahan)

        supportActionBar?.hide()
        //adapter dropdown \/\/\/
        val items = resources.getStringArray(R.array.Jenis_makanan)
        val adapter = ArrayAdapter(this, R.layout.list_dropdown, items)
        val autocomplete = findViewById<AutoCompleteTextView>(R.id.auto_jenismakan)
        autocomplete.setAdapter(adapter)
        //
        val items2 = resources.getStringArray(R.array.Jenis_penyimpanan)
        val adapter2 = ArrayAdapter(this, R.layout.list_dropdown, items2)
        val autocomplete2 = findViewById<AutoCompleteTextView>(R.id.auto_jenispenyimpanan)
        autocomplete2.setAdapter(adapter2)
        //
        val items3 = resources.getStringArray(R.array.nama_bahan)
        val adapter3 = ArrayAdapter(this, R.layout.list_dropdown, items3)
        val autocomplete3 = findViewById<AutoCompleteTextView>(R.id.auto_namabahan)
        autocomplete3.setAdapter(adapter3)
        //adapter dropdown /\/\/\

        //calendar picker \/\/\/
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        //
        findViewById<ImageView>(R.id.iv_tglkadal).setOnClickListener{
            val dpd = DatePickerDialog(
                this,
                android.R.style.Theme_Holo_Dialog,
                DatePickerDialog.OnDateSetListener{view: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                    findViewById<EditText>(R.id.et_tglkadal).setText("$dayOfMonth/$month/$year")
                }, year, month, day
            )
            dpd.show()
        }
        findViewById<ImageView>(R.id.iv_tglpembel).setOnClickListener{
            val dpd = DatePickerDialog(
                this,
                android.R.style.Theme_Holo_Dialog,
                DatePickerDialog.OnDateSetListener{view: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                    findViewById<EditText>(R.id.et_tglpembel).setText("$dayOfMonth/$month/$year")
                }, year, month, day
            )
            dpd.show()
        }
        //calendar picker /\/\/\

        findViewById<ImageView>(R.id.back_tambahbahan).setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }
        }


}