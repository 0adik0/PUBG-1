package battle.royale.bestapps

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import battle.royale.bestapps.R
import android.content.Intent
import android.os.Handler
import android.util.Log
import battle.royale.bestapps.Premium1Activity
import com.android.billingclient.api.*
import com.android.billingclient.api.BillingClient

import com.android.billingclient.api.Purchase

import com.android.billingclient.api.BillingResult

import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.Purchase.PurchasesResult
import java.util.*
import kotlin.collections.ArrayList


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({

            /* Create an Intent that will start the Menu-Activity. */
            val mainIntent = Intent(this@SplashActivity, Premium1Activity::class.java)
            startActivity(mainIntent)
            finish()
        }, 500)


    }


}