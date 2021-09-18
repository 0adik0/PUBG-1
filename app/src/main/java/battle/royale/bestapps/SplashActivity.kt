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
    private var billingClient: BillingClient? = null
    private var skuDetails: SkuDetails? = null
    private val TAG = "moneys_activity_billing"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        isUserHasSubscription(this)

    }

    private fun setUpBillingClient() {
        billingClient = BillingClient.newBuilder(this)
            .setListener(purchaseUpdateListener)
            .enablePendingPurchases()
            .build()
        startConnection()
    }
    private fun startConnection() {
        billingClient?.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    Log.d("billing_process", "Setup Billing Done")
                    queryAvaliableProducts()
                } else {
                    Log.d("billing_process", "Error: ${billingResult.debugMessage}")
                }
            }

            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                Log.d("billing_process", "Setup Billing disconnected")
            }
        })
    }

    private fun queryAvaliableProducts() {
        val skuList = ArrayList<String>()
        skuList.add("com.battleroyale.subsc.newtop")
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS)

        billingClient?.querySkuDetailsAsync(params.build()) { billingResult, skuDetailsList ->
            // Process the result.
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && !skuDetailsList.isNullOrEmpty()) {

                Log.d(
                    "billing_process",
                    "skuDetailsList : ${skuDetailsList}; ${skuDetailsList.size}"
                )
                val filteredList = skuDetailsList.sortedBy { it.priceAmountMicros }
                for (elements in  filteredList) {
                    Log.d("billing_process_item", " $elements")
                    runOnUiThread {
                        updateUI(elements)
                    }

                }
            } else {
                Log.d("billing_process", "Error: ${billingResult.debugMessage} | ${billingResult.responseCode} | ${skuDetailsList?.size}")
            }
        }
    }

    private fun isUserHasSubscription(context: Context) {
        billingClient = BillingClient.newBuilder(this)
            .setListener(purchaseUpdateListener)
            .enablePendingPurchases()
            .build()
        billingClient!!.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                val purchasesResult = billingClient!!.queryPurchases(BillingClient.SkuType.SUBS)
                billingClient!!.queryPurchaseHistoryAsync(
                    BillingClient.SkuType.SUBS
                ) { billingResult1: BillingResult, list: List<PurchaseHistoryRecord?>? ->
                    Log.d("billing_process", "res: ${billingResult1.responseCode}, ${list?.isNotEmpty()}")
                    list?.forEach {
                        Log.d("billing_process", "list: ${it}")
                    }
                    if (list != null) {
                        if (billingResult1.responseCode == BillingClient.BillingResponseCode.OK && list.isNotEmpty()) {

                            //here you can pass the user to use the app because he has an active subscription
                            Handler().postDelayed({

                                /* Create an Intent that will start the Menu-Activity. */
                                val mainIntent = Intent(this@SplashActivity, Premium1Activity::class.java)
                                startActivity(mainIntent)
                                finish()
                            }, 500)
                        } else {
                            setUpBillingClient()
                        }
                    } else {
                        setUpBillingClient()
                    }

                }
            }

            override fun onBillingServiceDisconnected() {
                setUpBillingClient()
            }
        })
    }
    private fun updateUI(skuDetails: SkuDetails) {
        Log.d(TAG, skuDetails.description)
        skuDetails.let {
            this.skuDetails = it
            Log.d(TAG, it.description)
        }

    }


    private val purchaseUpdateListener =
        PurchasesUpdatedListener { billingResult, purchases ->
            Log.d("billing_process", "billingResult responseCode : ${billingResult.responseCode}")

            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                for (purchase in purchases) {
//                        handlePurchase(purchase)
                    handleConsumedPurchases(purchase)
                }
            } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
                // Handle an error caused by a user cancelling the purchase flow.
            } else {
                // Handle any other error codes.
            }
        }

    private fun handleConsumedPurchases(purchase: Purchase) {
        Log.d("billing_process", "handleConsumablePurchasesAsync foreach it is $purchase")
        val params =
            ConsumeParams.newBuilder().setPurchaseToken(purchase.purchaseToken).build()
        billingClient?.consumeAsync(params) { billingResult, purchaseToken ->
            when (billingResult.responseCode) {
                BillingClient.BillingResponseCode.OK -> {
                    val myIntent = Intent(this@SplashActivity, Premium1Activity::class.java)
                    startActivity(myIntent)
                }
                else -> {
                    Log.d("billing_process", billingResult.debugMessage)
                }
            }
        }
    }
}