package com.example.bot_lobby.ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bot_lobby.ui.components.PlayerListItem
import com.example.bot_lobby.ui.viewmodels.PlayerViewModel
import com.example.bot_lobby.ui.viewmodels.TeamViewModel
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.shape.RoundedCornerShape
import android.util.Log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayersTab(
    playerViewModel: PlayerViewModel = viewModel(),
    teamViewModel: TeamViewModel = viewModel() // Get the TeamViewModel
) {
    // Collect state from PlayerViewModel
    val searchQuery by playerViewModel.searchQuery.collectAsState()
    val filteredPlayers by playerViewModel.filteredPlayers.collectAsState()

    // Collect teams from TeamViewModel
    val teams by teamViewModel.teams.collectAsState()

    val focusManager = LocalFocusManager.current

    // Log the filtered players
    Log.d("PlayersTab", "Number of filtered players: ${filteredPlayers.size}")
    filteredPlayers.forEach { Log.d("PlayersTab", "Player: ${it.name}, Tag: ${it.tag}, Team: ${it.team}") }

    // Main Column layout
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize()
            .padding(2.dp)
    ) {
        // Search Bar, Search Icon, and Refresh Button Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 0.dp, bottom = 4.dp), // Reduces the space on top of the search bar
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Search TextField
            TextField(
                value = searchQuery,
                onValueChange = { playerViewModel.updateSearchQuery(it) },
                placeholder = { Text("Search a Player's Tag") },
                modifier = Modifier
                    .weight(1f) // Ensures the search bar takes up as much space as possible
                    .padding(end = 4.dp),
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        focusManager.clearFocus()
                    }
                ),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.LightGray,
                    unfocusedContainerColor = Color.LightGray,
                    cursorColor = Color.Black,
                    focusedIndicatorColor = Color.Transparent, // Removes the focus line
                    unfocusedIndicatorColor = Color.Transparent // Removes the unfocused line
                )
            )

            // Search IconButton
            IconButton(onClick = {
                focusManager.clearFocus()
                Log.d("PlayersTab", "Search triggered")
            }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon"
                )
            }

            // Refresh IconButton to clear the search and reload data
            IconButton(onClick = {
                playerViewModel.updateSearchQuery("") // Clear the search query
                playerViewModel.reloadData() // Reload the initial data
            }) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh Icon"
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Display the number of filtered players
        Text("Players found: ${filteredPlayers.size}", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(4.dp))

        // Player List inside a LazyColumn to make it scrollable
        Box(modifier = Modifier.weight(1f)) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                items(filteredPlayers) { player ->
                    PlayerListItem(player = player, teams = teams) // Pass the teams to PlayerListItem
                }
            }
        }

        // Display "No players found" if the list is empty
        if (filteredPlayers.isEmpty()) {
            Text("No players found")
        }
    }
}
