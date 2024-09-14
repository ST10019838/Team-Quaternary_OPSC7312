package com.example.bot_lobby.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val apiKey = ""
//    private const val BASE_URL =
//        "https://android-kotlin-fun-mars-server.appspot.com"

    private const val BASE_URL =
        ""

    val Api by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TeamApi::class.java)
    }
}