package com.dan.newsfeedswithinmvvm.ui.util

import android.content.Context
import android.net.NetworkCapabilities
import android.os.Build

/**
 * Created by Dan Kim
 */
class ConnectivityManagerImpl(
    private val context: Context
) : ConnectivityManager {

    override fun isInternetConnected(): Boolean {
        val connectivityManager = context.applicationContext.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as android.net.ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }

        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    android.net.ConnectivityManager.TYPE_WIFI -> true
                    android.net.ConnectivityManager.TYPE_MOBILE -> true
                    android.net.ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
            return false
        }
    }
}