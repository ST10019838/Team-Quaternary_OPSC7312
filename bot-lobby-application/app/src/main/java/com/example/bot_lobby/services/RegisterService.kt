package com.example.bot_lobby.services

import com.example.bot_lobby.api.RetrofitInstance
import com.example.bot_lobby.api.UserApi
import com.example.bot_lobby.models.User
import retrofit2.Response

class RegisterService(private val userApi: UserApi) {
    suspend fun register(newUser: User): Response<User> {
        return userApi.register(RetrofitInstance.apiKey, newUser)
    }
}