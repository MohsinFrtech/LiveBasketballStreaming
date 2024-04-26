package com.depvt.live.football.tv.utils.objects

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

object InternetUtil {

    fun isInternetOn(context: Context?): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val connectivityManager = context?.getSystemService(ConnectivityManager::class.java)
            val activeNetwork = connectivityManager?.activeNetwork
            val networkCapabilities = connectivityManager?.getNetworkCapabilities(activeNetwork)
            if (networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true) {

                true
            } else networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true

        } else {
            true
        }
    }

}