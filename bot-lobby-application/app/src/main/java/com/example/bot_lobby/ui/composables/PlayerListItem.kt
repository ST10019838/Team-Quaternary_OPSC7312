package com.example.bot_lobby.ui.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bot_lobby.R
import com.example.bot_lobby.models.Player
import com.example.bot_lobby.models.Team

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerListItem(player: Player, teams: List<Team>) {
    val focusRequester = remember { FocusRequester() }
    var expanded by remember { mutableStateOf(false) }
    var selectedTeam by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(BorderStroke(1.dp, Gray))
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            // First Row: Player Icon, Player Name, LFT Button, and View Profile Button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_player_tag),
                    contentDescription = "Player Icon",
                    modifier = Modifier.size(48.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = player.playertag,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // LFT Button
                LFTButton(
                    inTeam = player.teams.isNotEmpty(),
                    onClick = {
                        // Handle LFT action
                    }
                )

                Spacer(modifier = Modifier.width(8.dp))

                // View Profile Button
                IconButton(onClick = {
                    // Check if player.playertag is empty, and assign "Player Tag 1" if so
                    val playertagToNavigate = if (player.playertag.isNullOrEmpty()) {
                        "Player Tag 1"
                    } else {
                        player.playertag
                    }

                    // Ensure the navigation graph has been set before navigating
                    try {
//                        navController.navigate("player_profile/$playertagToNavigate")
                    } catch (e: IllegalArgumentException) {
                        // Handle the case where the navigation graph is not properly set
                        println("Navigation graph is not properly set: ${e.message}")
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = "View Profile",
                        modifier = Modifier.size(32.dp)
                    )
                }

            }

            // Second Row: Team Selection Dropdown and Invite Button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Box(modifier = Modifier.width(150.dp)) {
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = {
                            if (player.teams.isEmpty()) {
                                expanded = !expanded
                            }
                        }
                    ) {
                        val textColor = if (player.teams.isEmpty()) Color.Black else Color.LightGray

                        TextField(
                            value = if (player.teams.isNotEmpty()) player.teams.first() else selectedTeam,
                            onValueChange = {},
                            placeholder = {
                                Text(
                                    text = "Team Tag",
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.Center
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .border(1.dp, Gray, RoundedCornerShape(8.dp))
                                .focusRequester(focusRequester)
//                                .menuAnchor(type, enabled)
                                .clickable(enabled = player.teams.isEmpty()) {
                                    if (player.teams.isEmpty()) {
                                        expanded = !expanded
                                        focusRequester.requestFocus()
                                    }
                                },
                            readOnly = true,
                            textStyle = TextStyle(fontSize = 12.sp, color = textColor),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                disabledContainerColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledTextColor = Gray
                            ),
                            trailingIcon = {
                                Icon(
                                    imageVector = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                                    contentDescription = "Dropdown",
                                    modifier = Modifier.graphicsLayer {
                                        alpha = if (player.teams.isEmpty()) 1f else 0.3f
                                    }
                                )
                            }
                        )

                        // Dropdown menu for selecting team
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier
                                .background(Color.White)
                                .border(1.dp, Gray, RoundedCornerShape(8.dp))
                        ) {
                            teams.forEach { team ->
                                DropdownMenuItem(
                                    text = { Text(text = team.teamtag, fontSize = 14.sp) },
                                    onClick = {
                                        selectedTeam = team.teamtag
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.width(18.dp))

                // Invite Button
                InviteButton(
                    enabled = player.teams.isEmpty(),
                    onClick = {
                        if (player.teams.isEmpty()) {
                            // Handle invite action
                        }
                    },
                    modifier = Modifier.width(150.dp)
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp))
}

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

@Composable
fun InviteButton(enabled: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = if (enabled) Color.Black else Gray,
            disabledContainerColor = Color.White,
            disabledContentColor = Gray
        ),
        border = BorderStroke(1.dp, Gray),
        shape = RoundedCornerShape(8.dp),
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
