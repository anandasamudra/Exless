package com.exless.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.exless.R

class adapter_seeexpiredsimpanan(val array: ArrayList<Datarv_seeexperired>) : RecyclerView.Adapter<adapter_seeexpiredsimpanan.viewholder_jenisbahan>() {
    class viewholder_jenisbahan(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.title_expired)
        val tvtgl: TextView = itemView.findViewById(R.id.tglkadalexpired)
        val tvjumlah: TextView = itemView.findViewById(R.id.jumlahexpired)
        val imgview: ImageView = itemView.findViewById(R.id.gambarexpired)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder_jenisbahan {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_expiredcopy, parent, false)
        return viewholder_jenisbahan(view)
    }

    override fun getItemCount(): Int {
        return array.size
    }

    override fun onBindViewHolder(holder: viewholder_jenisbahan, position: Int) {
        val currentItem = array[position]
        val (title,tglkadaluarsa,jumlahsimpanan,imageView) = array[position]
        holder.tvTitle.text = title
        holder.tvtgl.text = tglkadaluarsa
        holder.tvjumlah.text = jumlahsimpanan
        //holder use glide to fix different image size issue and lag
        Glide.with(holder.itemView.context)
            .load(currentItem.imageView)
            .into(holder.imgview)
    }
}
