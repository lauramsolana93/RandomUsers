package com.adevinta.randomusers.allusers.model

data class User(
    var id: Int? = null,
    var uuid: String,
    var nameSurname: String,
    var email: String,
    var picture: String,
    var phone: String,
    var gender: String,
    var location: Location,
    var registerDate: Registerd
)