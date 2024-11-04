package com.example.bot_lobby.view_models

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bot_lobby.models.Announcement
import com.example.bot_lobby.models.Team
import com.example.bot_lobby.models.User
import com.example.bot_lobby.services.PushNotificationService
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AnnouncementViewModel(context: Context) : ViewModel() {
    private val context by lazy { context }

    // List to store announcements
    private val _announcements = mutableListOf<Announcement>()
    val announcements: List<Announcement> get() = _announcements

    // State variable to track if an announcement was recently saved
    var announcementSaved = mutableStateOf(false)
        private set

    init {
        initializeFirebase()
    }

    private fun initializeFirebase() {
        // Ensure Firebase is initialized before subscribing to topics
        if (FirebaseApp.getApps(context).isNotEmpty()) {
            FirebaseMessaging.getInstance().subscribeToTopic("new_announcements")
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        // Handle subscription failure if needed
                    }
                }
        } else {
            // Log an error or handle initialization failure
            println("Firebase is not initialized")
        }
    }

    // Save an announcement and trigger a push notification
    fun saveAnnouncement(title: String, content: String, team: Team, currentUserId: User) {
        val newAnnouncement = Announcement(
            team = team,
            title = title,
            content = content,
            dateCreated = Date(), // Set current date and time
            createdByUserId = currentUserId // Set to the ID of the current user
        )

        println("saveAnnouncement called for: ${newAnnouncement.title}")  // Debug line
        _announcements.add(newAnnouncement)
        announcementSaved.value = true // Set to true to indicate a successful save

        // Trigger push notification for the new announcement
        sendPushNotification(newAnnouncement)
    }

    // Reset the save status (e.g., call this after showing a confirmation message)
    fun resetAnnouncementSaved() {
        announcementSaved.value = false
    }

    // Function to send push notification using Firebase Cloud Messaging
    private fun sendPushNotification(announcement: Announcement) {
        if (FirebaseApp.getApps(context).isNotEmpty()) {
            viewModelScope.launch {
                // Adding extra spaces to approximate centering
                val notificationContent = """
                ${announcement.content}
                """.trimIndent()

                PushNotificationService.showLocalNotification(
                    context = context,
                    title = "New Announcement: ${announcement.title}",
                    message = notificationContent,
                    dateCreated = SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss",
                        Locale.getDefault()
                    ).format(announcement.dateCreated),  // Avoid duplication in the notification
                    createdByUserId = "${announcement.createdByUserId}"
                )
            }
        } else {
            // Handle Firebase unavailability
            println("Firebase is not initialized")
        }
    }


}
