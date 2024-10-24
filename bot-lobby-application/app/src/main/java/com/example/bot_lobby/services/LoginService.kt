package com.example.bot_lobby.services

import android.util.Log
import com.example.bot_lobby.api.RetrofitInstance
import com.example.bot_lobby.api.UserApi
import com.example.bot_lobby.models.User
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

class LoginService(private val userApi: UserApi) {
    suspend fun login(username: String, password: String): Response<List<User>> {
        val response = userApi.login(RetrofitInstance.apiKey, username, password)

        // Check if the response is successful
        if (response.isSuccessful) {
            Log.d("LoginService", "Login successful!")
            // Handle the successful response here
            return response
        } else {
            // Handle the error response
            Log.e("LoginService", "Error: ${response.errorBody()?.string()}")
            throw IllegalArgumentException("Login failed with status code: ${response.code()}")
        }
    }
}
