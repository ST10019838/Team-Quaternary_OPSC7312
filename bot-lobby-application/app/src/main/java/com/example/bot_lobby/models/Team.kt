package com.example.bot_lobby.models

import java.util.UUID

// Team data class
data class Team(
    var id: UUID? = null,
    val tag: String,
    val name: String,
    val userIdsAndRoles: List<IdAndRole>,// Map<Int, String>, //List<Pair<Int, String>>
    val isPublic: Boolean = true,
    val isOpen: Boolean = true,
    val isLFM: Boolean = true,
    val maxNumberOfUsers: Int = 10,
    val bio: String? = null
)

data class IdAndRole(
    val id: Int,
    val role: String
)

data class TeamInsert(
    var name: String,
    var tag: String
)

