package com.example.bot_lobby

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.util.Log
import com.example.bot_lobby.models.User

class RegisterService(private val client: HttpClient) {
    private val baseUrl = "https://ynsntpgpunobawajnbow.supabase.co/auth/v1/signup"
    private val usersTableUrl = "https://ynsntpgpunobawajnbow.supabase.co/rest/v1/users"
    private val apiKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Inluc250cGdwdW5vYmF3YWpuYm93Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3MjQ1MjAyNjAsImV4cCI6MjA0MDA5NjI2MH0.LpyqZLqJp8bgSOXd3Tuw9Nntq60WJ4-GtAGK9bbJko8" // Replace with your actual API key

    suspend fun register(user: User): String {
        return withContext(Dispatchers.IO) {
            try {
                // Step 1: Register the user
                val response: HttpResponse = client.post(baseUrl) {
                    headers {
                        append(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                        append("apikey", apiKey)
                    }
                    setBody(mapOf("email" to user.username, "password" to user.password))
                }

                Log.d("RegisterService", "Response Status: ${response.status.value}")
                Log.d("RegisterService", "Response Body: ${response.bodyAsText()}")

                // Step 2: Insert user data into the users table
                if (response.status.isSuccess()) {
                    insertUserData(user) // No need for user_id
                }

                response.bodyAsText()
            } catch (e: Exception) {
                Log.e("RegisterService", "Registration failed", e)
                "Registration failed: ${e.message}"
            }
        }
    }

    private suspend fun insertUserData(user: User) {
        withContext(Dispatchers.IO) {
            try {
                val response: HttpResponse = client.post(usersTableUrl) {
                    headers {
                        append(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                        append("apikey", apiKey)
                        append("Authorization", "Bearer $apiKey")
                    }
                    setBody(
                        """{
        "username": "${user.username}",
        "password": "${user.password}",
        "type_id": ${user.role},  // Assuming the role corresponds to type_id
        "biometrics": ${user.biometrics?.let { "\"$it\"" } ?: "null"},
        "bio": ${user.bio?.let { "\"$it\"" } ?: "null"},
        "team_ids": ${user.teamIds}
    }"""
                    )

                }

                Log.d("RegisterService", "User data insertion status: ${response.status.value}")
                Log.d("RegisterService", "User data insertion response: ${response.bodyAsText()}")
            } catch (e: Exception) {
                Log.e("RegisterService", "User data insertion failed", e)
            }
        }
    }
}