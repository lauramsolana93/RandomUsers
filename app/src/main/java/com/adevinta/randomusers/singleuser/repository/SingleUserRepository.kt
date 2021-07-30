package com.adevinta.randomusers.singleuser.repository

import com.adevinta.randomusers.common.data.databasemodels.entity.UserEntity
import io.reactivex.Single

interface SingleUserRepository {

    fun getUserFromDatabase(userUuid: String): Single<UserEntity>

}