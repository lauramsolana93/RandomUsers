package com.adevinta.randomusers.common.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.adevinta.randomusers.common.data.databasemodels.entity.DeleteEntity
import io.reactivex.Single

@Dao
interface DeleteUsersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addDeleteUser(uuid: DeleteEntity)

    @Query("SELECT * FROM deleteTable")
    fun getAllDelete(): Single<List<DeleteEntity>>

}