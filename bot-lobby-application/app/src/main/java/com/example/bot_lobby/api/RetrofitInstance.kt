package com.example.bot_lobby.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val apiKey =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InZud2Jiemd1aG5ra3JncnpucGR0Iiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTcyNTUyMjA1NywiZXhwIjoyMDQxMDk4MDU3fQ.n7oDojo_o1YPSmutTliymByx08KWoZLPA48uax2yQdM"

//    private const val BASE_URL =
//        "https://android-kotlin-fun-mars-server.appspot.com"

    private const val BASE_URL =
        "https://vnwbbzguhnkkrgrznpdt.supabase.co/rest/v1/"

    val Api by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TeamApi::class.java)
    }
}