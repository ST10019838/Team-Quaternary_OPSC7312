package com.example.bot_lobby.models


data class User(
    val user_id: Int? = null,           // Assuming the ID is auto-generated
    val user_role: Int = 1,               // The type_id references user type (User, Admin, etc.)
    val user_bio: String? = null,
    val username: String,               // Username of the user
    val password: String,               // Password of the user (may want to consider encryption later)
    val biometrics: String? = null,     // Nullable in case biometrics are not provided
    val firstname: String,              // User's first name
    val lastname: String,               // User's last name
    val age: Int                        // User's age
)

data class UserInsert(
    val type_id: Int,           // This will be set to default to 1 for normal users or another value if specified
    val username: String,
    val password: String,
    val biometrics: String?,    // Nullable in case biometrics are not provided
    val firstname: String,
    val lastname: String,
    val age: Int
)

data class UserRequest(
    val user_id: Int,
    val type_id: Int,
    val username: String,
    val password: String
)