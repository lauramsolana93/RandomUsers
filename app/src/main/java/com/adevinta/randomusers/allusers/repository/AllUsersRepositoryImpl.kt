package com.adevinta.randomusers.allusers.repository

import com.adevinta.randomusers.common.data.databasemodels.entity.DeleteEntity
import com.adevinta.randomusers.common.data.databasemodels.entity.UserEntity
import com.adevinta.randomusers.common.data.networkmodels.UsersResultsResponse
import com.adevinta.randomusers.common.database.AllUsersDataBase
import com.adevinta.randomusers.common.network.RandomUsersApiService
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Response

class AllUsersRepositoryImpl(
    private val api: RandomUsersApiService,
    private val database: AllUsersDataBase,
) : AllUsersRepository {

    override suspend fun getPagedUsersFormNetwork(page: Int): Response<UsersResultsResponse> {
        return api.getRandomUsersByPages(page)
    }

    override fun getPagedUsersFromDatabase(page: Int): Single<List<UserEntity>> {
        return database.allUsersDao().getAllUsers(page)

    }

    override fun deleteUserFromDatabase(user: UserEntity): Completable {
        return Completable.fromAction {
            database.allUsersDao().deleteUser(user)
        }
    }

    override fun saveUsersToDatabase(users: List<UserEntity>): Completable {
        return Completable.fromAction {
            database.allUsersDao().addUsers(users)
        }

    }

    override fun getDeleteUsers(): Single<List<DeleteEntity>> {
        return database.deletedUsersDao().getAllDelete()
    }

    override fun addDeleteUsersToDataBase(delete: DeleteEntity): Completable {
        return Completable.fromAction {
            database.deletedUsersDao().addDeleteUser(delete)
        }
    }


}