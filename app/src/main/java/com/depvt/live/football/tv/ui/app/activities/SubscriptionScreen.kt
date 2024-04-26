package com.depvt.live.football.tv.ui.app.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.ProductDetailsResponseListener
import com.depvt.live.football.tv.R
import com.depvt.live.football.tv.billing.BillingManager
import com.depvt.live.football.tv.billing.BillingProvider
import com.depvt.live.football.tv.billing.RecyclerViewClickListener
import com.depvt.live.football.tv.billing.SkuRowData
import com.depvt.live.football.tv.databinding.ActivitySubscriptionScreenBinding
import com.depvt.live.football.tv.ui.app.adapters.AdapterPackages
import com.depvt.live.football.tv.utils.Logger
import java.util.*

class SubscriptionScreen : AppCompatActivity(), BillingProvider, RecyclerViewClickListener {


    private var mBillingManager: BillingManager? = null
    private var skuDetails: ArrayList<ProductDetails> = ArrayList()
    private var binding: ActivitySubscriptionScreenBinding? = null
    private val logger = Logger()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_subscription_screen)
        mBillingManager = BillingManager(this, this)
        showProgressDialog()

        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

        binding?.backIcon2?.setOnClickListener {
            finish()
        }

    }

    private fun onManagerReady() {
        val inList: MutableList<SkuRowData> = ArrayList()
        val responseListener =
            ProductDetailsResponseListener { billingResult: BillingResult, list: List<ProductDetails>? ->

                runOnUiThread {
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && list?.isNotEmpty() == true) {
                        Log.d("ManagerComes","come"+inList)

                        binding?.noData?.visibility=View.GONE

                        try {
                            Collections.sort(list, SkuCompare())
                            for (i in list.indices) {
                                inList.add(
                                    SkuRowData(
                                        list[i].productId,
                                        list[i].title,
                                        list[i].subscriptionOfferDetails!![0]
                                            .pricingPhases.pricingPhaseList[0].formattedPrice,
                                        list[i].description,
                                        list[i].productType,
                                        list[i].subscriptionOfferDetails!![0]
                                            .pricingPhases.pricingPhaseList[0]
                                            .priceAmountMicros
                                    )
                                )
                                skuDetails.add(list[i])
                            }
                            if (inList.isNotEmpty()) {
                                Collections.sort(inList, PriceCompare())
                                binding?.noData?.visibility = View.GONE
                                setUpRecyclerView(inList)
                                closeProgressDialog()
                            } else {
                                closeProgressDialog()
                                binding?.noData?.visibility = View.VISIBLE
                            }
                        } catch (e: Exception) {


                            logger.printLog(this.localClassName, "Exception: ${e.localizedMessage}")
                        }
                    } else {
                        binding?.noData?.visibility=View.VISIBLE

                        closeProgressDialog()
                    }
                }

            }

        val skus = mBillingManager?.getSkus(BillingClient.ProductType.SUBS)
        skus?.let {
            mBillingManager?.querySkuDetailsAsync(
                BillingClient.ProductType.SUBS,
                it,
                responseListener
            )
        }
    }

    internal class PriceCompare : Comparator<SkuRowData> {
        override fun compare(o1: SkuRowData, o2: SkuRowData): Int {
            val age1 = o1.price_amount_micros
            val age2 = o2.price_amount_micros
            return age2.compareTo(age1)
        }
    }

    internal class SkuCompare : Comparator<ProductDetails> {
        override fun compare(o1: ProductDetails, o2: ProductDetails): Int {
            var age1: Long = 0
            if (o1.subscriptionOfferDetails != null) {
                age1 =
                    o1.subscriptionOfferDetails!![0].pricingPhases.pricingPhaseList[0].priceAmountMicros
            }
            var age2: Long = 0
            if (o2.subscriptionOfferDetails != null) {
                age2 =
                    o2.subscriptionOfferDetails!![0].pricingPhases.pricingPhaseList[0].priceAmountMicros
            }
            return age2.compareTo(age1)
        }
    }

    private fun setUpRecyclerView(inList: List<SkuRowData>) {
        val llm = LinearLayoutManager(this)
        binding?.recyclerViewPackages?.layoutManager = llm

        val adapter = AdapterPackages(inList, this)
        binding?.recyclerViewPackages?.adapter = adapter
    }

    private fun showProgressDialog() {
        binding?.lottieSubs?.visibility = View.VISIBLE
    }

    private fun closeProgressDialog() {

        binding?.lottieSubs?.visibility = View.GONE
    }

    override fun onReady() {
        onManagerReady()
    }

    override fun onPurchaseSuccessful() {
        showProgressDialog()
        skuDetails.clear()
        onManagerReady()
    }

    override fun recyclerViewListClicked(position: Int) {
        mBillingManager!!.startPurchaseFlow(skuDetails[position])
    }


}