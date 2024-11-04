package com.example.bot_lobby.models

import java.util.UUID


data class User(
    val id: Int? = null,           // Assuming the ID is auto-generated
    val role: Int = 1,               // The type_id references user type (User, Admin, etc.)
    val bio: String? = null,
    val username: String,               // Username of the user
    val password: String? = null,       // Password of the user (may want to consider encryption later)
    val biometrics: String? = null,     // Nullable in case biometrics are not provided
    var teamIds: List<UUID>? = null,
    var isPublic: Boolean = true,
    var isLFT: Boolean = true,
    var email: String? = null
)