package com.adevinta.randomusers.common.data.networkmodels

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("gender") var gender: String,
    @SerializedName("name") var nameResponse: NameResponse,
    @SerializedName("location") var locationResponse: LocationResponse,
    @SerializedName("email") var email: String,
    @SerializedName("login") var loginResponse: LoginResponse,
    @SerializedName("dob") var dob: DobResponse,
    @SerializedName("registered") var registered: RegistredResponse,
    @SerializedName("phone") var phone: String,
    @SerializedName("cell") var cell: String,
    @SerializedName("id") var id: IdResponse,
    @SerializedName("picture") var pictureResponse: PictureResponse,
    @SerializedName("nat") var nat: String

)

data class NameResponse(
    @SerializedName("title") var title: String,
    @SerializedName("first") var first: String,
    @SerializedName("last") var last: String
)

data class CoordiantesResponse(
    @SerializedName("latitude") var latitude: String,
    @SerializedName("longitude") var longitude: String
)

data class TimeZoneResponse(
    @SerializedName("offset") var offset: String,
    @SerializedName("description") var description: String
)

data class LocationResponse(
    @SerializedName("street") var street: StreetResponse,
    @SerializedName("city") var city: String,
    @SerializedName("state") var state: String,
    @SerializedName("postcode") var postcode: String,
    @SerializedName("coordinates") var coordinates: CoordiantesResponse,
    @SerializedName("timezone") var timezone: TimeZoneResponse
)

data class StreetResponse(
    @SerializedName("number") var number: Long,
    @SerializedName("name") var name: String
)

data class LoginResponse(
    @SerializedName("uuid") var uuid: String,
    @SerializedName("username") var username: String,
    @SerializedName("password") var password: String,
    @SerializedName("salt") var salt: String,
    @SerializedName("md5") var md5: String,
    @SerializedName("sha1") var sha1: String,
    @SerializedName("sha256") var sha256: String
)

data class DobResponse(
    @SerializedName("date") var date: String,
    @SerializedName("age") var age: String
)

data class RegistredResponse(
    @SerializedName("date") var date: String,
    @SerializedName("age") var age: String
)

data class IdResponse(
    @SerializedName("name") var name: String,
    @SerializedName("value") var value: String
)

data class PictureResponse(
    @SerializedName("large") var large: String,
    @SerializedName("medium") var medium: String,
    @SerializedName("thumbnail") var thumbnail: String
)