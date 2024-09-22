package com.example.bot_lobby

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*

object KtorApiClient {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json() // Use Kotlinx for JSON serialization/deserialization
        }
    }
}