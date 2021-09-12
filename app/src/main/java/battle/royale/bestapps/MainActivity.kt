package battle.royale.bestapps

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import battle.royale.bestapps.R
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {
    var nav: NavigationView? = null
    var toggle: ActionBarDrawerToggle? = null
    var drawerLayout: DrawerLayout? = null
    private var backPressedTime: Long = 0
    var imageView2: ImageView? = null
    private val mDrawer: DrawerLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val equipmentFragment = EquipmentFragment()
        val weaponsFragment = WeaponsFragment()
        val favoriteFragment = FavoriteFragment()
        setCurrentFragment(equipmentFragment)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        nav = findViewById<View>(R.id.navmenu) as NavigationView
        drawerLayout = findViewById<View>(R.id.drawer) as DrawerLayout
        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        toggle!!.drawerArrowDrawable.color = Color.parseColor("#FF8D22");
        supportActionBar!!.setDisplayHomeAsUpEnabled(true);
        toggle?.setHomeAsUpIndicator(R.drawable.sprite_4)
        toggle!!.isDrawerIndicatorEnabled = true;

        drawerLayout!!.addDrawerListener(toggle!!)
        toggle!!.syncState()
        nav!!.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_equipment -> {
                    Toast.makeText(applicationContext, "Equipment", Toast.LENGTH_SHORT).show()
                    drawerLayout!!.closeDrawer(GravityCompat.START)
                    findViewById<TextView>(R.id.page_title).text = "Equipment"
                    setCurrentFragment(equipmentFragment)
                }
                R.id.menu_weapons -> {
                    Toast.makeText(applicationContext, "Weapons", Toast.LENGTH_SHORT).show()
                    drawerLayout!!.closeDrawer(GravityCompat.START)
                    findViewById<TextView>(R.id.page_title).text = "Weapons"
                    setCurrentFragment(weaponsFragment)
                }
                R.id.menu_favorite -> {
                    Toast.makeText(applicationContext, "Favorite", Toast.LENGTH_SHORT).show()
                    drawerLayout!!.closeDrawer(GravityCompat.START)
                    findViewById<TextView>(R.id.page_title).text = "Favorite"
                    setCurrentFragment(favoriteFragment)
                }
                R.id.menu_privacy -> {
                    Toast.makeText(applicationContext, "Privacy Policy", Toast.LENGTH_SHORT).show()
                    findViewById<TextView>(R.id.page_title).text = "Privacy Policy"
                    drawerLayout!!.closeDrawer(GravityCompat.START)
                }
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment)
            commit()
        }

    override fun onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
            overridePendingTransition(R.anim.slidein, R.anim.slideout)
            return
        }
        backPressedTime = System.currentTimeMillis()
    }
}