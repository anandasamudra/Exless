package com.exless.view

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.exless.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.jakewharton.threetenabp.AndroidThreeTen


class adapter_seeexpiredsimpanan(val array: ArrayList<Datarv_seeexperired>) : RecyclerView.Adapter<adapter_seeexpiredsimpanan.viewholder_jenisbahan>() {
    class viewholder_jenisbahan(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.title_expired)
        val tvtgl: TextView = itemView.findViewById(R.id.tglkadalexpired)
        val tvjumlah: TextView = itemView.findViewById(R.id.jumlahexpired)
        val imgview: ImageView = itemView.findViewById(R.id.gambarexpired)
        val expiredStatus: LinearLayout = itemView.findViewById(R.id.expiredstatus)

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
        val (title, tglkadaluarsa, jumlahsimpanan, imageView) = array[position]
        holder.tvTitle.text = title
        holder.tvtgl.text = tglkadaluarsa
        holder.tvjumlah.text = jumlahsimpanan


        if (tglkadaluarsa == "EXPIRED") {
            // Change background color to red for items with the text "expired"
            holder.expiredStatus.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.red))
        } else {
            // Change background color to normal for other items
            holder.expiredStatus.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.graylight))
        }
        // Set the image using the file name
        val imageName = title.replace("\\s+".toRegex(), "").toLowerCase() // Remove spaces and convert to lowercase
        val imageResId = holder.itemView.context.resources.getIdentifier(imageName, "drawable", holder.itemView.context.packageName)
        if (imageResId != 0) {
            Glide.with(holder.itemView.context)
                .load(imageResId)
                .into(holder.imgview)
        } else {
            //default  image
            holder.imgview.setImageResource(R.drawable.kacang)
        }

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, SeeExpiredActivity::class.java)
//            intent.putExtra("nama_bahan", title)
            context.startActivity(intent)
        }
    }
}
