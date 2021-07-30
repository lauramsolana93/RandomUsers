package com.adevinta.randomusers.splash.factory

import android.content.Context
import com.adevinta.randomusers.common.utils.ConnectivityHelper

class SplashFactoryImpl : SplashFactory {

    private var connectivityHelper = ConnectivityHelper()

    override suspend fun checkNetworkConnection(context: Context): Boolean {
        return connectivityHelper.isConnectedToNetwork(context)
    }

}