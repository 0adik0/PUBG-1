package battle.royale.bestapps.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import battle.royale.bestapps.InfoActivity
import battle.royale.bestapps.R
import battle.royale.bestapps.models.Subcategory
import com.bumptech.glide.Glide
import java.util.ArrayList

class SubcategoryAdapter(val recyclerView: RecyclerView?, val list: ArrayList<Subcategory>, val context: Context) :
        RecyclerView.Adapter<SubcategoryAdapter.SubcategoryViewHolder>() {
    var activeItem: Int = 0
    var viewGroup: ViewGroup? = null
    var used = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubcategoryViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.subcategory_item, parent, false)
        viewGroup = parent
        return SubcategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubcategoryViewHolder, position: Int) {
        val data = list[position]

        holder.title.text = if (list[position].title.contains(".png")) {
            list[position].title.substringBefore(".png")
        } else {
            list[position].title.substringBefore(".jpg")
        }
        Glide.with(context).load(data.photo).into(holder.photo)
        holder.root.setOnClickListener {
            val intent = Intent(context, InfoActivity::class.java)
            if (list[position].title.contains(".png")) {
                intent.putExtra("title", list[position].title.substringBefore(".png"))
            } else {
                intent.putExtra("title", list[position].title.substringBefore(".jpg"))
            }

            intent.putExtra("photo", list[position].photo)
            intent.putExtra("link", list[position].link)
            context.startActivity(intent)
        }
    }



    override fun getItemCount(): Int {
        return list.size
    }



    class SubcategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root = view
        var title: TextView = view.findViewById(R.id.title)
        var photo: ImageView = view.findViewById(R.id.product_photo)
    }

}