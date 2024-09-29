package com.example.bot_lobby.ui.pages

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bot_lobby.R
import com.example.bot_lobby.models.Player
import com.example.bot_lobby.ui.components.TeamItem
import com.example.bot_lobby.ui.viewmodels.PlayerViewModel
import com.example.bot_lobby.ui.viewmodels.TeamViewModel
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerProfileTab(
    playerViewModel: PlayerViewModel,
    teamViewModel: TeamViewModel,
    playerTag: String,
    onExitClick: () -> Unit
) {
    // Firebase Auth instance for managing user sessions
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current

    // This was taken form the following website to use the mutualstateof function
    // https://medium.com/@ah.shubita/jetpack-compose-remember-mutablestateof-derivedstateof-and-remembersaveable-explained-b6ede7fed673
    // Ahmad Shubita
    // https://medium.com/@ah.shubita

    // State to track if the user is logged in
    var isLoggedIn by remember { mutableStateOf(auth.currentUser != null) }

    // Get the player based on the playerTag, fallback to default values if not found
    val player = playerViewModel.players.collectAsState().value.find { it.playertag == playerTag }
        ?: Player(
            player = "user1@demo.com",  // Default email if player not found
            playertag = "Player Tag: Default",  // Default player tag
            teams = emptyList(),  // No teams assigned
            description = "Default description"  // Default description
        )

    // Retrieve the list of teams from the TeamViewModel
    val teams = teamViewModel.teams.collectAsState().value

    // State to manage the description field, initialized from the player's teams
    var description by remember { mutableStateOf(player.teams.joinToString(", ") { it }) }

    // This was taken form the following website to use the rememberScrollState function
    // https://developer.android.com/reference/kotlin/androidx/compose/foundation/ScrollState
    // Android Developers

    // Main scrollable container
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)
            .verticalScroll(rememberScrollState()), // Enable scrolling when content overflows
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Row for player image, tag, and action buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp),  // No internal padding
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Player image section
            Image(
                painter = painterResource(id = R.drawable.ic_team_tag),  // Sample image resource
                contentDescription = "Player Image",
                modifier = Modifier
                    .width(100.dp)
                    .height(140.dp)
                    .clip(RoundedCornerShape(16.dp))  // Rounded corners for the image
                    .border(1.dp, Color.Transparent, RoundedCornerShape(16.dp)),  // Border for the image
                contentScale = ContentScale.Crop  // Cropped image to maintain aspect ratio
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Column for player information and buttons
            Column(
                modifier = Modifier
                    .weight(2f)  // Take up available width after image
                    .align(Alignment.CenterVertically),
                verticalArrangement = Arrangement.spacedBy(8.dp)  // Space between elements
            ) {
                // Row with player tag and edit button
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Display player's tag
                    Text(
                        text = player.playertag,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    // Edit button to edit player information
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
                            painter = painterResource(id = R.drawable.ic_edit),  // Edit icon
                            contentDescription = "Edit",
                            modifier = Modifier.size(24.dp)  // Icon size
                        )
                    }
                }

                // Row with LFT (Looking for Team) and Public buttons
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // LFT Button
                    Button(
                        onClick = { /* Handle LFT button click */ },
                        modifier = Modifier.width(125.dp),
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
                        modifier = Modifier.width(125.dp),
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

                // Invite Button for sending team invites
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

        // TextField for player description input
        TextField(
            value = description,
            onValueChange = { newDesc -> description = newDesc },  // Update description state
            placeholder = { Text("Enter player description") },  // Placeholder text
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))  // Border styling
                .background(Color.White),
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

        // Display heading for the teams section with the number of teams
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Teams", style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold))
            Text("${teams.size}/10", style = TextStyle(fontSize = 16.sp))  // Display the current team count
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Iterate through teams and display each team using TeamItem composable
        teams.forEach { team ->
            TeamItem(team = team)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Logoff Button to sign out the user
        Button(
            onClick = {
                auth.signOut()  // Sign out the current user
                isLoggedIn = false  // Update the login state
                Toast.makeText(context, "Successfully logged off", Toast.LENGTH_SHORT).show()  // Show a confirmation toast

                // Call the onExitClick function or navigate to another screen
                onExitClick()
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
