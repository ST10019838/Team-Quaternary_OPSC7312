package com.example.bot_lobby.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import com.example.bot_lobby.models.Event
import com.example.bot_lobby.models.Announcement

// Main object implementing the HomeScreen
object HomeScreen : Screen {

    // This function defines the content to be displayed when HomeScreen is active
    @Composable
    override fun Content() {
        HomeScreenContent()  // Calls the main content composable for HomeScreen
    }
}

// Main content composable for the Home Screen, responsible for rendering all UI elements
@Composable
fun HomeScreenContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()  // Fill the entire screen space
            .padding(16.dp),  // Apply padding to the content
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
                    Event("Event 1", "Description of Event 1", "01/10/2024", "02/10/2024", "Team A", listOf("Player 1", "Player 2")),
                    Event("Event 2", "Description of Event 2", "05/10/2024", "06/10/2024", "Team B", listOf("Player 3", "Player 4"))
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
                    Announcement("Team B", "Player 2", "Important Announcement for Team B")
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
    var expanded by remember { mutableStateOf(false) }  // State to track whether the section is expanded or collapsed

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
                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Expand or collapse")  // Dropdown arrow icon
            }
        }

        // If expanded is true, display the content passed to this function
        if (expanded) {
            content()  // Display the section content (events or announcements)
        }
    }
}

// Composable function to display a single event item
@Composable
fun EventItem(event: Event) {
    var expanded by remember { mutableStateOf(false) }  // State to track whether the event details are expanded

    // Create a surface with a border for each event
    Surface(
        modifier = Modifier
            .fillMaxWidth()  // Fill available width
            .padding(vertical = 4.dp),  // Add vertical padding between event items
        border = BorderStroke(1.dp, Color.Gray),  // Add a grey border around the event box
        shape = MaterialTheme.shapes.small,  // Apply small rounded corners to the box
    ) {
        // Column to arrange event content vertically
        Column(
            modifier = Modifier.padding(8.dp)  // Add padding inside the event box
        ) {
            // First row contains the event title and a dropdown arrow to expand the details
            Row(
                modifier = Modifier.fillMaxWidth(),  // Fill available width
                verticalAlignment = Alignment.CenterVertically  // Center the content vertically
            ) {
                // Display the event title and date
                Text(text = "${event.title} - ${event.startDate}")
                Spacer(modifier = Modifier.weight(1f))  // Push the arrow to the right side
                // Dropdown arrow to expand/collapse event details
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Expand event details")  // Dropdown icon
                }
            }

            // If expanded is true, display the event details
            if (expanded) {
                Column {
                    // Display additional event details like description, start date, end date, team, and team members
                    Text("Description: ${event.description}")
                    Text("Start Date: ${event.startDate}")
                    Text("End Date: ${event.endDate}")
                    Text("Team: ${event.team}")
                    Text("Team Members: ${event.teamMembers.joinToString()}")
                }
            }
        }
    }
}

// Composable function to display a single announcement item
@Composable
fun AnnouncementItem(announcement: Announcement) {
    var expanded by remember { mutableStateOf(false) }  // State to track whether the announcement details are expanded

    // Create a surface with a border for each announcement
    Surface(
        modifier = Modifier
            .fillMaxWidth()  // Fill available width
            .padding(vertical = 4.dp),  // Add vertical padding between announcement items
        border = BorderStroke(1.dp, Color.Gray),  // Add a grey border around the announcement box
        shape = MaterialTheme.shapes.small,  // Apply small rounded corners to the box
    ) {
        // Column to arrange announcement content vertically
        Column(
            modifier = Modifier.padding(8.dp)  // Add padding inside the announcement box
        ) {
            // First row contains the team and player tag, along with a dropdown arrow
            Row(
                modifier = Modifier.fillMaxWidth(),  // Fill available width
                verticalAlignment = Alignment.CenterVertically  // Center the content vertically
            ) {
                // Display the team and player tag
                Text(text = "${announcement.teamTag} - ${announcement.playerTag}")
                Spacer(modifier = Modifier.weight(1f))  // Push the arrow to the right side
                // Dropdown arrow to expand/collapse announcement details
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Expand announcement details")  // Dropdown icon
                }
            }

            // If expanded is true, display the full announcement details
            if (expanded) {
                Column {
                    // Display the announcement description
                    Text("Announcement: ${announcement.description}")
                }
            }
        }
    }
}
