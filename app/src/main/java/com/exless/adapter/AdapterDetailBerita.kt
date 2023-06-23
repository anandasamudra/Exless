import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.exless.R
import com.exless.`object`.Datarv_detailberita

class AdapterDetailBerita(val array: ArrayList<Datarv_detailberita>) : RecyclerView.Adapter<AdapterDetailBerita.viewholder_detailberita>() {

    class viewholder_detailberita(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.titlesumber)
        val tvdesk: TextView = itemView.findViewById(R.id.descriptionsumber)
        val imgview: ImageView = itemView.findViewById(R.id.detailberita1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder_detailberita {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_detail_berita, parent, false)
        return viewholder_detailberita(view)
    }

    override fun getItemCount(): Int {
        return array.size
    }

    override fun onBindViewHolder(holder: viewholder_detailberita, position: Int) {
        val currentItem = array[position]
        val (title, description, imageUrl) = currentItem

        holder.tvTitle.text = title
        holder.tvdesk.text = description

        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .into(holder.imgview)
    }
}