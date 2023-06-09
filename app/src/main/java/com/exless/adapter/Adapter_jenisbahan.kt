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
import com.exless.`object`.Datarv_jenisbahan
import com.exless.view.seeitems_Activity

class adapter_jenisbahan(val array: ArrayList<Datarv_jenisbahan>) : RecyclerView.Adapter<adapter_jenisbahan.viewholder_jenisbahan>() {
    class viewholder_jenisbahan(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tv_titlejenis)
        val tvdesk: TextView = itemView.findViewById(R.id.tv_deskjenis)
        val imgview: ImageView = itemView.findViewById(R.id.gambarseeitems)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder_jenisbahan {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_seeitems, parent, false)
        return viewholder_jenisbahan(view)
    }

    override fun getItemCount(): Int {
        return array.size
    }

    override fun onBindViewHolder(holder: viewholder_jenisbahan, position: Int) {
        val currentItem = array[position]
        val (title, description, imageView) = array[position]
        holder.tvTitle.text = title
        holder.tvdesk.text = description
        //holder use glide to fix different image size issue and lag
        Glide.with(holder.itemView.context)
            .load(currentItem.imageView)
            .into(holder.imgview)

        holder.itemView.setOnClickListener {
            // Handle item click here
            val context = holder.itemView.context
            val intent = Intent(context, seeitems_Activity::class.java)
            // Pass any necessary data to the new activity if needed
            intent.putExtra("nama_bahan", title)
            context.startActivity(intent)
        }
    }
}
