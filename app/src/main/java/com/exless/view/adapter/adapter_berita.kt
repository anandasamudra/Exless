package com.exless.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.exless.R
import com.exless.view.`object`.object_berita

class adapter_berita(private val DaftarBerita: List<object_berita.ListBerita>)
    :RecyclerView.Adapter<adapter_berita.beritaViewHolder>(){
        class beritaViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            val imageviewBerita: ImageView = itemView.findViewById(R.id.berita)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): beritaViewHolder {
        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.item_list_berita, parent, false)
        return beritaViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return DaftarBerita.size
    }

    override fun onBindViewHolder(holder: beritaViewHolder, position: Int) {
        val berita = DaftarBerita[position]
        holder.imageviewBerita.setImageResource(berita.gambar)
    }
}