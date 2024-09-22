package com.example.bot_lobby.models

data class UserRequest(
    val user_id: Int,
    val type_id: Int,
    val username: String,
    val password: String
)