package com.example.bot_lobby.api

import com.example.bot_lobby.models.AuthRequest
import com.example.bot_lobby.models.AuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {

    @POST("token")
    suspend fun login(
        @Header("apikey") key: String,
        @Body request: AuthRequest // Change to use the request body, not query parameter
    ): Response<AuthResponse>

    @POST("signup")
    suspend fun register(
        @Header("apikey") key: String,
        @Body request: AuthRequest
    ): Response<AuthResponse>
}