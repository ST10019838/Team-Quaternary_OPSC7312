package com.example.bot_lobby

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.gson.*

// Object to configure and provide a singleton HttpClient instance
object ApiClient {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            gson() // Use Gson for JSON serialization/deserialization
        }
    }
}
