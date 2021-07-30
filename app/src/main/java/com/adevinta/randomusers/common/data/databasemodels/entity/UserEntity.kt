package com.adevinta.randomusers.common.data.databasemodels.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @ColumnInfo @PrimaryKey(autoGenerate = true) var id: Int? = null,
    @ColumnInfo(name = "uuid") var uuid: String,
    @ColumnInfo(name = "nameSurname") var nameSurname: String,
    @ColumnInfo(name = "email") var email: String,
    @ColumnInfo(name = "picture") var picture: String,
    @ColumnInfo(name = "phone") var phone: String,
    @ColumnInfo(name = "gender") var gender: String,
    @ColumnInfo(name = "city") var city: String,
    @ColumnInfo(name = "state") var state: String,
    @ColumnInfo(name = "number") var number: Long,
    @ColumnInfo(name = "streetName") var streetName: String,
    @ColumnInfo(name = "registerDate") var registerDate: String,
    @ColumnInfo(name = "registerAge") var registerAge: String
)