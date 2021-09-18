package battle.royale.bestapps

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.ValueEventListener
import android.widget.TextView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import android.os.Bundle
import battle.royale.bestapps.R
import android.widget.VideoView
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.MediaController
import battle.royale.bestapps.MainActivity
import com.android.billingclient.api.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

class Premium3Activity : AppCompatActivity(), ValueEventListener {
    private var text3: TextView? = null
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference = firebaseDatabase.reference
    private var billingClient: BillingClient? = null
    private var skuDetails: SkuDetails? = null
    private val TAG = "moneys_activity_billing"
    private val billing = databaseReference.child("app_billing").child("text")
    var imageView3: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_premium3)
        text3 = findViewById(R.id.textView3)

        // =====================================================================================
        val videoView = findViewById<VideoView>(R.id.videoPlayer)
        val videoPath = "android.resource://" + packageName + "/" + R.raw.pubgvideo
        val uri = Uri.parse(videoPath)
        videoView.setVideoURI(uri)
        val mediaController = MediaController(this)
        videoView.setMediaController(mediaController)
        mediaController.setAnchorView(videoView)

        // =====================================================================================
        imageView3 = findViewById(R.id.imageView3)
        imageView3?.setOnClickListener {
          isUserHasSubscription(this)
        }
    }

    override fun onDataChange(dataSnapshot: DataSnapshot) {
        if (dataSnapshot.getValue(String::class.java) != null) {
            val key = dataSnapshot.key
            if (key == "text") {
                val text = dataSnapshot.getValue(String::class.java)
                text3!!.text = text
            }
        }
    }

    override fun onCancelled(error: DatabaseError) {}
    override fun onStart() {
        super.onStart()
        billing.addValueEventListener(this)
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
                            val intent = Intent(this@Premium3Activity, MainActivity::class.java)
                            startActivity(intent)
                            overridePendingTransition(R.anim.slidein, R.anim.slideout)
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
                    val intent = Intent(this@Premium3Activity, MainActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slidein, R.anim.slideout)
                }
                else -> {
                    Log.d("billing_process", billingResult.debugMessage)
                }
            }
        }
    }
}