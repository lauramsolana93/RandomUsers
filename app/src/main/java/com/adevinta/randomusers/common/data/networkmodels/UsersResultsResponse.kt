package com.adevinta.randomusers.common.data.networkmodels

import com.google.gson.annotations.SerializedName

data class UsersResultsResponse(
    @SerializedName("results") var resultsUsers: List<UserResponse>,
    @SerializedName("info") var info: InfoResponse
)

data class InfoResponse(
    @SerializedName("seed") var seed: String,
    @SerializedName("results") var results: Int,
    @SerializedName("page") var page: Int,
    @SerializedName("version") var version: String
)