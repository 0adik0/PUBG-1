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
import battle.royale.bestapps.favorites.Product
import battle.royale.bestapps.models.Subcategory
import com.bumptech.glide.Glide
import java.util.ArrayList

class FavoritesAdapter(val list: ArrayList<Product>, val context: Context) :
        RecyclerView.Adapter<FavoritesAdapter.SubcategoryViewHolder>() {
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
        holder.title.text = data.title
        Glide.with(context).load(data.photo).into(holder.photo)
        holder.root.setOnClickListener {
            val intent = Intent(context, InfoActivity::class.java)
            intent.putExtra("title", list[position].title)
            intent.putExtra("photo", list[position].photo)
            intent.putExtra("link", list[position].link)
            if (list[position].id != "" && list[position].id.isNotEmpty()) {
                intent.putExtra("id", list[position].id)
            }
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