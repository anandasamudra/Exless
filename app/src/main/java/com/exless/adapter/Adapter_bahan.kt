package com.exless.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.exless.R
import com.exless.`object`.datarv_bahan
import com.exless.view.DetailItemActivity
import com.exless.view.seeitems_Activity

class adapter_bahan(val bahanlist : ArrayList<datarv_bahan>) : RecyclerView.Adapter<adapter_bahan.viewholder_bahan>() {
    class viewholder_bahan(itemView: View) : RecyclerView.ViewHolder(itemView){
        val namabahan : TextView = itemView.findViewById(R.id.tv_namaitemseeitems)
        val jenisbahan : TextView = itemView.findViewById(R.id.tv_labeljenisseeitem)
        val pembelian : TextView = itemView.findViewById(R.id.tv_tanggalseeitems)
        val jumlah : TextView = itemView.findViewById(R.id.tv_pcsseeitems)
        val jenissimpan : TextView = itemView.findViewById(R.id.tv_labeltempatseeitems)
        val kadaluarsa : TextView = itemView.findViewById(R.id.tv_labelkadaluarsaseeitems)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder_bahan {
        val itemvieww = LayoutInflater.from(parent.context).inflate(R.layout.adapter_seeitems, parent, false)
    return viewholder_bahan(itemvieww)
    }

    override fun getItemCount(): Int {

        return bahanlist.size
    }

    override fun onBindViewHolder(holder: viewholder_bahan, position: Int) {
        val currentitem = bahanlist[position]

        holder.namabahan.text = currentitem.nama
        holder.jenisbahan.text = currentitem.jenismakanan
        holder.pembelian.text = currentitem.tglbeli
        holder.jenissimpan.text = currentitem.jenissimpan
        holder.kadaluarsa.text = currentitem.tglkadaluarsa
        holder.jumlah.text = currentitem.jumlah
        holder.itemView.setOnClickListener {
            // Handle item click here
            val context = holder.itemView.context
            val intent = Intent(context, DetailItemActivity::class.java)
            // Pass any necessary data to the new activity if needed
            intent.putExtra("nama_bahan", currentitem.nama)
            context.startActivity(intent)
        }
    }


}