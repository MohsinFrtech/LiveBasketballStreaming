package com.depvt.live.football.tv.adsData

import android.content.Context
import com.chartboost.sdk.Chartboost
import com.chartboost.sdk.events.StartError
import com.chartboost.sdk.privacy.model.CCPA
import com.chartboost.sdk.privacy.model.COPPA
import com.chartboost.sdk.privacy.model.GDPR
import com.depvt.live.football.tv.utils.objects.Constants
import com.facebook.ads.AudienceNetworkAds
import com.google.android.exoplayer2.util.Log
import com.google.android.gms.ads.MobileAds
import com.startapp.sdk.adsbase.StartAppAd
import com.startapp.sdk.adsbase.StartAppSDK
import com.depvt.live.football.tv.models.AppAd
import com.unity3d.ads.IUnityAdsInitializationListener
import com.unity3d.ads.UnityAds

class AdSdkInitialization(val context: Context?) {

    fun initializeSdk(list: List<AppAd>)
    {
        for (adItem in list)
        {
            if (adItem.enable == true)
            {
                if (adItem.ad_provider.equals(Constants.admob,true))
                {
                    admobSdkInitialization(context)
                }
                else if (adItem.ad_provider.equals(Constants.facebook,true))
                {
                        facebookSdkInitialization(context)
                }
                else if (adItem.ad_provider.equals(Constants.chartBoost,true))
                {

                     chartBoostSdkInitialization(context)
                }
                else if (adItem.ad_provider.equals(Constants.unity,true))
                {

                    unitySdkInitialization(context)

                }
                else if (adItem.ad_provider.equals(Constants.startApp,true))
                {
                    startAppSdkInitialization(context)

                }

            }



        }

    }

     private fun startAppSdkInitialization(startAppContext: Context?) {
        try {
            if (startAppContext !=null)
            {
                StartAppSDK.init(startAppContext, Constants.startAppId, false)
                StartAppAd.disableSplash()
                StartAppSDK.setTestAdsEnabled(true)
                Constants.isStartAppSdkInit=true
            }

        } catch (e: Exception) {

            Log.d("Exception",""+e.message)
        }
    }

     private fun unitySdkInitialization(unityContext: Context?) {
        UnityAds.initialize(unityContext, Constants.unityGameID, Constants.unityTestMode, object :
            IUnityAdsInitializationListener {
            override fun onInitializationComplete() {
                if (UnityAds.isInitialized())
                {

                    Constants.isUnitySdkInit=true
                }

            }

            override fun onInitializationFailed(
                p0: UnityAds.UnityAdsInitializationError?,
                p1: String?
            ) {

                Constants.isUnitySdkInit=false
            }
        })
    }

     private fun chartBoostSdkInitialization(chartboostContext: Context?) {
        if (chartboostContext !=null)
        {
            Chartboost.addDataUseConsent(chartboostContext, GDPR(GDPR.GDPR_CONSENT.BEHAVIORAL))
            Chartboost.addDataUseConsent(chartboostContext, CCPA(CCPA.CCPA_CONSENT.OPT_IN_SALE))
            Chartboost.addDataUseConsent(chartboostContext, COPPA(true))

            Chartboost.startWithAppId(
                chartboostContext, Constants.chartBoostAppID, Constants.chartBoostAppSig
            ) { startError: StartError? ->
                if (startError == null) {
                    Constants.isChartboostSdkInit=true
                    // handle success
                } else {

                    // handle failure
                }
            }
        }

    }

     private fun facebookSdkInitialization(facebookContext: Context?) {
        AudienceNetworkAds
            .buildInitSettings(facebookContext)
            .withInitListener {
                if (it.isSuccess)
                {
                    Constants.isInitFacebookSdk=true
                    Log.d("Sdk","value")
                }
                else{

                    Constants.isInitFacebookSdk=false
                }

            }
            .initialize()
    }

      private fun admobSdkInitialization(admobContext: Context?) {
        if (admobContext != null) {
            MobileAds.initialize(admobContext) { p0 ->
                Constants.isInitAdmobSdk=true
                p0.toString()

            }
        }
    }


}