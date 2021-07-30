package com.adevinta.randomusers.singleuser.repository

import com.adevinta.randomusers.common.data.databasemodels.entity.UserEntity
import com.adevinta.randomusers.common.database.AllUsersDataBase
import io.reactivex.Single

class SingleUserRepositoryImpl(
    private val database: AllUsersDataBase
) : SingleUserRepository {

    override fun getUserFromDatabase(userUuid: String): Single<UserEntity> {
        return database.allUsersDao().getUser(userUuid)
    }
}