package com.example.bot_lobby.models

import java.util.Date

// Data class representing an announcement
data class Announcement(
    val team: String,           // Team associated with the announcement
    val title: String,
    val content: String,
    val dateCreated: Date,
    val createdByUserId: String
)

