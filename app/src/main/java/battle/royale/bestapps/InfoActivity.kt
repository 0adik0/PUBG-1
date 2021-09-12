package battle.royale.bestapps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import battle.royale.bestapps.favorites.AppDatabase
import battle.royale.bestapps.favorites.Methods
import battle.royale.bestapps.favorites.Util.add
import battle.royale.bestapps.favorites.Util.findByPhoto
import com.bumptech.glide.Glide
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class InfoActivity : AppCompatActivity() {
    companion object {
        val liveData = MutableLiveData<Methods>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
        getDescription()
        Glide.with(this).load(intent.getStringExtra("photo")).into( findViewById(R.id.photoInfo))
        GlobalScope.launch {
            try {
                Log.d("favorite_error_info", "productData: ${AppDatabase(this@InfoActivity).productDao().findByPhoto(intent.getStringExtra("photo").toString())[0] != null}")
                if (AppDatabase(this@InfoActivity).productDao().findByPhoto(intent.getStringExtra("photo").toString())[0] != null) {
                    runOnUiThread {
                        findViewById<ImageView>(R.id.add_to_favorites).setImageDrawable(getDrawable(R.drawable.sprite_3))
                    }
                } else {
                    runOnUiThread {
                        findViewById<ImageView>(R.id.add_to_favorites).setImageDrawable(getDrawable(R.drawable.sprite_3_white))
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    findViewById<ImageView>(R.id.add_to_favorites).setImageDrawable(getDrawable(R.drawable.sprite_3_white))
                }

            }

        }
        findViewById<TextView>(R.id.titleInfo).text = intent.getStringExtra("title")
        findViewById<ImageView>(R.id.add_to_favorites).setOnClickListener {
            GlobalScope.launch {
                try {
                    if (AppDatabase(this@InfoActivity).productDao().findByPhoto(intent.getStringExtra("photo").toString())[0] != null) {
                        Log.d("adding_item", "${intent.getStringExtra("link").toString()}")
                        AppDatabase(this@InfoActivity).productDao().delete(findByPhoto(this@InfoActivity, intent.getStringExtra("photo").toString()))
                        runOnUiThread {
                            findViewById<ImageView>(R.id.add_to_favorites).setImageDrawable(getDrawable(R.drawable.sprite_3_white))
                            Toast.makeText(
                                this@InfoActivity,
                                "Item was successfully deleted",
                                Toast.LENGTH_SHORT
                            ).show()
                            liveData.postValue(Methods.DEELTE)
                        }

                    } else {
                        Log.d("adding_item", "${intent.getStringExtra("link").toString()}")
                        add(this@InfoActivity, intent.getStringExtra("photo").toString(), intent.getStringExtra("link").toString(), intent.getStringExtra("title").toString())
                        runOnUiThread {
                            findViewById<ImageView>(R.id.add_to_favorites).setImageDrawable(getDrawable(R.drawable.sprite_3))
                            Toast.makeText(
                                this@InfoActivity,
                                "Item was successfully added",
                                Toast.LENGTH_SHORT
                            ).show()
                            liveData.postValue(Methods.ADD)
                        }
                    }
                } catch (e: Exception) {
                    Log.d("adding_item_error", "$e")
                    add(this@InfoActivity, intent.getStringExtra("photo").toString(), intent.getStringExtra("link").toString(), intent.getStringExtra("title").toString())
                    Log.d("adding_item", "${intent.getStringExtra("link").toString()}")
                    runOnUiThread {
                        findViewById<ImageView>(R.id.add_to_favorites).setImageDrawable(getDrawable(R.drawable.sprite_3))
                        Toast.makeText(
                            this@InfoActivity,
                            "Item was successfully added",
                            Toast.LENGTH_SHORT
                        ).show()
                        liveData.postValue(Methods.ADD)
                    }

                }

            }
        }
    }
    fun getDescription() {
        Thread {
            try {
                val doc: Document = Jsoup.connect(DOMAIN.dropLast(1) + intent.getStringExtra("link")!!).get()
               val links: Elements = doc.select("div[class=mw-parser-output] > p")
                Log.d("parser_data_info", "links: ${links[2]} | ${intent.getStringExtra("link")}")
                runOnUiThread {

                    findViewById<TextView>(R.id.info).text = Html.fromHtml(links[2].toString().drop(3).dropLast(4))
                }


            } catch (e: Exception) {
                Log.d("parser_data_info", "error: $e")
            }
        }.start()
    }
}