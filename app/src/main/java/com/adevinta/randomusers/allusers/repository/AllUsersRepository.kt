package com.adevinta.randomusers.allusers.repository

import com.adevinta.randomusers.common.data.databasemodels.entity.DeleteEntity
import com.adevinta.randomusers.common.data.databasemodels.entity.UserEntity
import com.adevinta.randomusers.common.data.networkmodels.UsersResultsResponse
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Response

interface AllUsersRepository {

    suspend fun getPagedUsersFormNetwork(page: Int): Response<UsersResultsResponse>
    fun getPagedUsersFromDatabase(page: Int): Single<List<UserEntity>>
    fun deleteUserFromDatabase(user: UserEntity): Completable
    fun addDeleteUsersToDataBase(delete: DeleteEntity): Completable
    fun saveUsersToDatabase(users: List<UserEntity>): Completable
    fun getDeleteUsers(): Single<List<DeleteEntity>>

}