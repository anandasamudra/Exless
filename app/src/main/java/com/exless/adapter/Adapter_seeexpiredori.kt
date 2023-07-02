package com.exless.adapter

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
import com.exless.`object`.Datarv_seeexperired
import java.util.Random



class adapter_seeexpiredori(val array: ArrayList<Datarv_seeexperired>) : RecyclerView.Adapter<adapter_seeexpiredori.viewholder_jenisbahan>() {
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
            holder.imgview.setImageResource(randomimage())
        }

    }
    fun randomimage (): Int {
        var random = Random().nextInt(5)+1
        val img1 = R.drawable.a1
        val img2 = R.drawable.a2
        val img3 = R.drawable.a3
        val img4 = R.drawable.a4
        val img5 = R.drawable.a5
        if (random ==1){
            random = img1
        }
        if (random ==2){
            random = img2
        }
        if (random ==3){
            random = img3
        }
        if (random ==4){
            random = img4
        }
        if (random ==5){
            random = img5
        }
        return random
    }
}
