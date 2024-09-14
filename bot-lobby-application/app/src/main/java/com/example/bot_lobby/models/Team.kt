package com.example.bot_lobby.models

data class Team(
    var id: Int,
    var name: String,
    var tag: String
)

data class TeamInsert(
    var name: String,
    var tag: String
)