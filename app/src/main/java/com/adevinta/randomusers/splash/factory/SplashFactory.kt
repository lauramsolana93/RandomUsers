package com.adevinta.randomusers.splash.factory

import android.content.Context

interface SplashFactory {

    suspend fun checkNetworkConnection(context: Context): Boolean
}