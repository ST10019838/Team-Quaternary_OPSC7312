package com.example.bot_lobby.ui.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp
import com.example.bot_lobby.models.Announcement
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AnnouncementItem(
    announcement: Announcement,
    onClick: () -> Unit,
    cornerRadius: Dp = 12.dp  // Default corner radius for rounded edges
) {
    var expanded by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),  // Make the entire item clickable
        border = BorderStroke(1.dp, Color.Gray),
        shape = RoundedCornerShape(cornerRadius),  // Rounded corners with specified radius
        color = MaterialTheme.colors.surface
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            // Header with title and dropdown icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${announcement.team}: ${announcement.title}",
                    style = MaterialTheme.typography.subtitle1
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Expand announcement details",
                        tint = if (expanded) MaterialTheme.colors.primary else Color.Gray
                    )
                }
            }

            // Expanded content layout
            if (expanded) {
                Column(modifier = Modifier.padding(top = 4.dp)) {
                    Text(
                        text = announcement.content,
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Format date
                    val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                    val formattedDate = dateFormatter.format(announcement.dateCreated)

                    // Date Created
                    Text(
                        text = "Date Created: $formattedDate",
                        style = MaterialTheme.typography.caption,
                        color = Color.Gray
                    )

                    // Created By (on a new line)
                    Text(
                        text = "Created By: ${announcement.createdByUserId}",
                        style = MaterialTheme.typography.caption,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}
