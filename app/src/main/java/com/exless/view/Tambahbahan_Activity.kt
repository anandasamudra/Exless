package com.exless.view

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import com.exless.R

class Tambahbahan_Activity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambahbahan)
        //
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
    }
}