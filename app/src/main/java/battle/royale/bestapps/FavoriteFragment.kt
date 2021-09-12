package battle.royale.bestapps

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import battle.royale.bestapps.InfoActivity.Companion.liveData
import battle.royale.bestapps.adapters.FavoritesAdapter
import battle.royale.bestapps.adapters.SubcategoryAdapter
import battle.royale.bestapps.favorites.Product
import battle.royale.bestapps.favorites.Util.getAll
import battle.royale.bestapps.models.Subcategory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.ArrayList

class FavoriteFragment : Fragment() {
    lateinit var adapter: FavoritesAdapter
    val list = ArrayList<Product>()
    val data = HashMap<String, String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_weapons, container, false)
        val categoriesView = root.findViewById<RecyclerView>(R.id.categories)
        categoriesView.visibility = View.GONE
        val subcategoriesView = root.findViewById<RecyclerView>(R.id.subcategories)
        adapter = FavoritesAdapter(list, requireActivity())
        val mLayoutManager: RecyclerView.LayoutManager = GridLayoutManager(context, 3)
        subcategoriesView.layoutManager = mLayoutManager
        subcategoriesView.adapter = adapter


        Log.d("parser_data_link_size", "list: ${list.size}")

        return  root
    }

    private fun getFavorites(context: Context?) {
        try {
            (context as Activity).runOnUiThread {
                list.clear()
                data.clear()
                adapter.notifyDataSetChanged()
            }
        } catch (e: Exception) {
            Log.d("parser_data_link_er", "error: $e")
        }
        GlobalScope.launch {

            try {
                if (context != null) {
                    for (elements in getAll(context)) {
                        if (data[elements.photo].toString() == "null" || data[elements.photo].toString().isEmpty()) {
                            list.add(elements)
                            data[elements.photo] = elements.photo
                        }

                    }
                    (context as Activity).runOnUiThread {
                        adapter.notifyDataSetChanged()
                    }
                }

            } catch (e: Exception) {
                Log.d("parser_data_link_er", "error: $e")
            }

        }



    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        liveData.observe(requireActivity()) {
            getFavorites(activity)

        }
        if (list.isEmpty()) {
            getFavorites(activity)
        }
    }
}