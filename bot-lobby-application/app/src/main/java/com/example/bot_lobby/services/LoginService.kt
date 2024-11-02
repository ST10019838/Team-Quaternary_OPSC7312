package com.example.bot_lobby.services

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Response
import com.example.bot_lobby.api.RetrofitInstance
import com.example.bot_lobby.api.UserApi
import com.example.bot_lobby.models.User

class LoginService(private val userApi: UserApi) : AppCompatActivity() {
    suspend fun login(username: String, password: String): Response<List<User>> {
        val usernameQuery = "eq.$username"
        val passwordQuery = "eq.$password"

        Log.i("IDK", username)
        Log.i("IDK", password)

        val response = userApi.login(RetrofitInstance.apiKey, usernameQuery, passwordQuery)

        // Check if the response is successful
        if (response.isSuccessful) {
            Log.i("IDK", response.body().toString())
            return response
        } else {
            // Handle the error response
            Log.e("LoginService", "Error: ${response.errorBody()?.string()}")
            throw IllegalArgumentException("Login failed with status code: ${response.code()}")
        }
    }
}