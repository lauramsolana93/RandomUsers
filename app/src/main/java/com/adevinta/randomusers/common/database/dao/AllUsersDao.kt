package com.adevinta.randomusers.common.database.dao

import androidx.room.*
import com.adevinta.randomusers.common.data.databasemodels.entity.UserEntity
import io.reactivex.Single


@Dao
interface AllUsersDao {

    @Query("SELECT * FROM user limit :limit offset (:page)*:limit")
    fun getAllUsers(page: Int, limit: Int = 10): Single<List<UserEntity>>

    @Query("SELECT * FROM user WHERE uuid = :uuid")
    fun getUser(uuid: String): Single<UserEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUsers(users: List<UserEntity>)

    @Delete
    fun deleteUser(user: UserEntity)

}