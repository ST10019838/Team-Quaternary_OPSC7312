package com.example.bot_lobby.ui.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.bot_lobby.models.Announcement

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
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Expand announcement details"
                    )  // Dropdown icon
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