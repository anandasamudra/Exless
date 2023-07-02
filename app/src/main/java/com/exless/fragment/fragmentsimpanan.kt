package com.exless.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exless.R
import com.exless.databinding.FragmentHomeBinding
import com.exless.`object`.Datarv_jenisbahan
import com.exless.`object`.Datarv_seeexperired
import com.exless.view.MainActivity
import com.exless.adapter.adapter_jenisbahan
import com.exless.adapter.adapter_seeexpiredsimpanan
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query


class fragmentsimpanan : Fragment() {
    private lateinit var dbref : DatabaseReference
    private lateinit var dbquery : Query
    private lateinit var rv_list_jenisbahan: RecyclerView
    private lateinit var rv_seeexpired : RecyclerView
    private var jenisbahanarraylist = ArrayList<Datarv_jenisbahan>()
    private var binding: FragmentHomeBinding? =null
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
        rv_seeexpired = view.findViewById(R.id.rv_seeexpired)
        rv_seeexpired.setHasFixedSize(true)
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
        if (bahanarraylistex.isEmpty()){
            println("bahan expired === $bahanarraylist")
            view.findViewById<RecyclerView>(R.id.rv_seeexpired).visibility = View.GONE
        }
        else{
            println("bahan expired === $bahanarraylist")
            view.findViewById<RecyclerView>(R.id.rv_seeexpired).visibility = View.VISIBLE
        }

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