package com.example.bot_lobby.ui.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bot_lobby.R
import com.example.bot_lobby.models.Team
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//used by Navigationtab/Teams

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamListItem(
    team: Team
) {
    // Get the current date in xx/xx/xxxx format
    val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(
                BorderStroke(1.dp, Color.Gray),
                shape = RoundedCornerShape(8.dp)
            ) // Add a grey border with rounded corners
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            // First Row: Icon, Team tag and name, Teams Button, and View Profile Button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_player_tag),
                    contentDescription = "Player Tag Icon",
                    modifier = Modifier.size(32.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = team.teamtag,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        ),
                    )
                    Text(
                        text = team.teamname,
                        fontSize = 11.sp,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        // Handle teams button action here
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    border = BorderStroke(1.dp, Color.Gray),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .height(36.dp)
                        .width(90.dp)
                ) {
                    Text(
                        text = "${team.members.size} / 10", // Example for team count
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // View Profile Button with icon
                // This should call the TeamProfile.kt
                IconButton(
                    onClick = {
                        // Check if team.teamtag is empty, and assign a default value if so
                        val teamTagToNavigate = if (team.teamtag.isNullOrEmpty()) {
                            "Default Team Tag" // Provide a default team tag if team.tag is null or empty
                        } else {
                            team.teamtag
                        }

                        // Ensure the navigation graph has been set before navigating
                        try {
                            //navController.navigate("team_profile/$teamTagToNavigate")
                        } catch (e: IllegalArgumentException) {
                            // Handle the case where the navigation graph is not properly set
                            println("Navigation graph is not properly set: ${e.message}")
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = "View Team Profile",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            // Add a horizontal divider
            Divider(
                color = Color.Gray,
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Second Row: "Next Event:" text and today's date
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Text for "Next Event:"
                Text(
                    text = "Next Event:",
                    color = Color.Black,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f) // This will take up the available space on the left
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Today's date aligned to the right
                Text(
                    text = currentDate,
                    color = Color.Black,
                    fontSize = 14.sp

                )
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}
