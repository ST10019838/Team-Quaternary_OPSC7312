package com.example.bot_lobby.services

import com.example.bot_lobby.api.RetrofitInstance
import com.example.bot_lobby.api.UserApi
import com.example.bot_lobby.models.User
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

class LoginService(private val userApi: UserApi) {
    suspend fun login(username: String, password: String): Response<User> {
        val response = userApi.login(RetrofitInstance.apiKey, username, password)
        return if (response.isSuccessful && response.body() != null && response.body()!!
                .isNotEmpty()
        ) {
            // Return the first user found in the list
            Response.success(response.body()!![0])
        } else {
            // If no users found or an error occurred, return an error response
            Response.error(
                response.code(),
                response.errorBody() ?: "User not found".toResponseBody(null)
            )
        }
    }
}
