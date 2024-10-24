package com.example.bot_lobby.services

import android.util.Log
import com.example.bot_lobby.api.RetrofitInstance
import com.example.bot_lobby.api.UserApi
import com.example.bot_lobby.models.User
import retrofit2.Response

class RegisterService(private val userApi: UserApi) {
    suspend fun register(newUser: User): Response<User> {
        val response = userApi.register(RetrofitInstance.apiKey, newUser)
        if (response.isSuccessful) {
            Log.d("RegisterService", "Response: ${response.body()?.toString()}")
        } else {
            Log.e("RegisterService", "Error: ${response.errorBody()?.string()}")
        }
        return response
    }
}