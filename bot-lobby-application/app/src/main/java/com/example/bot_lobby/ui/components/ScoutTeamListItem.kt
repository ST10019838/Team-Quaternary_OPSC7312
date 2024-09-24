package com.example.bot_lobby.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bot_lobby.models.Member
import com.example.bot_lobby.models.Team
import com.example.bot_lobby.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoutTeamListItem(team: Team) {
    // Main Box for each team
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(BorderStroke(1.dp, Color.Gray)) // Border for each box
            .padding(8.dp) // Padding inside the box
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp) // Padding inside the box
        ) {
            // First Row: Icon, Team tag and name, Teams Button, and View Profile Button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth() // Fill width to edge
            ) {
                // Icon on the first row (ic_player_tag.png)
                Image(
                    painter = painterResource(id = R.drawable.ic_player_tag),
                    contentDescription = "Player Tag Icon",
                    modifier = Modifier.size(32.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Team tag (bold) and team name (smaller font)
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = team.teamtag,  // Team tag in bold
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        ),
                    )
                    Text(
                        text = team.teamname,  // Team name in smaller font size
                        fontSize = 11.sp,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Teams Button showing XX/XX for number of teams
                Button(
                    onClick = {
                        // Handle teams button action here
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White, // White background
                        contentColor = Color.Black // Black text color
                    ),
                    border = BorderStroke(1.dp, Color.Gray), // Grey border
                    shape = RoundedCornerShape(8.dp), // Rounded edges
                    modifier = Modifier
                        .height(36.dp) // Adjust height
                        .width(120.dp) // Adjust width
                ) {
                    Text(
                        text = "${team.members.size} / 10", // Example for team count
                        fontSize = 14.sp // Text size
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // View Profile Button with icon
                IconButton(
                    onClick = {
                        // Handle view profile action
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = "View Profile",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Second Row: Open and Join Buttons
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth() // Fill the width
            ) {
                // Open Button
                Button(
                    onClick = {
                        // Handle open action
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White, // White background
                        contentColor = Color.Black // Black text color
                    ),
                    border = BorderStroke(1.dp, Color.Gray), // Grey border
                    shape = RoundedCornerShape(8.dp), // Rounded edges
                    modifier = Modifier
                        .height(36.dp)
                        .weight(1f) // Make the button fill 1/3 the width
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_open_book), // Open book icon
                        contentDescription = "Open",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Open")
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Join Button
                Button(
                    onClick = {
                        // Handle join action
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White, // White background
                        contentColor = Color.Black // Black text color
                    ),
                    border = BorderStroke(1.dp, Color.Gray), // Grey border
                    shape = RoundedCornerShape(8.dp), // Rounded edges
                    modifier = Modifier
                        .height(36.dp)
                        .weight(2f) // Make the button fill 2/3f the width
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_square_plus), // Square block with plus sign icon
                        contentDescription = "Join",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Join")
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp)) // Spacer between team boxes
}
