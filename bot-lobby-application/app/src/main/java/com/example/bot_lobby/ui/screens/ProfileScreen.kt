package com.example.bot_lobby.ui.screens

import android.widget.Toast
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    playerViewModel: PlayerViewModel,
    teamViewModel: TeamViewModel,
    playerTag: String,
    onExitClick: () -> Unit
) {
    // Get the current FirebaseAuth instance and navigator
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current
    val navigator = LocalNavigator.currentOrThrow

    // Get the player data from the ViewModel
    val player = playerViewModel.players.collectAsState().value.find { it.tag == playerTag }
    val teams = teamViewModel.teams.collectAsState().value
    var description by remember { mutableStateOf(player?.team ?: "") }  // Player description

    // Default player tag if player is not found
    val defaultPlayerTag = "Player Tag: Default"
    val displayPlayerTag = player?.tag ?: defaultPlayerTag

    // Main layout for the ProfileScreen
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (player != null) {
            // Row with Player Image and details
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Player image (ic_player_tag.png)
                Image(
                    painter = painterResource(id = R.drawable.ic_player_tag),
                    contentDescription = "Player Image",
                    modifier = Modifier.size(200.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Content next to the image in 3 rows
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    // Row 1: Player name
                    Text(
                        text = player.name,
                        style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Row 2: LFT button and Public button
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Button(
                            onClick = { /* Handle LFT button click */ },
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null)
                            Text("LFT")
                        }
                        Button(onClick = { /* Handle Public button click */ }) {
                            Icon(imageVector = Icons.Default.Public, contentDescription = null)
                            Text("Public")
                        }
                    }

                    // Row 3: Invite button (stretches across)
                    Button(
                        onClick = { /* Handle Invite click */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Email, contentDescription = null)
                        Text("Invite")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Input field for player description
            TextField(
                value = description,
                onValueChange = { newDesc -> description = newDesc },
                placeholder = { Text("Enter player description") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                singleLine = false
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

            // Exit button
            Button(
                onClick = onExitClick,
                modifier = Modifier
                    .size(56.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Exit")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Logoff Button
            Button(
                onClick = {
                    // Call the Firebase signOut() method to log off
                    auth.signOut()

                    // Show a toast message to notify the user
                    Toast.makeText(context, "Successfully logged off", Toast.LENGTH_SHORT).show()

                    // Navigate back to the root screen (LoginScreen) after signing out
                    navigator.popUntilRoot()
                    navigator.push(LoginScreen())
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Log Off")
            }
        } else {
            // Default display when player is not found
            Text("Player not found")
            Text(displayPlayerTag, style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Medium))

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onExitClick,
                modifier = Modifier
                    .size(56.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Exit")
            }
        }
    }
}

