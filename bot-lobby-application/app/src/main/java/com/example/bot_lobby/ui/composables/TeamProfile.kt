package com.example.bot_lobby.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.PublicOff
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.bot_lobby.R
import com.example.bot_lobby.models.Team
import com.example.bot_lobby.models.User
import com.example.bot_lobby.view_models.AuthViewModel
import com.example.bot_lobby.view_models.TeamViewModel
import com.example.bot_lobby.view_models.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamProfile(
    team: Team,
    canEdit: Boolean = false,
) {
    // Retrieve the teams players
    val userViewModel = UserViewModel()
    val teamViewModel = TeamViewModel()

    var users by remember { mutableStateOf<List<User>?>(null) }
    var error: String? by remember { mutableStateOf(null) }

//    var teamsUsers by remember { mutableStateOf<FetchResponse<List<User>>?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    var teamTag by remember { mutableStateOf(team.tag) }
    var teamName by remember { mutableStateOf(team.name) }
    var teamBio by remember { mutableStateOf(team.bio) }
    var teamIsOpen by remember { mutableStateOf(team.isOpen) }
    var teamIsPublic by remember { mutableStateOf(team.isPublic) }
    var teamIsLFM by remember { mutableStateOf(team.isPublic) }



    LaunchedEffect(true) {
        isLoading = true

        val response = userViewModel.getTeamsUsers(team)

        if (response.errors.isNullOrEmpty()) {
            users = response.data
        } else {
            error = response.errors
        }

        isLoading = false
    }


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. Heading: Team Tag
//        Text(
//            text = teamTag,
//            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
//            modifier = Modifier.align(Alignment.CenterHorizontally)
//        )

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = teamName,
            onValueChange = { teamName = it },
            readOnly = !canEdit,
            label = { Text("Team Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // 2. Horizontal divider under the heading
//        Spacer(modifier = Modifier.height(10.dp))
//        HorizontalDivider(color = Color.Gray, thickness = 1.dp)
        Spacer(modifier = Modifier.height(16.dp))

        // 3. Two columns layout
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // First Column (1f): Team Image
            Image(
                painter = painterResource(id = R.drawable.ic_team_tag),
                contentDescription = "Team Image",
                modifier = Modifier
                    .width(130.dp)
                    .height(130.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Second Column (2f): Team Details
            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically),
                verticalArrangement = Arrangement.SpaceAround,
            ) {
                // 4a. Team Name
//                Text(
//                    text = team.name,
//                    fontSize = 14.sp,
//                    color = Color.Black,
//                    fontWeight = FontWeight.Bold,
//                    modifier = Modifier.align(Alignment.CenterHorizontally)
//                )

                OutlinedTextField(
                    value = teamTag,
                    onValueChange = { teamTag = it },
                    readOnly = !canEdit,
                    label = { Text("Team Tag") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // 4b. Members count
//                Spacer(modifier = Modifier.height(8.dp))


                // 4c. Join Button
//                Spacer(modifier = Modifier.height(8.dp))
//                Button(
//                    onClick = {
//                        // Handle Join action
//                    },
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = Color.White,
//                        contentColor = Color.Black
//                    ),
//                    border = BorderStroke(1.dp, Color.Gray),
//                    shape = RoundedCornerShape(8.dp),
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Icon(
//                        painter = painterResource(id = R.drawable.ic_square_plus),
//                        contentDescription = "Join Icon",
//                        tint = Color.Black,
//                        modifier = Modifier.size(20.dp)
//                    )
//                    Spacer(modifier = Modifier.width(4.dp))
//                    Text(text = "Join")
//                }

                // 4d. Looking for Members Button
//                Spacer(modifier = Modifier.height(8.dp))
                AssistChip(
                    onClick = { if (canEdit) teamIsLFM = !teamIsLFM },
                    label = { if (teamIsLFM) Text(text = "LFM") else Text(text = "Not LFM") },
                    trailingIcon = {
                        if (teamIsLFM) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Team is LFM",
                                modifier = Modifier.size(20.dp)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Team Is Not LFM",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 5. Team Description Input Box
        OutlinedTextField(
            label = { Text("Team Bio") },
            value = if (teamBio.isNullOrEmpty()) "" else teamBio!!,
            readOnly = !canEdit,
            onValueChange = { teamBio = it },  // Update description state
            placeholder = { Text("Enter Team Description") },  // Placeholder text
            modifier = Modifier
                .fillMaxWidth(),
//                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))  // Border styling
//                .background(Color.White),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),  // Keyboard options
            singleLine = false,
            maxLines = 3,
//            shape = RoundedCornerShape(8.dp),
//            colors = TextFieldDefaults.colors(
//                focusedContainerColor = Color.White,
//                unfocusedContainerColor = Color.White,
//                cursorColor = Color.Black,
//                focusedIndicatorColor = Color.Transparent,
//                unfocusedIndicatorColor = Color.Transparent,
//                disabledIndicatorColor = Color.Transparent
//            )
        )

        // 6. Buttons: Open and Public
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // 6a. Open Button
            AssistChip(
                onClick = { if (canEdit) teamIsOpen = !teamIsOpen },
                label = { if (teamIsOpen) Text(text = "Open") else Text(text = "Closed") },
                trailingIcon = {
                    if (teamIsOpen) {
                        Icon(
                            imageVector = Icons.Default.LockOpen,
                            contentDescription = "Open Icon",
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Closed Icon",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                modifier = Modifier
                    .weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // 6b. Public Button
            AssistChip(
                onClick = { if (canEdit) teamIsPublic = !teamIsPublic },
                label = { if (teamIsPublic) Text(text = "Public") else Text(text = "Private") },
                trailingIcon = {
                    if (teamIsPublic) {
                        Icon(
                            imageVector = Icons.Default.Public,
                            contentDescription = "Team is Public",
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.PublicOff,
                            contentDescription = "Team is Private",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                modifier = Modifier
                    .weight(1f)
            )
        }

        Spacer(Modifier.height(20.dp))


        if (canEdit) {
            Button(
                onClick = {
                    val updatedTeam = Team(
                        id = team.id,
                        tag = teamTag,
                        name = teamName,
                        bio = teamBio,
                        isPublic = teamIsPublic,
                        isLFM = teamIsLFM,
                        isOpen = teamIsOpen,
                        userIdsAndRoles = team.userIdsAndRoles,
                        maxNumberOfUsers = team.maxNumberOfUsers
                    )

                    AuthViewModel.updateUsersTeam(updatedTeam)

                    teamViewModel.updateTeam(updatedTeam)
                },
//            colors = ButtonDefaults.buttonColors(
//                containerColor = Color.Red,
//                contentColor = Color.White
//            ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(imageVector = Icons.Default.SaveAlt, contentDescription = "Save Changes")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Save Changes", style = MaterialTheme.typography.bodyLarge)
            }
        }


        Spacer(Modifier.height(25.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Members",
                color = Color.Black,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "${team.userIdsAndRoles.size} / 10",  // Assuming a max of 10 members
                color = Color.Black,
                style = MaterialTheme.typography.titleLarge
            )
        }

        Spacer(Modifier.height(8.dp))
        HorizontalDivider(color = Color.Gray, thickness = 1.dp)
        Spacer(Modifier.height(16.dp))

        // Player List within LazyColumn for scrolling through players

        if (isLoading) {
            Text("Loading...")
        } else if (!error.isNullOrEmpty()) {
            error?.let { Text(it) }
        } else if (users.isNullOrEmpty()) {
            Text("No Players Found")
        } else {
            Box(
                modifier = Modifier
                    .weight(1f)
            ) {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    items(users!!) { user ->

                        // Pass navController to PlayerListItem to enable navigation
                        PlayerListItem(user = user, canView = false)
                    }
                }
            }
        }


        // 7. List of Players in the Team
//        Spacer(modifier = Modifier.height(8.dp))

//        Text("$isLoading")

//        teamsUsers?.data?.forEach { user ->
//            PlayerItem(user = user, role = user.role.toString(), onProfileClick = {
//                // Handle profile click
//            })
//            Spacer(modifier = Modifier.height(8.dp))
//        }
    }
}
