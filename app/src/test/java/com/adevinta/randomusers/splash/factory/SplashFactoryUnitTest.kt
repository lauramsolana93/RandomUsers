package com.adevinta.randomusers.splash.factory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.adevinta.randomusers.common.utils.ConnectivityHelper
import com.adevinta.randomusers.utils.TestCoroutineRule
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SplashFactoryUnitTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var factory: SplashFactory
    private lateinit var connectivityHelper: ConnectivityHelper

    @Before
    fun setup(){
        connectivityHelper = mock()
        factory = SplashFactoryImpl()

    }

    @ExperimentalCoroutinesApi
    @Test
    fun checkConnectivityNetwork_shouldCheckConnectivityFromHelper(){
        testCoroutineRule.runBlockingTest {
            doReturn(true)
                .`when`(connectivityHelper)
                .isConnectedToNetwork(mock())
            factory.checkNetworkConnection(mock())
        }
    }



}