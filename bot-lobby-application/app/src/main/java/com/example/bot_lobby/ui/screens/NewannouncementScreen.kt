package com.example.bot_lobby.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import com.example.bot_lobby.view_models.AnnouncementViewModel
import java.util.Date

@Composable
fun NewAnnouncementScreen(
    viewModel: AnnouncementViewModel,
    onCancel: () -> Unit,
    onPostAnnouncement: () -> Unit,
    currentUserId: String // Pass in the currently logged-in user ID
) {
    var title by remember { mutableStateOf(TextFieldValue("")) }
    var content by remember { mutableStateOf(TextFieldValue("")) }
    var selectedTeam by remember { mutableStateOf("") }
    val teamList = listOf("Team A", "Team B", "Team C")
    var teamDropdownExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "New Announcement",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Team *")
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .padding(8.dp)
                .clickable { teamDropdownExpanded = true }
        ) {
            Text(
                text = if (selectedTeam.isEmpty()) "Select Team" else selectedTeam,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            DropdownMenu(
                expanded = teamDropdownExpanded,
                onDismissRequest = { teamDropdownExpanded = false }
            ) {
                teamList.forEach { team ->
                    DropdownMenuItem(
                        text = { Text(text = team) },
                        onClick = {
                            selectedTeam = team
                            teamDropdownExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Divider(color = Color.Gray, thickness = 1.dp)
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Title *")
        BasicTextField(
            value = title,
            onValueChange = { title = it },
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .padding(8.dp),
            textStyle = TextStyle(color = Color.Black)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Description *")
        BasicTextField(
            value = content,
            onValueChange = { content = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .padding(8.dp),
            textStyle = TextStyle(color = Color.Black)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.saveAnnouncement(
                    title = title.text,
                    content = content.text,
                    team = selectedTeam,
                    currentUserId = currentUserId
                )
                onPostAnnouncement()  // Trigger callback to close or refresh the screen
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
        ) {
            Text("Post Announcement")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = onCancel,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancel")
        }
    }
}
