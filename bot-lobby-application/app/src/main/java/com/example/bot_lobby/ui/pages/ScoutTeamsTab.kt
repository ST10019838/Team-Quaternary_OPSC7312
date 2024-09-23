package com.example.bot_lobby.ui.pages

import androidx.compose.foundation.layout.* // Import layout components
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bot_lobby.ui.viewmodels.TeamViewModel
import com.example.bot_lobby.ui.components.ScoutTeamListItem
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.bot_lobby.ui.theme.*

@Composable
fun ScoutTeamsTab(teamViewModel: TeamViewModel = viewModel()) {
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
                value = searchQuery,
                onValueChange = { teamViewModel.updateSearchQuery(it) },
                placeholder = { Text(text = "Search a Team's Tag") },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 4.dp),
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = FocusedContainerGray,
                    unfocusedContainerColor = UnfocusedContainerGray,
                    cursorColor = BlackCursor,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
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

        Spacer(modifier = Modifier.height(16.dp))

        // List of scout teams
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredTeams) { team ->
                ScoutTeamListItem(team = team)
            }
        }

        // Display a message when no teams are found
        if (filteredTeams.isEmpty()) {
            Text(text = "No scout teams found", modifier = Modifier.padding(16.dp))
        }
    }
}
