package com.adevinta.randomusers.common.network

import com.adevinta.randomusers.common.data.networkmodels.UsersResultsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RandomUsersApiService {

    @GET("api/")
    suspend fun getRandomUsersByPages(
        @Query("page") page: Int,
        @Query("results") results: Int = 10
    ): Response<UsersResultsResponse>

}