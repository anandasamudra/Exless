import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.exless.R
import com.exless.`object`.Datarv_detailberita

class AdapterBerita(val array: ArrayList<Datarv_detailberita>) :
    RecyclerView.Adapter<AdapterBerita.viewholder_berita>() {

    inner class viewholder_berita(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.titlesumber)
        private val tvDesk: TextView = itemView.findViewById(R.id.descriptionsumber)
        private val imgView: ImageView = itemView.findViewById(R.id.detailberita1)
        private var newsLink: String? = null

        init {

            imgView.setOnClickListener {
                newsLink?.let { link ->
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                    itemView.context.startActivity(intent)
                }
            }
        }

        fun bind(data: Datarv_detailberita) {
            tvTitle.text = data.title
            tvDesk.text = data.description
            newsLink = data.newsUrl

            Glide.with(itemView)
                .load(data.imageUrl)
                .into(imgView)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder_berita {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_detail_berita, parent, false)
        return viewholder_berita(view)
    }

    override fun getItemCount(): Int {
        return array.size
    }

    override fun onBindViewHolder(holder: viewholder_berita, position: Int) {
        val currentItem = array[position]
        holder.bind(currentItem)
    }
}