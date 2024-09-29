package com.example.bot_lobby.models

// Team data class
data class Team(
    var id: Int,
    val tag: String,
    val name: String,
    val members: List<Member>,
    val isPublic: Boolean
)

// Member data class
data class Member(
    val playertag: String,
    val role: String
)

data class TeamInsert(
    var name: String,
    var tag: String
)

