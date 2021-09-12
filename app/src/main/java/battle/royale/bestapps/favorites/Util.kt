package battle.royale.bestapps.favorites

import android.content.Context
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

object Util {
    fun add(context: Context, photo: String, link: String, title: String) {
        GlobalScope.launch {
            AppDatabase(context).productDao().insertAll(Product(UUID.randomUUID().toString(), photo, link, title))
        }
    }
    fun getAll(context: Context): List<Product> {
        return AppDatabase(context).productDao().getAll()
    }

    fun findByPhoto(context: Context, photo: String): Product {
        return AppDatabase(context).productDao().findByPhoto(photo)[0]
    }
}