package com.example.bot_lobby.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bot_lobby.models.IdAndRole
import com.example.bot_lobby.models.Team
import com.example.bot_lobby.ui.composables.FullScreenModal
import com.example.bot_lobby.ui.composables.TeamListItem
import com.example.bot_lobby.ui.composables.TeamProfile
import com.example.bot_lobby.ui.composables.TeamsHeader
import com.example.bot_lobby.view_models.AuthViewModel
import com.example.bot_lobby.view_models.TeamViewModel
import com.example.bot_lobby.view_models.UserViewModel
import java.util.UUID


@Composable
fun TeamsScreen() {
    // Get the TeamViewModel instance
    val teamViewModel: TeamViewModel = viewModel()
    val userViewModel: UserViewModel = viewModel()


    // Collect the list of filtered teams from the view model
    val teams = AuthViewModel.usersTeams.collectAsState()

    // Get the total number of teams
    val totalTeams = teams.value.size

    var isDialogOpen by remember { mutableStateOf(false) }
    var teamToView by remember { mutableStateOf<Team?>(null) }


    // Main content area
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Display the TeamsHeader with the number of teams
            TeamsHeader(totalTeams = totalTeams)

            Spacer(modifier = Modifier.height(15.dp))

            LazyColumn {
                items(teams.value) { team ->
                    TeamListItem(team = team, onView = {
                        teamToView = team
                        isDialogOpen = true
                    }) // No navController needed here
                    Spacer(modifier = Modifier.height(8.dp)) // Add spacing between team items
                }

                item {
                    Spacer(modifier = Modifier.height(60.dp))
                }
            }

//            // Loop through each team and display the team list item
//            teams.value.forEach { team ->
//                TeamListItem(team = team) // No navController needed here
//                Spacer(modifier = Modifier.height(8.dp)) // Add spacing between team items
//            }
        }

        // Floating button in the bottom-right corner with a plus sign
        if (teams.value.size < 10) {
            FloatingActionButton(
                onClick = {
                    // Handle the button click event here
                    val user = AuthViewModel.userLoggedIn.value

                    val newTeam = Team(
                        id = UUID.randomUUID(),
                        tag = "EDIT",
                        name = "${user?.username}'s Team ${teams.value.size + 1}",
                        userIdsAndRoles = listOf(IdAndRole(user?.id!!, "Owner")),//List<IdAndRole>
                        isPublic = true,
                        maxNumberOfUsers = 10
                    )

                    teamViewModel.createTeam(newTeam)

                    AuthViewModel.addTeamToUser(newTeam)
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd) // Align to bottom-right corner
                    .padding(16.dp),
                containerColor = MaterialTheme.colorScheme.primary, // Set the background color to white
                contentColor = Color.White  // Set the plus sign color to black
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add New Team"
                )
            }
        }

    }

    if (isDialogOpen) {
        FullScreenModal(onClose = {
            isDialogOpen = false
            teamToView = null
        }) {
            // TODO: change ability to edit team details when more members join
            TeamProfile(team = teamToView!!, canEdit = true)
        }
    }
}
