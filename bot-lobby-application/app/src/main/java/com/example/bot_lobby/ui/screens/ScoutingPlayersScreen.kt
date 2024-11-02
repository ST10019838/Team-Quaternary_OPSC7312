package com.example.bot_lobby.ui.screens

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bot_lobby.R
import com.example.bot_lobby.models.User
import com.example.bot_lobby.ui.composables.FullScreenModal
import com.example.bot_lobby.ui.composables.PlayerListItem
import com.example.bot_lobby.ui.composables.PlayerProfile
import com.example.bot_lobby.ui.theme.BlueStandard
import com.example.bot_lobby.view_models.AuthViewModel
import com.example.bot_lobby.view_models.TeamViewModel
import com.example.bot_lobby.view_models.UserViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ScoutingPlayersScreen(
    userViewModel: UserViewModel = viewModel(), // Initialize PlayerViewModel
    teamViewModel: TeamViewModel = viewModel() // Initialize TeamViewModel
) {
    // Collect searchQuery and filtered players from the PlayerViewModel
    val searchQuery by userViewModel.searchQuery.collectAsState()
    val isSearching by userViewModel.isSearching.collectAsState()
    val searchedUsers by userViewModel.searchedUsers.collectAsState()
    val searchError by userViewModel.searchError.collectAsState()

    // Collect teams from the TeamViewModel
    val teams by AuthViewModel.usersTeams.collectAsState()

    // Focus manager for clearing the focus when search is triggered
    val focusManager = LocalFocusManager.current
    var isDialogOpen by remember { mutableStateOf(false) }
    var userToView by remember { mutableStateOf<User?>(null) }

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
                .padding(top = 0.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Search TextField for searching players by tag
            TextField(
                value = searchQuery,
                onValueChange = { userViewModel.updateSearchQuery(it) },
                placeholder = { Text(stringResource(id = R.string.search_player_tag)) },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 4.dp),
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = { focusManager.clearFocus() }
                ),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = BlueStandard,
                    unfocusedContainerColor = BlueStandard,
                    cursorColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedPlaceholderColor = Color.White,
                    unfocusedPlaceholderColor = Color.White
                )
            )

            // Search IconButton to trigger search
            IconButton(
                onClick = {
                    focusManager.clearFocus()
                    Log.d("PlayersTab", "Search triggered")
                    userViewModel.searchForUsers()
                },
                enabled = searchQuery.isNotEmpty()
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(id = R.string.search_player_tag)
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Display the number of players found after filtering
        Text(
            stringResource(id = R.string.players_found, searchedUsers?.size ?: 0),
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Player List within LazyColumn for scrolling through players
        when {
            searchedUsers == null -> {
                Text(stringResource(id = R.string.enter_player_name))
            }
            !searchError.isNullOrEmpty() -> {
                Text(searchError ?: "")
            }
            isSearching -> {
                Text(stringResource(id = R.string.searching))
            }
            searchedUsers.isEmpty() -> {
                Text(stringResource(id = R.string.no_players_found))
            }
            else -> {
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(searchedUsers) { user ->
                            PlayerListItem(user = user, teams = teams, canView = true, onView = {
                                isDialogOpen = true
                                userToView = user
                            })
                        }
                    }
                }
            }
        }
    }

    if (isDialogOpen) {
        FullScreenModal(onClose = {
            isDialogOpen = false
            userToView = null
        }) {
            PlayerProfile(user = userToView!!)
        }
    }
}
