package com.example.bot_lobby.models

// Team data class
data class Team(
    val teamtag: String,
    val teamname: String,
    val members: List<Member>,
    val isPublic: Boolean
)

// Member data class
data class Member(
    val playertag: String,
    val role: String
)

