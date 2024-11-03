package com.example.bot_lobby.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bot_lobby.models.Announcement
import com.example.bot_lobby.ui.composables.AnnouncementItem
import com.example.bot_lobby.ui.theme.BlueStandard
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@SuppressLint("NewApi")
@Composable
fun AnnouncementsScreen(
    announcements: List<Announcement>,
    onAddAnnouncement: () -> Unit,
    onAnnouncementClick: (Announcement) -> Unit
) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Announcements",
            style = MaterialTheme.typography.headlineSmall.copy( // Changed h5 to headlineSmall
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            textAlign = TextAlign.Center
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 8.dp)
        ) {
            items(announcements) { announcement ->
                AnnouncementItem(
                    announcement = announcement,
                    onClick = { onAnnouncementClick(announcement) }
                )
                Spacer(modifier = Modifier.height(8.dp)) // Spacer between items
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.BottomEnd // Changed to BottomEnd for full compatibility
        ) {
            FloatingActionButton(
                onClick = onAddAnnouncement,
                modifier = Modifier.padding(16.dp),
                shape = MaterialTheme.shapes.medium,
                containerColor = BlueStandard,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add announcement", tint = Color.White)
            }
        }
    }
}
