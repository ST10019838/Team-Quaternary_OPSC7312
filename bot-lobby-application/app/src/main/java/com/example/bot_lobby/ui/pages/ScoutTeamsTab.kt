package com.example.bot_lobby.ui.pages

import androidx.compose.foundation.layout.* // Import layout components
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.* // Import Material Design components
import androidx.compose.runtime.* // Import Compose runtime state functions
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController // Import NavController for navigation
import com.example.bot_lobby.ui.viewmodels.TeamViewModel
import com.example.bot_lobby.ui.components.ScoutTeamListItem
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.bot_lobby.ui.theme.* // Import custom theme

@Composable
fun ScoutTeamsTab(navController: NavController, teamViewModel: TeamViewModel = viewModel()) {
    // State variables observed from the TeamViewModel
    val searchQuery by teamViewModel.searchQuery.collectAsState()
    val filteredTeams by teamViewModel.filteredTeams.collectAsState()

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
            IconButton(onClick = { teamViewModel.updateSearchQuery(searchQuery) }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search Scout Team Icon")
            }

            // Refresh Icon to clear the scouting team search
            IconButton(onClick = { teamViewModel.updateSearchQuery("") }) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = "Clear Scout Team Search Icon")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))  // Add space between search bar and team list

        // List of scout teams
        LazyColumn(
            modifier = Modifier.fillMaxSize()  // Fill the available space
        ) {
            items(filteredTeams) { team ->  // Dynamically populate the list
                ScoutTeamListItem(team = team, navController = navController)  // Pass the navController to each item
            }
        }

        // Display a message when no teams are found
        if (filteredTeams.isEmpty()) {  // Check if no teams are available
            Text(text = "No scout teams found", modifier = Modifier.padding(16.dp))
        }
    }
}
