package com.example.bot_lobby.models

data class UserInsert(
    val type_id: Int,           // This will be set to default to 1 for normal users or another value if specified
    val username: String,
    val password: String,
    val biometrics: String?,    // Nullable in case biometrics are not provided
    val firstname: String,
    val lastname: String,
    val age: Int
)