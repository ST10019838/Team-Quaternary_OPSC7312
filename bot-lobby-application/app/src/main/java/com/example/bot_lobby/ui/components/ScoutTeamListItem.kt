package com.example.bot_lobby.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.* // Import layout components
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bot_lobby.models.*
import androidx.compose.foundation.border

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoutTeamList(teams: List<Team>) {
    // Wrapping the entire list in a LazyColumn to make it scrollable
    LazyColumn(
        modifier = Modifier
            .fillMaxSize() // Fill the available space
            .padding(8.dp) // Padding around the column
    ) {
        items(teams) { team ->
            ScoutTeamListItem(team = team)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class) // Allow experimental API usage
@Composable
fun ScoutTeamListItem(team: Team) {
    // Main Box for each team
    Box(
        modifier = Modifier
            .fillMaxWidth() // Fill the width of the screen
            .border(BorderStroke(1.dp, Color.Gray)) // Border for each box
            .padding(8.dp) // Padding inside the box
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp) // Padding inside the box
        ) {
            // First Row: Team name and View Details button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth() // Fill width to edge
            ) {
                // Team name (bold)
                Text(
                    text = team.name,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.weight(1f) // Fill remaining space for the team's name
                )

                Spacer(modifier = Modifier.width(8.dp))

                // View Details Button
                Button(
                    onClick = {
                        // Handle view details action here
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White, // White background
                        contentColor = Color.Black // Black text color
                    ),
                    border = BorderStroke(1.dp, Color.Gray), // Grey border
                    shape = RoundedCornerShape(8.dp), // Small rounded edges
                    modifier = Modifier
                        .height(36.dp) // Adjust height to make it more square
                        .width(120.dp) // Adjust width for the button
                ) {
                    Text(
                        text = "View Details",
                        fontSize = 14.sp // Text size
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Second Row: List of members in the team
            Column(modifier = Modifier.fillMaxWidth()) {
                team.members.forEach { member ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                    ) {
                        Text(
                            text = "${member.name} (${member.role})",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp)) // Spacer between team boxes
}
