package com.adevinta.randomusers.allusers.factory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.adevinta.randomusers.allusers.repository.AllUsersRepository
import com.adevinta.randomusers.common.data.networkmodels.InfoResponse
import com.adevinta.randomusers.common.data.networkmodels.UsersResultsResponse
import com.adevinta.randomusers.utils.TestCoroutineRule
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AllUsersFactoryUnitTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var repository: AllUsersRepository
    private lateinit var factory: AllUsersFactory
    private val usersResultsResponse = UsersResultsResponse(
        listOf(),
        InfoResponse("", 10, 1, "")
    )

    @Before
    fun setup() {
        repository = mock()
        factory = AllUsersFactoryImpl(repository)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getAllUsersByPage_shouldGetUsersFromRepo() {
        testCoroutineRule.runBlockingTest {
            doReturn(usersResultsResponse)
                .`when`(repository)
                .getPagedUsersFormNetwork(1)
            factory.getPagedUsersFormNetwork(1)
            verify(repository).getPagedUsersFormNetwork(1)
        }
    }

}