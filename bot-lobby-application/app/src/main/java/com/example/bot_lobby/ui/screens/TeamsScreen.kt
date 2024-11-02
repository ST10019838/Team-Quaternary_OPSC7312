package com.example.bot_lobby.ui.screens

import android.widget.Toast
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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bot_lobby.R
import com.example.bot_lobby.api.RetrofitInstance
import com.example.bot_lobby.models.IdAndRole
import com.example.bot_lobby.models.Team
import com.example.bot_lobby.ui.composables.FullScreenModal
import com.example.bot_lobby.ui.composables.TeamListItem
import com.example.bot_lobby.ui.composables.TeamProfile
import com.example.bot_lobby.ui.composables.TeamsHeader
import com.example.bot_lobby.ui.theme.BlueStandard
import com.example.bot_lobby.view_models.AuthViewModel
import com.example.bot_lobby.view_models.TeamViewModel
import com.example.bot_lobby.view_models.UserViewModel
import java.util.UUID

@Composable
fun TeamsScreen() {
    val teamViewModel: TeamViewModel = viewModel()
    val userViewModel: UserViewModel = viewModel()
    val context = LocalContext.current

    val teams = AuthViewModel.usersTeams.collectAsState()
    val userLoggedIn by AuthViewModel.userLoggedIn.collectAsState()

    val totalTeams = teams.value.size

    var isDialogOpen by remember { mutableStateOf(false) }
    var teamToView by remember { mutableStateOf<Team?>(null) }

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
                    })
                    Spacer(modifier = Modifier.height(8.dp))
                }

                item {
                    Spacer(modifier = Modifier.height(60.dp))
                }
            }
        }

        if (teams.value.size < 10) {
            FloatingActionButton(
                onClick = {
                    val user = AuthViewModel.userLoggedIn.value

                    val newTeam = Team(
                        id = UUID.randomUUID(),
                        tag = "EDIT",
                        name = "${user?.username}'s Team ${teams.value.size + 1}",
                        userIdsAndRoles = listOf(IdAndRole(user?.id!!, "Owner")),
                        isPublic = true,
                        maxNumberOfUsers = 10
                    )

                    AuthViewModel.addTeamToUser(newTeam) {
                        if (it != null) {
                            userViewModel.updateUser(it)
                        }
                    }

                    teamViewModel.createTeam(newTeam) {}

                    val updatedUser = user
                    var updatedTeamIds = user.teamIds?.toMutableList()

                    if (updatedTeamIds == null) {
                        updatedTeamIds = mutableListOf(newTeam.id!!)
                    } else {
                        updatedTeamIds += newTeam.id!!
                    }

                    updatedUser.teamIds = updatedTeamIds.toList()
                    userViewModel.updateUser(updatedUser)

                    Toast.makeText(
                        context,
                        stringResource(id = R.string.team_created_success),
                        Toast.LENGTH_SHORT
                    ).show()
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                containerColor = BlueStandard,
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.register)
                )
            }
        }
    }

    if (isDialogOpen) {
        FullScreenModal(onClose = {
            isDialogOpen = false
            teamToView = null
        }) {
            TeamProfile(team = teamToView!!, canEdit = true)
        }
    }
}
