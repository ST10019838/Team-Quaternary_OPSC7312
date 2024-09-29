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
import androidx.navigation.NavController
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
    navController: NavController, // Add NavController as a parameter
    playerViewModel: PlayerViewModel = viewModel(), // Initialize PlayerViewModel
    teamViewModel: TeamViewModel = viewModel() // Initialize TeamViewModel
) {

    // This was taken form the following website to use the collectAsState function
    // https://medium.com/androiddevelopers/consuming-flows-safely-in-jetpack-compose-cde014d0d5a3
    // Manuel Vivo
    // https://medium.com/@manuelvicnt

    // Collect searchQuery and filtered players from the PlayerViewModel
    val searchQuery by playerViewModel.searchQuery.collectAsState()
    val filteredPlayers by playerViewModel.filteredPlayers.collectAsState()

    // Collect teams from the TeamViewModel
    val teams by teamViewModel.teams.collectAsState()

    // This was taken form the following website to use the LocalFocusManager function
    // https://medium.com/google-developer-experts/focus-in-jetpack-compose-6584252257fe
    // Jamie Sanson
    // https://medium.com/@jamiesanson

    // Focus manager for clearing the focus when search is triggered
    val focusManager = LocalFocusManager.current

    // Debugging logs to track the filtered players
    Log.d("PlayersTab", "Number of filtered players: ${filteredPlayers.size}")
    filteredPlayers.forEach { Log.d("PlayersTab", "Player: ${it.player}, Tag: ${it.playertag}, Teams: ${it.teams}") }

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

        // This was taken form the following website to use the LazyColumn function
        // https://medium.com/@mal7othify/lists-using-lazycolumn-in-jetpack-compose-c70c39805fbc
        // Maryam Alhuthayfi
        // https://medium.com/@mal7othify
        
        // Player List within LazyColumn for scrolling through players
        Box(modifier = Modifier.weight(1f)) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                items(filteredPlayers) { player ->
                    // Pass navController to PlayerListItem to enable navigation
                    PlayerListItem(player = player, teams = teams, navController = navController)
                }
            }
        }

        // Display message if no players are found
        if (filteredPlayers.isEmpty()) {
            Text("No players found")
        }
    }
}
