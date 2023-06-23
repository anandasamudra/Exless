package com.exless.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.exless.R
import com.exless.model.model_resep

class adapter_resep(private val daftarResep: List<model_resep>)
    : RecyclerView.Adapter<adapter_resep.ResepViewHolder>() {
    class ResepViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imageViewResep: ImageView = itemView.findViewById(R.id.resepmakan)
        val textViewDurasi: TextView = itemView.findViewById(R.id.durasi)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResepViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_list_resep, parent, false)
        return ResepViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: ResepViewHolder, position: Int) {
        val resep = daftarResep[position]
        holder.textViewDurasi.text = resep.Durasi

        // Menggunakan library Glide untuk memuat gambar dari URL
        Glide.with(holder.itemView)
            .load(resep.Gambar)
            .into(holder.imageViewResep)
    }
    override fun getItemCount(): Int {
        return daftarResep.size
    }
}
