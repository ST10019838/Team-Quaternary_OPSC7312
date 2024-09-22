package com.example.bot_lobby

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface SupabaseService {
    @POST("auth/v1/signup")
    suspend fun signUp(
        @Header("apikey") apikey: String = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Inluc250cGdwdW5vYmF3YWpuYm93Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3MjQ1MjAyNjAsImV4cCI6MjA0MDA5NjI2MH0.LpyqZLqJp8bgSOXd3Tuw9Nntq60WJ4-GtAGK9bbJko8",
        @Header("Content-Type") contentType: String = "application/json",
        @Body body: Map<String, String>
    ): Response<Any> // Specify the response type

    @POST("auth/v1/token?grant_type=password")
    suspend fun logIn(
        @Header("apikey") apikey: String = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Inluc250cGdwdW5vYmF3YWpuYm93Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3MjQ1MjAyNjAsImV4cCI6MjA0MDA5NjI2MH0.LpyqZLqJp8bgSOXd3Tuw9Nntq60WJ4-GtAGK9bbJko8",
        @Header("Content-Type") contentType: String = "application/json",
        @Body body: Map<String, String>
    ): Response<Any> // Specify the response type
}