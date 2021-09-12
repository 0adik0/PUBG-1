package battle.royale.bestapps

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import battle.royale.bestapps.R
import battle.royale.bestapps.adapters.CategoryAdapter
import battle.royale.bestapps.models.Category
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.util.ArrayList

class EquipmentFragment : Fragment() {
    lateinit var adapter: CategoryAdapter
    val categoriesList = ArrayList<Category>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root =  inflater.inflate(R.layout.fragment_weapons, container, false)
        val categoriesView = root.findViewById<RecyclerView>(R.id.categories)
        val subcategoriesView = root.findViewById<RecyclerView>(R.id.subcategories)
        adapter = CategoryAdapter(categoriesView, subcategoriesView, categoriesList, requireActivity())


        val llm = LinearLayoutManager(requireActivity())
        llm.orientation = LinearLayoutManager.HORIZONTAL
        categoriesView.layoutManager = llm
        categoriesView.adapter = adapter
        getCategories()
        return root
    }

    private fun getCategories() {
        Thread {
            try {
                val doc: Document = Jsoup.connect("https://pubg.fandom.com/wiki/Category:Equipment").get()
                val links: Elements = doc.select("div.mw-category").select("a[href]")
                for ((i, link) in links.withIndex()) {

                    if (link.attr("href").contains("Category:")) {
                        Log.d("parser_data_link", "data: ${link.attr("href")} | ${link.text()} | size: ${categoriesList.size}")
                        categoriesList.add(Category(DOMAIN + link.attr("href"), link.text(), false))
                        requireActivity().runOnUiThread {
                            adapter.notifyItemChanged(i)
                        }

                    }
                }

            } catch (e: Exception) {
                Log.d("parser_data_link", "error: $e")
            }
        }.start()
        adapter.notifyDataSetChanged()
    }
}