package com.exless.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.exless.R
import com.exless.model.model_detail_menu_resep
import com.exless.view.detail_menu_resep

class adapter_detail_menu_resep(private val menuResepList: List<model_detail_menu_resep>):
RecyclerView.Adapter<adapter_detail_menu_resep.MenuBahanMasakanViewHolder>(){

    inner class MenuBahanMasakanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val namaTextView: TextView = itemView.findViewById(R.id.nama_bahan)
        internal val jumlahTextView: TextView = itemView.findViewById(R.id.jumlah_diperlukan)
        internal val gambarImageView: ImageView = itemView.findViewById(R.id.gambar_bahan)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuBahanMasakanViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_list_bahan_menu_resep, parent, false)
        return MenuBahanMasakanViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return menuResepList.size
    }

    override fun onBindViewHolder(holder: MenuBahanMasakanViewHolder, position: Int) {
        val menuResep = menuResepList[position]
        holder.namaTextView.text = menuResep.NamaBahan
        holder.jumlahTextView.text = menuResep.Jumlah
        Glide.with(holder.itemView)
            .load(menuResep.GambarBahan)
            .into(holder.gambarImageView)

    }
}