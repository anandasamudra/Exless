package com.exless.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exless.R
import com.exless.databinding.FragmentHomeBinding
import com.exless.view.Datarv_jenisbahan
import com.exless.view.Datarv_seeexperired
import com.exless.view.MainActivity
import com.exless.view.SeeExpiredActivity
import com.exless.view.adapter_bahan
import com.exless.view.adapter_jenisbahan
import com.exless.view.adapter_seeexpiredsimpanan
import com.exless.view.datarv_bahan
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener


class fragmentsimpanan : Fragment() {
    private lateinit var dbref : DatabaseReference
    private lateinit var dbquery : Query
    private lateinit var rv_list_jenisbahan: RecyclerView
    private var jenisbahanarraylist = ArrayList<Datarv_jenisbahan>()
    private var binding: FragmentHomeBinding? =null
    //
    private lateinit var dbrefex : DatabaseReference
    private lateinit var dbqueryex : Query
    private lateinit var rv_list_jenisbahanex: RecyclerView
    private var jenisbahanarraylistex = ArrayList<Datarv_seeexperired>()
    private var dataList: ArrayList<Datarv_seeexperired>? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_simpanan,container,false)//inflater fragment
// Recylerview jenis/kategori bahan \/\/\/
        rv_list_jenisbahan = view.findViewById(R.id.rv_jenisbahan)
        rv_list_jenisbahan.setHasFixedSize(true)
        val mainActivity = activity as? MainActivity
        val bahanarraylist = mainActivity?.getBahanArrayList()
        jenisbahanarraylist.clear()
        jenisbahanarraylist.addAll(bahanarraylist!!)
        showRecylerview()
        //
        rv_list_jenisbahanex = view.findViewById(R.id.rv_seeexpired)
        rv_list_jenisbahanex.setHasFixedSize(true)
        val bahanarraylistex = mainActivity?.getBahanArrayListex()
        jenisbahanarraylistex.clear()
        jenisbahanarraylistex.addAll(bahanarraylistex!!)
        println(jenisbahanarraylistex)
        showRecylerviewex()
//         Recylerview jenis/kategori bahan /\/\/\
        return view// harus paling bawah(?)
    }

    fun showRecylerview(){
        rv_list_jenisbahan.layoutManager = LinearLayoutManager(requireContext())
        rv_list_jenisbahan.adapter= adapter_jenisbahan(jenisbahanarraylist)
    }
    fun showRecylerviewex(){
        rv_list_jenisbahanex.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        rv_list_jenisbahanex.adapter= adapter_seeexpiredsimpanan(jenisbahanarraylistex)
    }
// Recylerview jenis/kategori bahan /\/\/\

}