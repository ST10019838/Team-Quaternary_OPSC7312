package com.example.bot_lobby.api

import com.example.bot_lobby.models.AuthRequest
import com.example.bot_lobby.models.AuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {

    @POST("/auth/v1/token?grant_type=password")
    suspend fun login(
        @Header("apikey") key: String,
        @Body request: AuthRequest
    ): Response<AuthResponse>

    @POST("/auth/v1/signup")
    suspend fun register(
        @Header("apikey") key: String,
        @Body request: AuthRequest
    ): Response<AuthResponse>
}