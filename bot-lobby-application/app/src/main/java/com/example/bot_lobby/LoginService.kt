package com.example.bot_lobby

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.util.Log

class LoginService(private val client: HttpClient) {
    private val baseUrl = "https://ynsntpgpunobawajnbow.supabase.co/rest/v1/users"
    private val apiKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Inluc250cGdwdW5vYmF3YWpuYm93Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3MjQ1MjAyNjAsImV4cCI6MjA0MDA5NjI2MH0.LpyqZLqJp8bgSOXd3Tuw9Nntq60WJ4-GtAGK9bbJko8" // Replace with your actual anon API key

    suspend fun login(username: String, password: String): String {
        return withContext(Dispatchers.IO) {
            try {
                val response: HttpResponse = client.post(baseUrl) {
                    headers {
                        append(HttpHeaders.Authorization, "Bearer $apiKey")
                        append("apikey", apiKey) // Explicitly add apikey header
                        append(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    }
                    setBody(
                        mapOf(
                            "username" to username, // Use lowercase for consistency
                            "password" to password
                        )
                    )
                }

                // Log response details for debugging
                Log.d("LoginService", "Response Status: ${response.status}")
                Log.d("LoginService", "Response Body: ${response.bodyAsText()}")
                response.bodyAsText()
            } catch (e: Exception) {
                // Log detailed error information
                Log.e("LoginService", "Login failed", e)
                val errorMessage = "Error: ${e.message}"
                Log.d("LoginService", "Full Error Details: ${e.localizedMessage}")
                errorMessage
            }
        }
    }
}