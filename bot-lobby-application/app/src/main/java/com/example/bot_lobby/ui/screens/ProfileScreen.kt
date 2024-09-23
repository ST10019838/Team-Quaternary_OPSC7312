package com.example.bot_lobby.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.bot_lobby.R
import com.example.bot_lobby.ui.viewmodels.PlayerViewModel
import com.example.bot_lobby.ui.viewmodels.TeamViewModel
import com.example.bot_lobby.models.*
import com.example.bot_lobby.ui.components.TeamItem
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.draw.clip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    playerViewModel: PlayerViewModel,
    teamViewModel: TeamViewModel,
    playerTag: String,
    onExitClick: () -> Unit
) {
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current
    val navigator = LocalNavigator.currentOrThrow

    // Get the player from the ViewModel, if not found, assign a default player
    val player = playerViewModel.players.collectAsState().value.find { it.playertag == playerTag }
        ?: Player(
            player = "user1@demo.com",  // Default email if player not found
            playertag = "Player Tag: Default",  // Default player tag
            teams = emptyList(),  // No teams assigned
            description = "Default description"  // Default description
        )

    val teams = teamViewModel.teams.collectAsState().value
    var description by remember { mutableStateOf(player.teams.joinToString(", ") { it }) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp),  // Removed padding
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Row with Player Image and Player details (Edit button, LFT, Public, Invite)
        Row(
            modifier = Modifier
                .fillMaxWidth() // Stretch row across full width
                .padding(0.dp),  // Remove internal padding
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image Section (Column 1)
            Image(
                painter = painterResource(id = R.drawable.ic_team_tag),
                contentDescription = "Player Image",
                modifier = Modifier
                    .width(120.dp) // Set image width
                    .height(150.dp) // Set image height
                    .clip(RoundedCornerShape(16.dp)) // Rounded corners
                    .border(1.dp, Color.Transparent, RoundedCornerShape(16.dp)), // Transparent border
                contentScale = ContentScale.Crop // Scale image to fit
            )

            Spacer(modifier = Modifier.width(16.dp)) // No spacing between image and content

            // Player Info & Buttons Section (Column 2)
            Column(
                modifier = Modifier
                    .weight(1f) // Stretch to fill remaining space
                    .align(Alignment.CenterVertically),
                verticalArrangement = Arrangement.spacedBy(8.dp) // Space between items
            ) {
                // Row for Player Tag and Edit button
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Player Tag
                    Text(
                        text = player.playertag,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    // Edit Button
                    Button(
                        onClick = { /* Handle Edit Click */ },
                            colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.Black  // Icon color
                        ),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Color.Transparent)  // Transparent border
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_edit),
                            contentDescription = "Edit",
                            modifier = Modifier.size(24.dp)  // Adjust icon size
                        )
                    }
                }

                // Row for LFT and Public buttons
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // LFT Button
                    Button(
                        onClick = { /* Handle LFT button click */ },
                        modifier = Modifier.width(110.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        ),
                        border = BorderStroke(1.dp, Color.Gray),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("LFT", fontSize = 12.sp)
                    }

                    // Public Button
                    Button(
                        onClick = { /* Handle Public button click */ },
                        modifier = Modifier.width(110.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        ),
                        border = BorderStroke(1.dp, Color.Gray),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Public, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Public", fontSize = 12.sp)
                    }
                }

                // Invite Button
                Button(
                    onClick = { /* Handle Invite button click */ },
                    modifier = Modifier.width(250.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    border = BorderStroke(1.dp, Color.Gray),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.Email, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Invite", fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Input field for player description
        TextField(
            value = description,
            onValueChange = { newDesc -> description = newDesc },
            placeholder = { Text("Enter player description") },
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .background(Color.White),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            singleLine = false,
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                cursorColor = Color.Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Teams heading with dynamic XX/XX teams count
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Teams", style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold))
            Text("${teams.size}/10", style = TextStyle(fontSize = 16.sp))
        }

        Spacer(modifier = Modifier.height(8.dp))

        // List of teams the player belongs to
        teams.forEach { team ->
            TeamItem(team = team)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.weight(1f))

        // Exit Button
        Button(
            onClick = onExitClick,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            border = BorderStroke(1.dp, Color.Gray),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "X",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Logoff Button
        Button(
            onClick = {
                auth.signOut()
                Toast.makeText(context, "Successfully logged off", Toast.LENGTH_SHORT).show()
                navigator.popUntilRoot()
                navigator.push(LoginScreen())
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            border = BorderStroke(1.dp, Color.Gray),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Log Off", fontSize = 12.sp)
        }
    }
}
