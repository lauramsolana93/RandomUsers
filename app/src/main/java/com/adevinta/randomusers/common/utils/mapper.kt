package com.adevinta.randomusers.common.utils

import com.adevinta.randomusers.allusers.model.*
import com.adevinta.randomusers.common.data.databasemodels.entity.DeleteEntity
import com.adevinta.randomusers.common.data.databasemodels.entity.UserEntity
import com.adevinta.randomusers.common.data.networkmodels.LocationResponse
import com.adevinta.randomusers.common.data.networkmodels.RegistredResponse
import com.adevinta.randomusers.common.data.networkmodels.StreetResponse
import com.adevinta.randomusers.common.data.networkmodels.UserResponse

fun UserResponse.mapToUser(): User {
    return User(
        uuid = this.loginResponse.uuid,
        nameSurname = "${this.nameResponse.first} ${this.nameResponse.last}",
        email = this.email,
        picture = this.pictureResponse.medium,
        phone = this.phone,
        gender = this.gender,
        location = this.locationResponse.mapToLocation(),
        registerDate = this.registered.mapToRegister()
    )
}

private fun RegistredResponse.mapToRegister(): Registerd {
    return Registerd(
        date = this.date,
        age = this.age
    )

}

private fun LocationResponse.mapToLocation(): Location {
    return Location(
        street = this.street.mapToStreet(),
        city = this.city,
        state = this.state
    )
}

private fun StreetResponse.mapToStreet(): Street {
    return Street(
        number = this.number,
        name = this.name
    )
}

fun User.mapToUserEntity(): UserEntity {
    return UserEntity(
        id = this.id,
        uuid = this.uuid,
        nameSurname = this.nameSurname,
        email = this.email,
        picture = this.picture,
        phone = this.phone,
        gender = this.gender,
        city = this.location.city,
        state = this.location.state,
        number = this.location.street.number,
        streetName = this.location.street.name,
        registerDate = this.registerDate.date,
        registerAge = this.registerDate.age
    )
}


fun UserEntity.mapToUser(): User {
    return User(
        id = this.id,
        uuid = this.uuid,
        nameSurname = this.nameSurname,
        email = this.email,
        picture = this.picture,
        phone = this.phone,
        gender = this.gender,
        location = setLocation(this),
        registerDate = Registerd(this.registerDate, this.registerAge)

    )
}

private fun setLocation(user: UserEntity): Location {
    return Location(
        street = Street(user.number, user.streetName),
        city = user.city,
        state = user.state
    )
}

fun List<UserEntity>.mapToUserList(): List<User> {
    val userList = ArrayList<User>()
    this.forEach {
        userList.add(it.mapToUser())
    }
    userList.distinct()
    userList.sortedBy {
        it.id
    }
    return userList
}

fun List<DeleteEntity>.mapToListOfDelete(): List<Delete> {
    val listDelete = ArrayList<Delete>()
    this.forEach {
        listDelete.add(it.mapToDelete())
    }
    return listDelete
}

fun DeleteEntity.mapToDelete(): Delete {
    return Delete(uuid = this.uuid)
}

fun Delete.mapToDeleteEntity(): DeleteEntity {
    return DeleteEntity(this.uuid)
}