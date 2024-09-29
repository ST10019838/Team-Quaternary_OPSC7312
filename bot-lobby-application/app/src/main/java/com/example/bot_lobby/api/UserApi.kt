package com.example.bot_lobby.api

import com.example.bot_lobby.models.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface UserApi {

    @GET("users")  // Shouldn't have "rest/v1" here since it's part of the base URL
    suspend fun getUsers(@Header("apikey") key: String): Response<List<User>>

    @POST("users")
    suspend fun createUser(@Header("apikey") key: String, @Body user: User): Response<User>

    @PATCH("users")
    suspend fun updateUser(
        @Header("apikey") key: String,
        @Query("id") id: String,
        @Body user: User
    ): Response<User>

    @DELETE("users")
    suspend fun deleteUser(@Header("apikey") key: String, @Query("id") id: String): Response<User>
}