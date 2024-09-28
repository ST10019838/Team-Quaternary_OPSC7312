package com.example.bot_lobby.ui.screens

import android.util.Log
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bot_lobby.ui.composables.PlayerListItem
import com.example.bot_lobby.ui.viewmodels.PlayerViewModel
import com.example.bot_lobby.ui.viewmodels.TeamViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoutingPlayersScreen(
    playerViewModel: PlayerViewModel = viewModel(), // Initialize PlayerViewModel
    teamViewModel: TeamViewModel = viewModel() // Initialize TeamViewModel
) {
    // Collect searchQuery and filtered players from the PlayerViewModel
    val searchQuery by playerViewModel.searchQuery.collectAsState()
    val filteredPlayers by playerViewModel.filteredPlayers.collectAsState()

    // Collect teams from the TeamViewModel
    val teams by teamViewModel.teams.collectAsState()

    // Focus manager for clearing the focus when search is triggered
    val focusManager = LocalFocusManager.current

    // Debugging logs to track the filtered players
    Log.d("PlayersTab", "Number of filtered players: ${filteredPlayers.size}")
    filteredPlayers.forEach {
        Log.d(
            "PlayersTab",
            "Player: ${it.player}, Tag: ${it.playertag}, Teams: ${it.teams}"
        )
    }

    // Main Column layout to structure the screen
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize()
            .padding(2.dp)
    ) {
        // Row for the Search Bar, Search Icon, and Refresh Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 0.dp, bottom = 4.dp), // Adjusts padding for a tight layout
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Search TextField for searching players by tag
            TextField(
                value = searchQuery, // Binds the searchQuery state
                onValueChange = { playerViewModel.updateSearchQuery(it) }, // Updates searchQuery state
                placeholder = { Text("Search a Player's Tag") }, // Placeholder text for search field
                modifier = Modifier
                    .weight(1f) // Expands the search field to take full available width
                    .padding(end = 4.dp),
                shape = RoundedCornerShape(16.dp), // Rounded corner for aesthetic
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search), // Search keyboard action
                keyboardActions = KeyboardActions(
                    onSearch = { focusManager.clearFocus() } // Clears focus when search is triggered
                ),
                singleLine = true, // Restrict search bar to one line
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.LightGray,
                    unfocusedContainerColor = Color.LightGray,
                    cursorColor = Color.Black,
                    focusedIndicatorColor = Color.Transparent, // Remove default focus underline
                    unfocusedIndicatorColor = Color.Transparent // Remove default unfocused underline
                )
            )

            // Search IconButton to trigger search
            IconButton(onClick = {
                focusManager.clearFocus() // Clears focus when search button is clicked
                Log.d("PlayersTab", "Search triggered")
            }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon" // Describes the search button
                )
            }

            // Refresh IconButton to clear search and reload the data
            IconButton(onClick = {
                playerViewModel.updateSearchQuery("") // Clears the search query
                playerViewModel.reloadData() // Triggers data reload from PlayerViewModel
            }) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh Icon" // Describes the refresh button
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Display the number of players found after filtering
        Text("Players found: ${filteredPlayers.size}", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(4.dp))

        // Player List within LazyColumn for scrolling through players
        Box(modifier = Modifier.weight(1f)) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                items(filteredPlayers) { player ->
                    // Pass navController to PlayerListItem to enable navigation
                    PlayerListItem(player = player, teams = teams)
                }
            }
        }

        // Display message if no players are found
        if (filteredPlayers.isEmpty()) {
            Text("No players found")
        }
    }
}
