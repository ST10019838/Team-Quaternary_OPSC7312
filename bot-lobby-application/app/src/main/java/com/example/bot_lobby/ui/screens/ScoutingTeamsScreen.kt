package com.example.bot_lobby.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bot_lobby.models.IdAndRole
import com.example.bot_lobby.models.Team
import com.example.bot_lobby.ui.composables.FullScreenModal
import com.example.bot_lobby.ui.composables.ScoutTeamListItem
import com.example.bot_lobby.ui.composables.TeamListItem
import com.example.bot_lobby.ui.composables.TeamProfile
import com.example.bot_lobby.ui.theme.BlackCursor
import com.example.bot_lobby.ui.theme.BlueStandard
import com.example.bot_lobby.ui.theme.FocusedContainerGray
import com.example.bot_lobby.ui.theme.UnfocusedContainerGray
import com.example.bot_lobby.view_models.AnnouncementViewModel
import com.example.bot_lobby.view_models.SessionViewModel
import com.example.bot_lobby.view_models.TeamViewModel
import com.example.bot_lobby.view_models.UserViewModel

@Composable
fun ScoutingTeamsScreen(teamViewModel: TeamViewModel = viewModel()) {
    val context = LocalContext.current
    val sessionViewModel = viewModel { SessionViewModel(context) }
    val session by sessionViewModel.session.collectAsState()
    val userViewModel = viewModel<UserViewModel>()
    val announcementViewModel = viewModel { AnnouncementViewModel(context) }


    // State variables observed from the TeamViewModel
    val searchQuery by TeamViewModel.searchQuery.collectAsState()
    val isSearching by TeamViewModel.isSearching.collectAsState()
    val searchedTeams by TeamViewModel.searchedTeams.collectAsState()
    val searchError by TeamViewModel.searchError.collectAsState()

    var isDialogOpen by remember { mutableStateOf(false) }
    var teamToView by remember { mutableStateOf<Team?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize()
            .padding(2.dp)
    ) {
        // Search bar for scouting teams
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 0.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically // Ensure correct alignment
        ) {
            // Search TextField for scouting team search
            TextField(
                value = searchQuery,  // Bind the search query state to the input field
                onValueChange = { TeamViewModel.updateSearchQuery(it) },  // Update the search query
                placeholder = { Text(text = "Search a Team's Name") },  // Placeholder for the search bar
                modifier = Modifier
                    .weight(1f)  // Use available width
                    .padding(end = 4.dp),  // Right padding
                shape = RoundedCornerShape(16.dp),  // Rounded shape
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = BlueStandard,  // Custom theme colors
                    unfocusedContainerColor = BlueStandard,
                    cursorColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedPlaceholderColor = Color.White,
                    unfocusedPlaceholderColor = Color.White
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)  // Action when search is pressed
            )

            // Search Icon
            IconButton(onClick = {
                TeamViewModel.clearSearchQuery()
            }, enabled = searchQuery.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear Search"
                )
            }

            // Refresh Icon to clear the scouting team search
//            IconButton(onClick = {
//                teamViewModel.clearSearchQuery()
//
////                teamViewModel.createTeam(
////                    Team(
////                        name = "NEW TEAM",
////                        tag = "NT",
////                        userIdsAndRoles = listOf(IdAndRole(id = 1, role = "ADMIN"))
////                    )
////                )
//
//            }) {
//                Icon(
//                    imageVector = Icons.Default.Refresh,
//                    contentDescription = "Clear Scout Team Search Icon"
//                )
//            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Display the number of players found after filtering
        Text(
            "Teams found: ${if (searchedTeams.isNullOrEmpty()) 0 else searchedTeams?.size}",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))  // Add space between search bar and team list


        // Player List within LazyColumn for scrolling through players
        if (isSearching) {
            Text("Searching...")
        } else if (searchQuery.isEmpty()) {
            Text("Enter a Team's Name to Search.")
        } else if (!searchError.isNullOrEmpty()) {
            searchError?.let { Text(it) }
        } else if (searchedTeams?.isEmpty() == true) {
            Text("No Teams Found")
        } else if (searchedTeams?.isNotEmpty() == true) {
            Box(modifier = Modifier.weight(1f)) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(searchedTeams!!) { team ->
                        // Pass navController to PlayerListItem to enable navigation
                        TeamListItem(
                            team = team,
                            onView = {
                                isDialogOpen = true
                                teamToView = team
                            }
                        )  // Pass the navController to each item
                    }
                }
            }
        }
    }

    if (isDialogOpen) {
        FullScreenModal(onClose = {
            isDialogOpen = false
            teamToView = null
        }) {
//

            TeamProfile(
                team = teamToView!!,
                canJoin = teamToView!!.userIdsAndRoles?.firstOrNull { it.id == session?.userLoggedIn?.id } === null,// true true can join if you are not already a member
                onJoin = {
                    teamViewModel.getTeam(teamToView?.id!!) { teamToUpdate ->
                        val updatedIdsAndRoles = teamToUpdate.userIdsAndRoles?.plus(
                            IdAndRole(
                                id = session?.userLoggedIn?.id!!,
                                isOwner = false
                            )
                        )

                        val updatedTeam = Team(
                            id = teamToUpdate.id,
                            tag = teamToUpdate.tag,
                            name = teamToUpdate.name,
                            bio = teamToUpdate.bio,
                            isPublic = teamToUpdate.isPublic,
                            isLFM = teamToUpdate.isLFM,
                            isOpen = teamToUpdate.isOpen,
                            userIdsAndRoles = updatedIdsAndRoles,
                            maxNumberOfUsers = teamToUpdate.maxNumberOfUsers
                        )


                        teamViewModel.updateTeam(updatedTeam)

                        announcementViewModel.subscribeToTeamAnnouncements(updatedTeam.id)


                        sessionViewModel.addTeamToUser(updatedTeam) {
                            if (it != null) {
                                userViewModel.updateUser(it)
                            }
                        }


                        isDialogOpen = false

                        Toast.makeText(
                            context,
                            "Successfully Joined Team",
                            Toast.LENGTH_SHORT
                        )
                            .show()  // Show a confirmation toast
                    }
                },

                )
        }
    }
}
