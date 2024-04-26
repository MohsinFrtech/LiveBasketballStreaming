package com.depvt.live.football.tv

import android.app.Application
import android.util.Log
import com.depvt.live.football.tv.adsData.AppOpenManager
import com.p2pengine.core.p2p.EngineExceptionListener
import com.p2pengine.core.p2p.P2pConfig
import com.p2pengine.core.segment.StrictHlsSegmentIdGenerator
import com.p2pengine.core.utils.AnnounceLocation
import com.p2pengine.core.utils.EngineException
import com.p2pengine.sdk.P2pEngine


class MyApp:Application() {

    private var appOpenManager: AppOpenManager? = null
    override fun onCreate() {
        super.onCreate()
        appOpenManager = AppOpenManager(this)

        try {
            val config = P2pConfig.Builder()
                .p2pEnabled(true)
//                .logEnabled(true)
//                .logLevel(LogLevel.DEBUG)
                .announceLocation(AnnounceLocation.USA)
//            .announceLocation(AnnounceLocation.HongKong)
//            .announceLocation(AnnounceLocation.USA)
                .build()

            //println("MainActivity P2pEngine init")
            P2pEngine.init(applicationContext, "WUAlHM1Vg", config)
            P2pEngine.instance?.setHlsSegmentIdGenerator(StrictHlsSegmentIdGenerator())

            P2pEngine.instance?.registerExceptionListener(object : EngineExceptionListener {
                override fun onOtherException(e: EngineException) {

                }

                override fun onSchedulerException(e: EngineException) {

                }

                override fun onSignalException(e: EngineException) {

                }

                override fun onTrackerException(e: EngineException) {

                }
            })

        }
        catch (e:Exception)
        {
            Log.d("Exception","msg")
        }
    }
}