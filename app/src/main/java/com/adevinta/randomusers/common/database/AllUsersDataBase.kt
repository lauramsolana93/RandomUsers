package com.adevinta.randomusers.common.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.adevinta.randomusers.common.data.databasemodels.entity.DeleteEntity
import com.adevinta.randomusers.common.data.databasemodels.entity.UserEntity
import com.adevinta.randomusers.common.database.dao.AllUsersDao
import com.adevinta.randomusers.common.database.dao.DeleteUsersDao

@Database(
    entities = [
        UserEntity::class,
        DeleteEntity::class],
    version = 1,
    exportSchema = false

)

abstract class AllUsersDataBase : RoomDatabase() {
    abstract fun allUsersDao(): AllUsersDao
    abstract fun deletedUsersDao(): DeleteUsersDao
}