package com.example.bot_lobby.models

// Data class representing an announcement
data class Announcement(
    val teamTag: String,        // Tag for the team
    val playerTag: String,      // Tag for the player (based on Player model)
    val description: String     // Description of the announcement
)
