package com.adevinta.randomusers.allusers.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.adevinta.randomusers.allusers.factory.AllUsersFactory
import com.adevinta.randomusers.allusers.model.Info
import com.adevinta.randomusers.allusers.model.UsersResult
import com.adevinta.randomusers.allusers.repository.AllUsersRepository
import com.adevinta.randomusers.common.utils.Resource
import com.adevinta.randomusers.utils.TestCoroutineRule
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import io.mockk.every
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AllUsersViewModelTest {

    @get:Rule
    val testExecutor = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var factory: AllUsersFactory
    private lateinit var repository : AllUsersRepository
    private lateinit var viewModel : AllUsersViewModel
    private lateinit var observer: Observer<Resource<UsersResult>>
    private lateinit var observerDatabase: Observer<UsersResult>

    private val usersResult = UsersResult(
        listOf(),
        Info(1)
    )

    @ExperimentalCoroutinesApi
    @Before
    fun setup(){
        factory = mock()
        observer = mock()
        repository = mock()
        viewModel = AllUsersViewModel(repository, factory)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getAllUsers_shouldReturnSuccess(){
        testCoroutineRule.runBlockingTest {
            doReturn(Resource.Success(usersResult))
                .`when`(factory)
                .getPagedUsersFormNetwork(1)
            viewModel.getAllUsersByPage(1)
            viewModel.usersResult.observeForever(observer)
            verify(factory).getPagedUsersFormNetwork(1)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getAllUsers_shouldReturnError(){
        testCoroutineRule.runBlockingTest {
            doReturn(Resource.Error("Error", null))
                .`when`(factory)
                .getPagedUsersFormNetwork(1)
            viewModel.getAllUsersByPage(1)
            viewModel.usersResult.observeForever(observer)
            verify(factory).getPagedUsersFormNetwork(1)
        }
    }


    @Test
    fun getAllUsersFromDatabase_shouldReturnSuccess(){
        viewModel.getAllUsersFromDataBase(1)
        verify(factory).getPagedUsersFromDatabase(1)
    }


}