package com.example.bot_lobby.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import com.example.bot_lobby.R

// HomeScreen object implementing the Screen interface from Voyager
object HomeScreen : Screen {

    // Override the Content function to load the composable content for the Home screen
    @Composable
    override fun Content() {
        HomeScreenContent()
    }
}

// Main content for the Home screen, which can be previewed in Android Studio
@Preview
@Composable
fun HomeScreenContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Welcome text
        Text(
            text = "Welcome Back!",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Upcoming events drop-down menu
        UpcomingEvents()

        // Announcements section
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Announcements",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Announcements list display
        AnnouncementsList()
    }
}

// Composable for upcoming events using a dropdown menu
@Composable
fun UpcomingEvents() {
    var expanded by remember { mutableStateOf(false) }
    val events = listOf("Event 1", "Event 2", "Event 3") // Example event data

    // Dropdown to display the list of events
    Box(modifier = Modifier.fillMaxWidth()) {
        TextButton(onClick = { expanded = !expanded }) {
            Text("Upcoming Events", fontSize = 18.sp)
            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
        }

        // Dropdown menu for events
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            events.forEach { event ->
                DropdownMenuItem(
                    text = { Text(event) },
                    onClick = {
                        // Handle event selection if needed
                        expanded = false
                    }
                )
            }
        }
    }
}

// Composable to display the list of announcements
@Composable
fun AnnouncementsList() {
    val announcements = listOf(
        Announcement("Team A", "Player 1", R.drawable.ic_profile_foreground, "Announcement 1"),
        Announcement("Team B", "Player 2", R.drawable.ic_profile_foreground, "Announcement 2"),
        Announcement("Team C", "Player 3", R.drawable.ic_profile_foreground, "Announcement 3")
    ) // Replace with actual announcement data

    Column {
        announcements.forEach { announcement ->
            AnnouncementItem(announcement)
            Divider() // Divider between announcement items
        }
    }
}

// Composable for individual announcement items
@Composable
fun AnnouncementItem(announcement: Announcement) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Profile image for the announcement
        Image(
            painter = painterResource(id = announcement.profileImage),
            contentDescription = "Profile Photo",
            modifier = Modifier
                .size(40.dp)
                .padding(end = 8.dp)
        )

        // Text content for the announcement
        Column(modifier = Modifier.weight(1f)) {
            Text(text = "${announcement.teamTag} | ${announcement.playerTag}")
            Text(text = announcement.description)
        }

        // Button to view profile (optional, can add functionality)
        IconButton(onClick = { /* Handle profile button click */ }) {
            Icon(imageVector = Icons.Default.Person, contentDescription = "Profile Icon")
        }
    }
}

// Data class for announcements
data class Announcement(
    val teamTag: String,
    val playerTag: String,
    val profileImage: Int,
    val description: String
)
