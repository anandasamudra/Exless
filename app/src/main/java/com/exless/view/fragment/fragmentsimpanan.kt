package com.exless.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exless.R
import com.exless.databinding.FragmentHomeBinding
import com.exless.view.Datarv_jenisbahan
import com.exless.view.adapter_jenisbahan


class fragmentsimpanan : Fragment() {
    private lateinit var rv_list_jenisbahan: RecyclerView
    private var jenisbahanarraylist = ArrayList<Datarv_jenisbahan>()
    private var binding: FragmentHomeBinding? =null
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_simpanan,container,false)

            rv_list_jenisbahan = view.findViewById(R.id.rv_jenisbahan)
            rv_list_jenisbahan.setHasFixedSize(true)
////
            jenisbahanarraylist.addAll(listbahanarray)
            showRecylerview()

        return view
        }



    private val listbahanarray: ArrayList<Datarv_jenisbahan>
        get() {
            val dataTitle = resources.getStringArray(R.array.data_name)
            val datadesk = resources.getStringArray(R.array.data_description)
            val dataimage = resources.obtainTypedArray(R.array.data_photo)
            val datalist = ArrayList<Datarv_jenisbahan>()

            for (i in dataTitle.indices){
                val bahanlist = Datarv_jenisbahan(
                    dataTitle[i],
                    datadesk[i],
                    dataimage.getResourceId(i, -1)
                )
                datalist.add(bahanlist)
            }
            return datalist
        }
    fun showRecylerview(){
        rv_list_jenisbahan.layoutManager = LinearLayoutManager(requireContext())
        rv_list_jenisbahan.adapter= adapter_jenisbahan(jenisbahanarraylist)
    }
}