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
import com.exless.model.model_menu_resep
import com.exless.view.detail_menu_resep

class adapter_menu_resep(private val menuResepList: List<model_menu_resep>) :
    RecyclerView.Adapter<adapter_menu_resep.MenuResepViewHolder>() {

    inner class MenuResepViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val gambarImageView: ImageView = itemView.findViewById(R.id.gambarmenuresepmakan)
        internal val namaTextView: TextView = itemView.findViewById(R.id.nama_menu_resep_makanan)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuResepViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_list_menu_resep, parent, false)
        return MenuResepViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MenuResepViewHolder, position: Int) {
        val menuResep = menuResepList[position]
        holder.namaTextView.text = menuResep.Nama
        Glide.with(holder.itemView)
            .load(menuResep.Gambar)
            .into(holder.gambarImageView)

        //memanggil detail_menu_resep.kt
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, detail_menu_resep::class.java)
            intent.putExtra("key", menuResep.key)
            intent.putExtra("Nama", menuResep.Nama)
            intent.putExtra("Gambar", menuResep.Gambar)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return menuResepList.size
    }
}
