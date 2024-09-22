package com.example.bot_lobby.models
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val user_id: Int? = null,           // Assuming the ID is auto-generated
    val type_id: Int = 1,               // The type_id references user type (User, Admin, etc.)
    val username: String,               // Username of the user
    val password: String,               // Password of the user (may want to consider encryption later)
    val biometrics: String? = null,     // Nullable in case biometrics are not provided
    val firstname: String,              // User's first name
    val lastname: String,               // User's last name
    val age: Int                        // User's age
)