package com.example.bot_lobby.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.bot_lobby.view_models.UserViewModel
import com.example.bot_lobby.view_models.TeamViewModel
import com.example.bot_lobby.models.*
import com.example.bot_lobby.models.Team
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.draw.clip
import io.supabase.kotlin.SupabaseClient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userViewModel: UserViewModel,  // Changed PlayerViewModel to UserViewModel
    teamViewModel: TeamViewModel,
    userTag: String,
    onExitClick: () -> Unit
) {
    val supabaseClient = SupabaseClient(
        supabaseUrl = "your-supabase-url",
        supabaseKey = "your-supabase-key"
    )
    val auth = supabaseClient.auth
    val context = LocalContext.current
    val navigator = LocalNavigator.currentOrThrow

    // Get the user from the ViewModel, if not found, assign a default user
    val user = userViewModel.userData.collectAsState().value.find { it.usertag == userTag }
        ?: User(
            user = "user1@demo.com",
            usertag = "User Tag: Default",
            teams = emptyList(),
            description = "Default description",
            age = 0,  // Provide missing parameters
            firstname = "John",
            lastname = "Doe",
            password = "password",
            username = "defaultUsername"
        )

    val teams = teamViewModel.teams.collectAsState().value
    var description by remember { mutableStateOf(user.teams.joinToString(", ") { it }) }

    // Wrapping the entire content in a LazyColumn for scrolling
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp),  // Removed padding
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        item {
            // Profile Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Image Section
                Image(
                    painter = painterResource(id = R.drawable.ic_team_tag),
                    contentDescription = "User Image",
                    modifier = Modifier
                        .width(120.dp)
                        .height(150.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, Color.Transparent, RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))

                // User Info & Buttons
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Row for User Tag and Edit button
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = user.username,  // Changed from playertag to usertag
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        // Edit Button
                        Button(
                            onClick = { /* Handle Edit Click */ },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, Color.Transparent)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_edit),
                                contentDescription = "Edit",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    // Row for LFT and Public buttons
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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
        }

        item {
            Spacer(modifier = Modifier.height(4.dp))

            TextField(
                value = description,
                onValueChange = { newDesc -> description = newDesc },
                placeholder = { Text("Enter user description") },  // Changed from player to user
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
        }

        item {
            // Teams Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Teams", style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold))
                Text("${teams.size}/10", style = TextStyle(fontSize = 16.sp))
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            teams.forEach { team ->
                TeamItem(team = team)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))

            // Settings Section

            Spacer(modifier = Modifier.height(8.dp))

            // Exit Button
            Button(
                onClick = onExitClick,
                modifier = Modifier.fillMaxWidth(),
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

            Spacer(modifier = Modifier.height(8.dp))

            //Delete Account
            Button(
                onClick = {
                    auth.signOut()
                    Toast.makeText(context, "Successfully Deleted", Toast.LENGTH_SHORT).show()
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
                Text("Delete Account", fontSize = 12.sp)
            }
        }
    }
}
