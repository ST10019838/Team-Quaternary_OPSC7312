package com.example.bot_lobby.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bot_lobby.ui.components.TeamListItem
import com.example.bot_lobby.ui.viewmodels.TeamViewModel
import com.example.bot_lobby.ui.composables.TeamsHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamsScreen() {
    // Get the TeamViewModel instance
    val teamViewModel: TeamViewModel = viewModel()

    // Collect the list of filtered teams from the view model
    val teams = teamViewModel.filteredTeams.collectAsState()

    // Get the total number of teams
    val totalTeams = teams.value.size

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

            Spacer(modifier = Modifier.height(4.dp))

            // Loop through each team and display the team list item
            teams.value.forEach { team ->
                TeamListItem(team = team) // No navController needed here
                Spacer(modifier = Modifier.height(8.dp)) // Add spacing between team items
            }
        }

        // Floating button in the bottom-right corner with a plus sign
        FloatingActionButton(
            onClick = {
                // Handle the button click event here
            },
            modifier = Modifier
                .align(Alignment.BottomEnd) // Align to bottom-right corner
                .padding(16.dp),
            containerColor = Color.White, // Set the background color to white
            contentColor = Color.Black // Set the plus sign color to black
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add New Team"
            )
        }
    }
}
