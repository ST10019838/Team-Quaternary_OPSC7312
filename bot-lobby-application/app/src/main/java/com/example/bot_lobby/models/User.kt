package com.example.bot_lobby.models


data class User(
    val id: Int? = null,           // Assuming the ID is auto-generated
    val role: Int = 1,               // The type_id references user type (User, Admin, etc.)
    val bio: String? = null,
    val username: String,               // Username of the user
    val password: String? = null,               // Password of the user (may want to consider encryption later)
    val biometrics: String? = null,     // Nullable in case biometrics are not provided
    val teamIds: List<Int>?,
    var isPublic: Boolean = true,
    var isLFT: Boolean = true
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