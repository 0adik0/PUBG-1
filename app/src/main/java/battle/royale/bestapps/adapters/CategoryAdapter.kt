package battle.royale.bestapps.adapters

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import battle.royale.bestapps.R
import battle.royale.bestapps.models.Category
import battle.royale.bestapps.models.Subcategory
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.util.ArrayList

class CategoryAdapter(val recyclerView: RecyclerView, val subRecyclerView: RecyclerView, val list: ArrayList<Category>, val context: Context) :
        RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    var activeItem: Int = 0
    var viewGroup: ViewGroup? = null
    var used = false
    val subcategoriesList = ArrayList<Subcategory>()
    lateinit var adapter: SubcategoryAdapter
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.category_item, parent, false)
        viewGroup = parent
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val data = list[position]
        holder.title.text = data.title
        holder.divider.visibility = GONE
        if (position == 0 && !used) {
           adapter = SubcategoryAdapter(subRecyclerView, subcategoriesList, context)
            subRecyclerView.adapter = adapter
            val mLayoutManager: RecyclerView.LayoutManager = GridLayoutManager(context, 3)
            subRecyclerView.layoutManager = mLayoutManager
            subRecyclerView.adapter = adapter
            holder.divider.visibility = VISIBLE
            activeItem = position
            used = true
            getSubcategories(list[position].link)

        }
        holder.root.setOnClickListener {
            val previousItem = activeItem
            activeItem = position
            recyclerView.findViewHolderForAdapterPosition(previousItem)?.itemView?.findViewById<ImageView>(R.id.additional_divider)?.visibility = View.GONE
            recyclerView.findViewHolderForAdapterPosition(activeItem)?.itemView?.findViewById<ImageView>(R.id.additional_divider)?.visibility = View.VISIBLE
            getSubcategories(list[position].link)
            notifyItemChanged(previousItem)
        }


    }
    private fun getSubcategories(link: String) {
        Thread {
//
            try {
                val doc: Document = Jsoup.connect(link).get()
                val div: Elements = doc.select("div.mw-category-generated").select("li.gallerybox")
                for ((i, link) in div.withIndex()) {
                        Log.d("parser_data_link_sub1", "data: ${link.attr("href")} | ${link.text()} | size: ${subcategoriesList.size} | ${link.select("a[href]").get(0).attr("href")}")
                    Log.d("parser_data_link_sub3", "${div.select("div.gallerytext").select("a[href]")[0].attr("href")}")
                        subcategoriesList.add(Subcategory(div.select("div.gallerytext").select("a[href]")[0].attr("href"), link.text(), link.select("a[href]")[0].attr("href"), false))
                        (context as Activity).runOnUiThread {
                            adapter.notifyItemChanged(i)
                        }
                }

            } catch (e: Exception) {
                Log.d("parser_data_link_sub", "error: $e")
            }
        }.start()
        adapter.notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }



    class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root = view
        var title: TextView = view.findViewById(R.id.title)
        var divider: ImageView = view.findViewById(R.id.additional_divider)
    }

}