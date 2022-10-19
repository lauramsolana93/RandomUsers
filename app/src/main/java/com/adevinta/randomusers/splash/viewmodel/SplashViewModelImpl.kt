package com.adevinta.randomusers.splash.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adevinta.randomusers.common.utils.Resource
import com.adevinta.randomusers.splash.factory.SplashFactory
import kotlinx.coroutines.launch

class SplashViewModelImpl constructor(
    private val factory: SplashFactory
) : SplashViewModel, ViewModel() {

    override val networkConnection : MutableLiveData<Resource<Boolean>> by lazy {
        MutableLiveData()
    }

    override fun checkNetworkConnection(context: Context) {
        networkConnection.postValue(Resource.Loading())
        viewModelScope.launch {
            val result = factory.checkNetworkConnection(context)
            if (result) networkConnection.postValue(Resource.Success(result))
            else networkConnection.postValue(Resource.Error("Connection not available"))
        }
    }
}