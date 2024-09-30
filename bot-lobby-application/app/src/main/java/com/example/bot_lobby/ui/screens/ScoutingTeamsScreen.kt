package com.example.bot_lobby.ui.screens

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
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bot_lobby.models.IdAndRole
import com.example.bot_lobby.models.Team
import com.example.bot_lobby.ui.composables.FullScreenModal
import com.example.bot_lobby.ui.composables.ScoutTeamListItem
import com.example.bot_lobby.ui.composables.TeamProfile
import com.example.bot_lobby.ui.theme.BlackCursor
import com.example.bot_lobby.ui.theme.FocusedContainerGray
import com.example.bot_lobby.ui.theme.UnfocusedContainerGray
import com.example.bot_lobby.view_models.TeamViewModel

@Composable
fun ScoutingTeamsScreen(teamViewModel: TeamViewModel = viewModel()) {
    // State variables observed from the TeamViewModel
    val searchQuery by teamViewModel.searchQuery.collectAsState()
    val isSearching by teamViewModel.isSearching.collectAsState()
    val searchedTeams by teamViewModel.searchedTeams.collectAsState()
    val searchError by teamViewModel.searchError.collectAsState()

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
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically // Ensure correct alignment
        ) {
            // Search TextField for scouting team search
            TextField(
                value = searchQuery,  // Bind the search query state to the input field
                onValueChange = { teamViewModel.updateSearchQuery(it) },  // Update the search query
                placeholder = { Text(text = "Search a Team's Tag") },  // Placeholder for the search bar
                modifier = Modifier
                    .weight(1f)  // Use available width
                    .padding(end = 4.dp),  // Right padding
                shape = RoundedCornerShape(16.dp),  // Rounded shape
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = FocusedContainerGray,  // Custom theme colors
                    unfocusedContainerColor = UnfocusedContainerGray,
                    cursorColor = BlackCursor,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)  // Action when search is pressed
            )

            // Search Icon
            IconButton(onClick = {
                teamViewModel.searchForTeams()
            }, enabled = searchQuery.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Scout Team Icon"
                )
            }

            // Refresh Icon to clear the scouting team search
            IconButton(onClick = {
                teamViewModel.clearSearchQuery()

                teamViewModel.createTeam(
                    Team(
                        name = "NEW TEAM",
                        tag = "NT",
                        userIdsAndRoles = listOf(IdAndRole(id = 1, role = "ADMIN"))
                    )
                )

            }) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Clear Scout Team Search Icon"
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Display the number of players found after filtering
        Text(
            "Teams found: ${if (searchedTeams.isNullOrEmpty()) 0 else searchedTeams?.size}",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))  // Add space between search bar and team list


        // Player List within LazyColumn for scrolling through players
        if (searchQuery.isEmpty()) {
            Text("Enter a Team's Name to Search.")
        } else if (!searchError.isNullOrEmpty()) {
            searchError?.let { Text(it) }
        } else if (isSearching) {
            Text("Searching...")
        } else if (searchedTeams?.isEmpty() == true) {
            Text("No Teams Found")
        } else if (searchedTeams?.isNotEmpty() == true) {
            Box(modifier = Modifier.weight(1f)) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    items(searchedTeams!!) { team ->
                        // Pass navController to PlayerListItem to enable navigation
                        ScoutTeamListItem(
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
            TeamProfile(team = teamToView!!)
        }
    }
}
