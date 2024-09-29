package com.example.bot_lobby.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bot_lobby.R
import com.example.bot_lobby.models.Member
import com.example.bot_lobby.models.Team
import com.example.bot_lobby.ui.components.PlayerItem
import com.example.bot_lobby.ui.viewmodels.TeamViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamProfileScreen(
    teamTag: String,
    teamViewModel: TeamViewModel,
    onExitClick: () -> Unit
) {
    // Retrieve the team using the provided teamTag
    val team = teamViewModel.teams.collectAsState().value.find { it.teamtag == teamTag }
        ?: Team(
            teamtag = "Default Team Tag",
            teamname = "Default Team Name",
            members = emptyList(),
            isPublic = true
        )

    // This was taken form the following website to use the mutualstateof function
    // https://medium.com/@ah.shubita/jetpack-compose-remember-mutablestateof-derivedstateof-and-remembersaveable-explained-b6ede7fed673
    // Ahmad Shubita
    // https://medium.com/@ah.shubita

    // State for the team description
    var description by remember { mutableStateOf("Description of the team") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),  // Overall padding
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. Heading: Team Tag
        Text(
            text = team.teamtag,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        // 2. Horizontal divider under the heading
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(color = Color.Gray, thickness = 1.dp)
        Spacer(modifier = Modifier.height(8.dp))

        // 3. Two columns layout
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            // First Column (1f): Team Image
            Column(
                modifier = Modifier
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_team_tag),
                    contentDescription = "Team Image",
                    modifier = Modifier
                        .width(150.dp)
                        .height(200.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Second Column (2f): Team Details
            Column(
                modifier = Modifier
                    .weight(2f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 4a. Team Name
                Text(
                    text = team.teamname,
                    fontSize = 14.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                // 4b. Members count
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Members",
                        fontSize = 12.sp,
                        color = Color.Black
                    )
                    Text(
                        text = "${team.members.size} / 10",  // Assuming a max of 10 members
                        fontSize = 12.sp,
                        color = Color.Black
                    )
                }

                // 4c. Join Button
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        // Handle Join action
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    border = BorderStroke(1.dp, Color.Gray),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_square_plus),
                        contentDescription = "Join Icon",
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Join")
                }

                // 4d. Looking for Members Button
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        // Handle "Looking for Members" action
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    border = BorderStroke(1.dp, Color.Gray),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Check Icon",
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Looking for Members")
                }
            }
        }

        // 5. Team Description Input Box
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = description,
            onValueChange = { newValue -> description = newValue },
            placeholder = { Text("Team Description", color = Color.Gray) },
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
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

        // 6. Buttons: Open and Public
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // 6a. Open Button
            Button(
                onClick = {
                    // Handle Open action
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                border = BorderStroke(1.dp, Color.Gray),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_open_book),
                    contentDescription = "Open Icon",
                    tint = Color.Black,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Open")
            }

            Spacer(modifier = Modifier.width(8.dp))

            // 6b. Public Button
            Button(
                onClick = {
                    // Handle Public action
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                border = BorderStroke(1.dp, Color.Gray),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Public,
                    contentDescription = "Public Icon",
                    tint = Color.Black,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Public")
            }
        }

        // 7. List of Players in the Team
        Spacer(modifier = Modifier.height(8.dp))
        team.members.forEach { member ->
            PlayerItem(member = member, onProfileClick = {
                // Handle profile click
            })
            Spacer(modifier = Modifier.height(8.dp))
        }

        // 8. Exit Button at the bottom
        Spacer(modifier = Modifier.weight(1f))  // Pushes the exit button to the bottom
        Button(
            onClick = onExitClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            border = BorderStroke(1.dp, Color.Gray),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = "X",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


