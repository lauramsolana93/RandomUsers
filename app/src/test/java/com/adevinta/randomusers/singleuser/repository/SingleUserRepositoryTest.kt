package com.adevinta.randomusers.singleuser.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.adevinta.randomusers.common.data.databasemodels.entity.UserEntity
import com.adevinta.randomusers.common.database.AllUsersDataBase
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SingleUserRepositoryTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: SingleUserRepository
    private var database = mockk<AllUsersDataBase>()
    private val userEntity = UserEntity(
        null, "",
        "", "", "", "",
        "", "", "", 1, "", "", ""

    )

    @Before
    fun setup() {
        repository = SingleUserRepositoryImpl(database)
    }

    @Test
    fun getUserFromDatabase_shouldReturnUser() {
        every { repository.getUserFromDatabase("") } returns Single.just(userEntity)
    }

}