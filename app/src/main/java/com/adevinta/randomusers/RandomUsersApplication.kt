package com.adevinta.randomusers

import android.app.Application
import com.adevinta.randomusers.di.injectModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class RandomUsersApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initDi()
        injectModule()
    }

    private fun initDi() {
        startKoin {
            androidContext(this@RandomUsersApplication)
        }
    }

}


