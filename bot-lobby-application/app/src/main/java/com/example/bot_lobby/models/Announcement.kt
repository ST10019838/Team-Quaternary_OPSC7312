package com.example.bot_lobby.models

import java.time.LocalDate
import java.util.Date

// Data class representing an announcement
data class Announcement(
    val team: Team,           // Team associated with the announcement
    val title: String,
    val content: String,
    val dateCreated: Date,
    val createdByUserId: User
)

