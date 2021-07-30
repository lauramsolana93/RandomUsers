package com.adevinta.randomusers.splash.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.adevinta.randomusers.splash.factory.SplashFactory

class SplashViewModelFactory(
    private val splashFactory: SplashFactory
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SplashViewModel(factory = splashFactory) as T
    }

}