package com.example.bot_lobby.models

// Team data class
data class Team(
    val name: String,
    val members: List<Member>,
    val isPublic: Boolean
)

// Member data class
data class Member(
    val name: String,
    val role: String
)

// TeamInsert data class for inserting new teams
data class TeamInsert(
    val name: String,
    val tag: String
)
