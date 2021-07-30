package com.adevinta.randomusers.common.data.databasemodels.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deleteTable")
data class DeleteEntity(
    @PrimaryKey var uuid: String
)