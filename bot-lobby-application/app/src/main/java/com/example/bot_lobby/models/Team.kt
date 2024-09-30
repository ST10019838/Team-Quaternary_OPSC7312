package com.example.bot_lobby.models

import java.util.UUID

// Team data class
data class Team(
    var id: UUID? = null,
    val tag: String,
    val name: String,
    val userIdsAndRoles: List<IdAndRole>,// Map<Int, String>, //List<Pair<Int, String>>
    val isPublic: Boolean = true,
    val maxNumberOfUsers: Int = 10
)

data class IdAndRole(
    val id: Int,
    val role: String
)

data class TeamInsert(
    var name: String,
    var tag: String
)

