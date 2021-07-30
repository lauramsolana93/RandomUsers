package com.adevinta.randomusers.splash.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.adevinta.randomusers.common.utils.Resource
import com.adevinta.randomusers.splash.factory.SplashFactory
import com.adevinta.randomusers.utils.TestCoroutineRule
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SplashViewModelTest {

    @get:Rule
    val testExecutor = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var factory: SplashFactory
    private lateinit var viewmodel: SplashViewModel
    private lateinit var observer: Observer<Resource<Boolean>>

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        factory = mock()
        observer = mock()
        viewmodel = SplashViewModel(factory)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun checkNetworkConnection_shouldReturnSuccess() {
        testCoroutineRule.runBlockingTest {
            doReturn(Resource.Success(true))
                .`when`(factory)
                .checkNetworkConnection(mock())
            viewmodel.networkConnection.observeForever(observer)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun checkConnectivityNetwork_shouldReturnError() {
        testCoroutineRule.runBlockingTest {
            doReturn(Resource.Error("Error", null))
                .`when`(factory)
                .checkNetworkConnection(mock())
            viewmodel.networkConnection.observeForever(observer)
        }
    }

}