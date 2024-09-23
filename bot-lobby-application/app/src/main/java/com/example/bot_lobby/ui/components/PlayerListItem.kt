package com.example.bot_lobby.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color.Companion.Gray
import com.example.bot_lobby.R
import com.example.bot_lobby.models.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.background

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerListItem(player: Player, teams: List<Team>) {
    // Initialize FocusRequester for controlling focus on the TextField
    val focusRequester = remember { FocusRequester() }

    // Manage state for dropdown expansion and selected team
    var expanded by remember { mutableStateOf(false) }  // Controls dropdown expansion state
    var selectedTeam by remember { mutableStateOf("") }  // Stores the selected team

    // Main container for the player list item
    Box(
        modifier = Modifier
            .fillMaxWidth()  // Ensure the item takes up the full width of the screen
            .border(BorderStroke(1.dp, Gray))  // Add a gray border around the item
            .padding(8.dp)  // Padding inside the item
    ) {
        // Column to stack elements vertically
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)  // Padding between the box border and the content
        ) {
            // First Row: Player Icon, Player Name, LFT Button, and View Profile Button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()  // Make the row fill the available width
            ) {
                // Display the player icon (can be replaced with an actual image)
                Image(
                    painter = painterResource(id = R.drawable.ic_player_tag),  // Replace with player image resource
                    contentDescription = "Player Icon",
                    modifier = Modifier.size(48.dp),  // Set the size of the player icon
                )

                Spacer(modifier = Modifier.width(8.dp))  // Add space between the icon and the text

                // Display the player's name
                Text(
                    text = player.name,  // Player's name text
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),  // Bold text style
                    modifier = Modifier.weight(1f)  // Make the text take up available space
                )

                Spacer(modifier = Modifier.width(8.dp))  // Add space between the text and buttons

                // LFT (Looking for Team) Button
                LFTButton(
                    inTeam = player.team.isNotEmpty(),  // Check if the player is already in a team
                    onClick = {
                        // Handle LFT button action here (e.g., mark player as LFT)
                    }
                )

                Spacer(modifier = Modifier.width(8.dp))  // Add space between the LFT button and the view profile button

                // View Profile Button (for viewing more details about the player)
                IconButton(onClick = {
                    // Handle view profile action here (e.g., navigate to the player profile)
                }) {
                    Icon(
                        imageVector = Icons.Default.Visibility,  // Use the visibility icon for viewing profile
                        contentDescription = "View Profile",
                        modifier = Modifier.size(32.dp)  // Set the size of the icon
                    )
                }
            }

            // Second Row: Team Selection Dropdown and Invite Button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)  // Add space above the second row
            ) {
                // Box to contain the dropdown menu for team selection
                Box(modifier = Modifier.width(150.dp)) {
                    // Start of ExposedDropdownMenuBox
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = {
                            // Toggle dropdown expansion only if no team is assigned
                            if (player.team.isEmpty()) {
                                expanded = !expanded
                            }
                        }
                    ) {
                        // Determine the text color based on whether the dropdown is enabled or not
                        val textColor = if (player.team.isEmpty()) Color.Black else Color.LightGray

                        // TextField that shows selected team or placeholder if no team is assigned
                        TextField(
                            value = if (player.team.isNotEmpty()) player.team else selectedTeam,  // Show current or selected team
                            onValueChange = {},  // Disable manual text input
                            placeholder = {
                                Text(
                                    text = "Team Tag",  // Placeholder text when no team is selected
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.Center  // Center the text horizontally
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))  // Grey border with rounded corners
                                .focusRequester(focusRequester)  // Attach FocusRequester to handle focus
                                .menuAnchor()  // Use as anchor for the dropdown menu
                                .clickable(
                                    enabled = player.team.isEmpty()  // Disable clicking if player has a team
                                ) {
                                    if (player.team.isEmpty()) {
                                        expanded = !expanded
                                        focusRequester.requestFocus()  // Request focus on click
                                    }
                                },
                            readOnly = true,  // Disable manual typing
                            textStyle = TextStyle(fontSize = 12.sp, color = textColor),  // Adjust text color dynamically
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,  // Set background color to white when focused
                                unfocusedContainerColor = Color.White,  // Set background color to white when unfocused
                                disabledContainerColor = Color.White,  // Keep the background white when disabled
                                focusedIndicatorColor = Color.Transparent,  // Remove focus indicator
                                unfocusedIndicatorColor = Color.Transparent,  // Remove unfocused indicator
                                disabledTextColor = Gray
                            ),
                            trailingIcon = {
                                Icon(
                                    imageVector = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                                    contentDescription = if (player.team.isEmpty()) "Dropdown" else "Dropdown Disabled",
                                    // Set the arrow to be grayed out when disabled
                                    modifier = Modifier.graphicsLayer {
                                        alpha = if (player.team.isEmpty()) 1f else 0.3f  // Reduce opacity if disabled
                                    }
                                )
                            }
                        )

                        // Dropdown menu for selecting teams
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },  // Collapse dropdown when an item is clicked or outside click
                            modifier = Modifier
                                .background(Color.White)  // Set background to white
                                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))  // Add grey border with rounded corners
                        ) {
                            teams.forEach { team ->
                                DropdownMenuItem(
                                    text = { Text(text = team.name, fontSize = 14.sp) },
                                    onClick = {
                                        selectedTeam = team.name  // Set selected team when clicked
                                        expanded = false  // Collapse the dropdown
                                    }
                                )
                            }
                        }
                    }  // End of ExposedDropdownMenuBox
                }

                Spacer(modifier = Modifier.width(18.dp))  // Add space between the dropdown and the invite button

                // Invite Button (enabled only if the player is not in a team)
                InviteButton(
                    enabled = player.team.isEmpty(),  // Enable only if the player is not in a team
                    onClick = {
                        if (player.team.isEmpty()) {
                            // Handle invite action here (e.g., invite player to a team)
                        }
                    },
                    modifier = Modifier.width(150.dp)  // Set the width of the button to match the dropdown
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp))  // Add space between player boxes
}

// Composable for the LFT (Looking for Team) Button
@Composable
fun LFTButton(inTeam: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        border = BorderStroke(1.dp, Gray),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .height(48.dp)
            .width(100.dp)
    ) {
        Icon(
            imageVector = if (inTeam) Icons.Default.Close else Icons.Default.Check,
            contentDescription = if (inTeam) "In Team" else "Looking for Team",
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "LFT",
            fontSize = 14.sp
        )
    }
}

// Composable for the Invite Button
@Composable
fun InviteButton(enabled: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,  // Background color when enabled
            contentColor = if (enabled) Color.Black else Gray,  // Text/Icon color based on enabled state
            disabledContainerColor = Color.White,  // Set background color to white when disabled
            disabledContentColor = Gray  // Set content color to grey when disabled
        ),
        border = BorderStroke(1.dp, Gray),  // Grey border
        shape = RoundedCornerShape(8.dp),  // Rounded corners
        modifier = modifier
            .height(48.dp)
            .width(180.dp)
    ) {
        Icon(
            imageVector = Icons.Default.MailOutline,
            contentDescription = "Invite to Team",
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "Invite to Team",
            fontSize = 12.sp
        )
    }
}
