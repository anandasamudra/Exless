package com.exless.view.adapter

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.exless.R

class adapter_seeexpired(
    private val listName:List<String>
)

    : RecyclerView.Adapter<adapter_seeexpired.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): adapter_seeexpired.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_expired,parent,false)
        )
    }

    override fun onBindViewHolder(holder: adapter_seeexpired.ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return listName.size
    }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val text = view.findViewById<TextView>(R.id.textView7)
    }

}

