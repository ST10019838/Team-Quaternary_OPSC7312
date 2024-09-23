package com.example.bot_lobby.ui.screens

import androidx.compose.foundation.layout.* // Import layout components
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bot_lobby.ui.pages.PlayersTab
import com.example.bot_lobby.ui.pages.ScoutTeamsTab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoutingScreen() {
    // Define the tabs
    val tabs = listOf("Players", "Teams")

    // State to hold the selected tab index
    var selectedTabIndex by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            // Bottom Navigation Bar
            Box(Modifier.padding(bottom = 0.dp)) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.height(48.dp) // Make the navigation bar thinner (default is 56.dp)
                ) {
                    NavigationBarItem(
                        icon = {}, // No icon for Players tab
                        label = { Text("Players") },
                        selected = selectedTabIndex == 0,
                        onClick = { selectedTabIndex = 0 }
                    )
                    NavigationBarItem(
                        icon = {}, // No icon for Teams tab
                        label = { Text("Teams") },
                        selected = selectedTabIndex == 1,
                        onClick = { selectedTabIndex = 1 }
                    )
                }
            }
        }
    ) { paddingValues ->
        // Remove paddingValues to avoid extra space on top
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp) // Remove padding for top and sides
        ) {
            // Tab content
            Box(modifier = Modifier.weight(1f)) {
                when (selectedTabIndex) {
                    0 -> PlayersTab()  // PlayersTab content
                    1 -> ScoutTeamsTab()  // ScoutTeamsTab content
                }
            }
        }
    }
}
