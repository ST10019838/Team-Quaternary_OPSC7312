package com.example.bot_lobby.ui.composables

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bot_lobby.R
import com.example.bot_lobby.models.User
import com.example.bot_lobby.ui.screens.LoginScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerProfile(
    user: User,
    onDetailsSave: () -> Unit = {},
    onExitClick: () -> Unit = {}
) {
    // Firebase Auth instance for managing user sessions
    val context = LocalContext.current

    // State to manage the description field, initialized from the player's teams
    var user_bio by remember { mutableStateOf(user.user_bio) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp),  // No padding around the entire column
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Row for player image, tag, and action buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()  // Row fills the available width
                .padding(0.dp),  // No internal padding
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Player image section
            Image(
                painter = painterResource(id = R.drawable.ic_team_tag),  // Sample image resource
                contentDescription = "Player Image",
                modifier = Modifier
                    .width(120.dp)
                    .height(150.dp)
                    .clip(RoundedCornerShape(16.dp))  // Rounded corners for the image
                    .border(
                        1.dp,
                        Color.Transparent,
                        RoundedCornerShape(16.dp)
                    ),  // Border for the image
                contentScale = ContentScale.Crop  // Cropped image to maintain aspect ratio
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Column for player information and buttons
            Column(
                modifier = Modifier
                    .weight(1f)  // Take up available width after image
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
                        modifier = Modifier.width(110.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        ),
                        border = BorderStroke(1.dp, Color.Gray),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
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
                        Icon(
                            imageVector = Icons.Default.Public,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
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
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
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
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),  // Keyboard options
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
            Text(
                "${teams?.size}/10",
                style = TextStyle(fontSize = 16.sp)
            )  // Display the current team count
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Iterate through teams and display each team using TeamItem composable
        teams?.forEach { team ->
            TeamItem(team = team)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.weight(1f))  // Space to push buttons to the bottom

        // Exit Button to exit the profile screen
//        if (onExitClick != null) {
//            Button(
//                onClick = onExitClick,  // Triggered when the exit button is clicked
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .align(Alignment.CenterHorizontally),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color.White,
//                    contentColor = Color.Black
//                ),
//                border = BorderStroke(1.dp, Color.Gray),
//                shape = RoundedCornerShape(8.dp)
//            ) {
//                Text(
//                    text = "X",
//                    fontSize = 16.sp,
//                    fontWeight = FontWeight.Bold
//                )
//            }
//        }

        Spacer(modifier = Modifier.height(8.dp))

        // Logoff Button to sign out the user
        Button(
            onClick = {
//                auth.signOut()  // Sign out the current user
                Toast.makeText(context, "Successfully logged off", Toast.LENGTH_SHORT)
                    .show()  // Show a confirmation toast
                navigator.popUntilRoot()  // Navigate back to the root screen
                navigator.push(LoginScreen())  // Push the LoginScreen back onto the stack
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
