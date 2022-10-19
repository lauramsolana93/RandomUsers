package com.adevinta.randomusers.splash.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.adevinta.randomusers.common.utils.Resource

interface SplashViewModel {

    val networkConnection: MutableLiveData<Resource<Boolean>>

    fun checkNetworkConnection(context: Context)
}