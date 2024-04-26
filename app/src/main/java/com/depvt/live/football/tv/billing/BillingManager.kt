package com.depvt.live.football.tv.billing

import android.app.Activity
import com.android.billingclient.api.*
import com.depvt.live.football.tv.utils.Logger
import com.depvt.live.football.tv.utils.objects.Constants.oldSkuList
import com.depvt.live.football.tv.utils.objects.Constants.removeAds
import com.google.common.collect.ImmutableList


class BillingManager(
    private val mActivity: Activity,
    private val billingProvider: BillingProvider
) : PurchasesUpdatedListener {

    private val logger = Logger()

    private val mBillingClient: BillingClient =
        BillingClient.newBuilder(mActivity).enablePendingPurchases().setListener(this).build()

    private var oldSku = ""
    private val products: MutableList<QueryProductDetailsParams.Product> = ArrayList()

    init {
        startServiceConnectionIfNeeded(null)
    }

    private fun startServiceConnectionIfNeeded(executeOnSuccess: Runnable?) {
        if (mBillingClient.isReady) {
            executeOnSuccess?.run()
        } else {
            mBillingClient.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        checkPurchase()
                        billingProvider.onReady()
                        executeOnSuccess?.run()
                    }
                }

                override fun onBillingServiceDisconnected() {

                }
            })
        }
    }

    companion object {
        private var SKUS: HashMap<String, List<String>>? = null

        init {
            SKUS = HashMap()
            SKUS!![BillingClient.ProductType.SUBS] =
                listOf(
                    "remove_ad",
                    "remove_ad_1",
                    "remove_ad_3",
                    "remove_ad_12",
                    )
        }
    }

    //    private static final String SUBS_SKUS[] = {"remove_ads"};
    fun querySkuDetailsAsync(
        @BillingClient.ProductType itemType: String?,
        skuList: List<String?>,
        listener: ProductDetailsResponseListener
    ) {
        // Specify a runnable to start when connection to Billing client is established
        products.clear()
        val executeOnConnectedService = Runnable {
            for (i in skuList.indices) {
                val productItem = QueryProductDetailsParams.Product
                    .newBuilder()
                    .setProductId(skuList[i]!!)
                    .setProductType(itemType!!)
                    .build()
                products.add(productItem)
            }
            val skuDetailsParams = QueryProductDetailsParams.newBuilder()
                .setProductList(products)
                .build()
            mBillingClient.queryProductDetailsAsync(
                skuDetailsParams
            ) { billingResult: BillingResult?, productDetailsList: List<ProductDetails> ->

                // Process the result
                listener.onProductDetailsResponse(billingResult!!, productDetailsList)
            }
        }

        // If Billing client was disconnected, we retry 1 time and if success, execute the query
        startServiceConnectionIfNeeded(executeOnConnectedService)
    }

    fun getSkus(@BillingClient.ProductType type: String): List<String> {
        return SKUS!![type]!!
    }

    fun startPurchaseFlow(skuDetails: ProductDetails) {
        if (skuDetails.subscriptionOfferDetails != null) {
            val offerToken = skuDetails.subscriptionOfferDetails!![0]
                .offerToken
            val productDetailsParamsList = ImmutableList.of(
                BillingFlowParams.ProductDetailsParams.newBuilder()
                    .setProductDetails(skuDetails)
                    .setOfferToken(offerToken)
                    .build()
            )
            val billingFlowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(productDetailsParamsList)
                .build()
            mBillingClient.launchBillingFlow(mActivity, billingFlowParams)
        }
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: List<Purchase>?) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK
            && purchases != null
        ) {
            for (purchase in purchases) {
                handlePurchase(purchase)
            }
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            // Grant entitlement to the user.
            removeAds = true
            checkPurchase()


            billingProvider.onPurchaseSuccessful()

            // Acknowledge the purchase if it hasn't already been acknowledged.
            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()
                //mBillingClient.acknowledgePurchase(acknowledgePurchaseParams) { billingResult: BillingResult? -> }
                mBillingClient.acknowledgePurchase(acknowledgePurchaseParams) {  }
            }
        }
    }

    private fun checkPurchase() {
        try {
            mBillingClient.queryPurchasesAsync(
                QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.SUBS)
                    .build()
            ) { _: BillingResult?, purchases: List<Purchase>? ->
                if (purchases != null) {
                    oldSkuList.clear()
                    for (i in purchases.indices) {
                        removeAds = true
                        oldSku = purchases[i].products[0]
                        //logger.printLog("Subscription_check", "check$oldSku")
                        oldSkuList.add(purchases[i].products[0])
                    }
                    if (purchases.isEmpty()) {
                        removeAds = false
                        oldSku = ""
                        oldSkuList = ArrayList()
                    }
                } else {
                    removeAds = false
                    oldSku = ""
                    oldSkuList = ArrayList()
                }
            }
        } catch (e: Exception) {
            logger.printLog("Exception", "failed : ${e.localizedMessage}")
            logger.printLog("Exception", "failed : ${e.cause}")
        }

    }

}