package com.adevinta.randomusers.singleuser.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.adevinta.randomusers.allusers.model.Location
import com.adevinta.randomusers.allusers.model.Registerd
import com.adevinta.randomusers.allusers.model.Street
import com.adevinta.randomusers.allusers.model.User
import com.adevinta.randomusers.common.utils.Resource
import com.adevinta.randomusers.singleuser.factory.SingleUserFactory
import com.adevinta.randomusers.singleuser.repository.SingleUserRepository
import com.adevinta.randomusers.utils.TestCoroutineRule
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import io.mockk.every
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SingleUserViewModelTest {

    @get:Rule
    val testExecutor = InstantTaskExecutorRule()


    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var factory: SingleUserFactory
    private lateinit var viewModel: SingleUserViewModel
    private lateinit var observer : Observer<Resource<User>>

    private val user = User(
        1,
        "",
        "",
        "",
        "",
        "",
        "",
        Location(Street(1, ""), "", ""),
        Registerd("", "")
    )


    @Before
    fun setup(){
        factory = mock()
        observer = mock()
        viewModel = SingleUserViewModel(factory)

    }

    @Test
    fun getUserFromDatabase_shouldReturnUser(){
        viewModel.getUserFromDataBase("")
        verify(factory).getUserFromDatabase("")
    }

}