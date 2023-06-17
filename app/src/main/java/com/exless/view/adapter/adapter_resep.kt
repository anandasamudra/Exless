package com.exless.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.exless.R
import com.exless.view.`object`.object_resep

class adapter_resep(private val DaftarResep: List<object_resep.ResepMakanan>)
    :RecyclerView.Adapter<adapter_resep.resepViewHolder>(){
    class resepViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val imageviewResep: ImageView = itemView.findViewById(R.id.resepmakan)
        val textviewDurasi: TextView = itemView.findViewById(R.id.durasi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): resepViewHolder {
        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.item_list_resep, parent, false)
        return resepViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return DaftarResep.size
    }

    override fun onBindViewHolder(holder: resepViewHolder, position: Int) {
        val resep = DaftarResep[position]
        holder.imageviewResep.setImageResource(resep.gambar)
        holder.textviewDurasi.text = resep.durasi_masak
    }

}