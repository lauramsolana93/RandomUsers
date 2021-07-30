package com.adevinta.randomusers.allusers.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.adevinta.randomusers.common.data.databasemodels.entity.DeleteEntity
import com.adevinta.randomusers.common.data.databasemodels.entity.UserEntity
import com.adevinta.randomusers.common.data.networkmodels.InfoResponse
import com.adevinta.randomusers.common.data.networkmodels.UsersResultsResponse
import com.adevinta.randomusers.common.database.AllUsersDataBase
import com.adevinta.randomusers.common.network.RandomUsersApiService
import com.adevinta.randomusers.utils.TestCoroutineRule
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response


class AllUsersRepositoryTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var repository: AllUsersRepository
    private lateinit var repositoryRx: AllUsersRepository

    private lateinit var database: AllUsersDataBase
    private lateinit var api: RandomUsersApiService

    private var databaseRx = mockk<AllUsersDataBase>()
    private var apiRx = mockk<RandomUsersApiService>()
    private val usersResultsResponse = UsersResultsResponse(
        listOf(),
        InfoResponse("", 10, 1, "")
    )
    private val userEntity = UserEntity(
        1, "", "", "", "", "",
        "", "", "", 1, "", "", ""
    )


    @Before
    fun setup() {
        api = mock()
        database = mock()
        repository = AllUsersRepositoryImpl(api, database)
        repositoryRx = AllUsersRepositoryImpl(apiRx, databaseRx)
    }


    @ExperimentalCoroutinesApi
    @Test
    fun getAllUsersFromServer_shouldReturnAllUsers() {
        testCoroutineRule.runBlockingTest {
            whenever(api.getRandomUsersByPages(1)).thenReturn(Response.success(usersResultsResponse))
            api.getRandomUsersByPages(1)
            verify(api).getRandomUsersByPages(1)
        }

    }

    @Test
    fun getAllUsersFromDataBase_shouldReturnAllUsers() {
        every { repositoryRx.getPagedUsersFromDatabase(1) } returns Single.just(listOf(userEntity))
    }

    @Test
    fun addDeleteUser_shouldAddDeleteUser() {
        every { repositoryRx.getDeleteUsers() } returns Single.just(listOf(DeleteEntity("")))
    }

    @Test
    fun deleteFromDatabase_shouldDeleteUser() {
        repositoryRx.deleteUserFromDatabase(userEntity).test()
    }

    @Test
    fun addUsersToDatabase_shouldAddUsers() {
        repositoryRx.saveUsersToDatabase(listOf(userEntity)).test()
    }

    @Test
    fun addDeletesUsersToDatabase_shouldAddDeletedUser() {
        repositoryRx.addDeleteUsersToDataBase(DeleteEntity("")).test()
    }

}