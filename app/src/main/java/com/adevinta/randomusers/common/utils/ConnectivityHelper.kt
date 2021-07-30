package com.adevinta.randomusers.common.utils

import android.content.Context
import android.net.ConnectivityManager

class ConnectivityHelper {

    fun isConnectedToNetwork(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        var isConnected = false

        if (connectivityManager != null) {
            val activeNetwork = connectivityManager.activeNetwork
            isConnected = activeNetwork != null
        }
        return isConnected
    }

}