package com.exless.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.exless.R
import com.exless.`object`.Datarv_detailberita


class AdapterDetailBerita(private val context: Context, mutableListOf: MutableList<Any>) :
    RecyclerView.Adapter<AdapterDetailBerita.ViewHolder>() {

    private val data: MutableList<Datarv_detailberita> = mutableListOf()

    // Inner class ViewHolder untuk merepresentasikan tampilan item dalam RecyclerView
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.detailberita1)
        val tanggalTextView: TextView = itemView.findViewById(R.id.tanggal)
    }

    // Method onCreateViewHolder untuk membuat ViewHolder baru
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_list_detail_berita, parent, false)
        return ViewHolder(view)
    }

    // Method onBindViewHolder untuk menghubungkan data dengan tampilan ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.imageView.setImageResource(item.imageView)
        holder.tanggalTextView.text = item.tanggal
    }

    // Method getItemCount untuk mengembalikan jumlah item dalam data
    override fun getItemCount(): Int {
        return data.size
    }

    // Fungsi untuk mengatur data baru ke adapter
    fun setData(newData: List<Datarv_detailberita>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }
}