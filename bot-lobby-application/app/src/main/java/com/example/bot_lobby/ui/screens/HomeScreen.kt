package com.example.bot_lobby.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bot_lobby.models.Announcement
import com.example.bot_lobby.models.Event
import com.example.bot_lobby.ui.composables.AnnouncementItem
import com.example.bot_lobby.ui.composables.EventItem


// Main content composable for the Home Screen, responsible for rendering all UI elements
@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()  // Fill the entire screen space
            .padding(4.dp),  // Apply padding to the content
        horizontalAlignment = Alignment.CenterHorizontally  // Center-align content horizontally
    ) {
        // Display the "Welcome Back!" heading centered on the screen
        Text(
            text = "Welcome Back!",  // Text content
            style = MaterialTheme.typography.h4,  // Large headline text style
            color = Color.Black,  // Set text color to black
            modifier = Modifier
                .padding(bottom = 16.dp)  // Padding below the text
                .align(Alignment.CenterHorizontally)  // Align the text at the center
        )

        // Section for displaying upcoming events
        CollapsibleSection(
            heading = "Upcoming Events",  // Title for the section
            content = {
                // List of sample events
                val events = listOf(
                    Event(
                        "Event 1",
                        "Description of Event 1",
                        "01/10/2024",
                        "02/10/2024",
                        "Team A",
                        listOf("Player 1", "Player 2")
                    ),
                    Event(
                        "Event 2",
                        "Description of Event 2",
                        "05/10/2024",
                        "06/10/2024",
                        "Team B",
                        listOf("Player 3", "Player 4")
                    ),
                    Event(
                        "Event 2",
                        "Description of Event 2",
                        "05/10/2024",
                        "06/10/2024",
                        "Team B",
                        listOf("Player 2", "Player 3")
                    )
                )
                // Loop through each event and display it
                events.forEach { event ->
                    EventItem(event)  // Render each event using the EventItem composable
                }
            }
        )

        // Add space between the two sections
        Spacer(modifier = Modifier.height(16.dp))

        // Section for displaying announcements
        CollapsibleSection(
            heading = "Announcements",  // Title for the section
            content = {
                // List of sample announcements
                val announcements = listOf(
                    Announcement("Team A", "Player 1", "Important Announcement for Team A"),
                    Announcement("Team B", "Player 2", "Important Announcement for Team B"),
                    Announcement("Team C", "Player 3", "Important Announcement for Team C")
                )
                // Loop through each announcement and display it
                announcements.forEach { announcement ->
                    AnnouncementItem(announcement)  // Render each announcement using the AnnouncementItem composable
                }
            }
        )
    }
}

// Composable function to create a collapsible section
@Composable
fun CollapsibleSection(heading: String, content: @Composable () -> Unit) {
    var expanded by remember { mutableStateOf(true) }  // Set expanded to true by default

    Column {
        // Heading row with an expand/collapse icon
        Row(
            modifier = Modifier.fillMaxWidth(),  // Fill available width
            verticalAlignment = Alignment.CenterVertically  // Center-align the row content vertically
        ) {
            // Display the heading text
            Text(
                text = heading,
                fontSize = 18.sp,  // Set font size for the heading
                color = Color.Black,  // Set text color to black
                modifier = Modifier.weight(1f)  // Allow text to take available space
            )
            // IconButton to toggle the expand/collapse state
            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    imageVector = Icons.Default.Visibility,
                    contentDescription = "Expand or collapse"
                )  // Dropdown arrow icon
            }
        }

        // Display the content (events or announcements) if expanded is true
        if (expanded) {
            content()  // Show the section content
        }
    }
}



