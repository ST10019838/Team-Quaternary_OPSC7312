package com.example.bot_lobby.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    val client: Retrofit = Retrofit.Builder()
        .baseUrl("https://ynsntpgpunobawajnbow.supabase.co")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}