package com.example.bot_lobby.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bot_lobby.ui.viewmodels.PlayerViewModel
import com.example.bot_lobby.ui.viewmodels.TeamViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    playerViewModel: PlayerViewModel,
    teamViewModel: TeamViewModel,
    playerTag: String,
    onExitClick: () -> Unit
) {
    // Define the available tabs: "PlayerProfile" and "Settings"
    val tabs = listOf("PlayerProfile", "Settings")

    // State to track which tab is currently selected
    var selectedTabIndex by remember { mutableStateOf(0) }

    // Scaffold provides a structured layout with support for top bars, bottom bars, etc.
    Scaffold(
        bottomBar = {
            // Bottom Navigation Bar for switching between tabs
            Box(Modifier.padding(bottom = 0.dp)) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface, // Navigation bar background color
                    contentColor = MaterialTheme.colorScheme.onSurface, // Navigation bar text color
                    modifier = Modifier.height(48.dp) // Custom height for the navigation bar (default is 56.dp)
                ) {
                    // "PlayerProfile" Tab Navigation Item
                    NavigationBarItem(
                        icon = { }, // No icon for PlayerProfile tab
                        label = { Text("PlayerProfile") }, // Tab label
                        selected = selectedTabIndex == 0, // Check if "PlayerProfile" tab is selected
                        onClick = {
                            selectedTabIndex = 0
                        } // Switch to PlayerProfile tab when clicked
                    )

                    // "Settings" Tab Navigation Item
                    NavigationBarItem(
                        icon = {}, // No icon for Settings tab
                        label = { Text("Settings") }, // Tab label
                        selected = selectedTabIndex == 1, // Check if "Settings" tab is selected
                        onClick = { selectedTabIndex = 1 } // Switch to Settings tab when clicked
                    )
                }
            }
        }
    ) { paddingValues ->
        // Main content area for displaying the selected tab content
        Column(
            modifier = Modifier
                .fillMaxSize() // Take up the full screen space
                .padding(0.dp) // Remove padding for top and sides
        ) {
            // Box to contain the content of the currently selected tab
            Box(modifier = Modifier.weight(1f)) {
                // Switch content based on selectedTabIndex (0: PlayerProfile, 1: Settings)
                when (selectedTabIndex) {
                    0 -> PlayerProfileTab(
                        playerViewModel = playerViewModel,
                        teamViewModel = teamViewModel,
                        playerTag = playerTag,
                        onExitClick = onExitClick
                    )

                    1 -> SettingsTab()
                }
            }
        }
    }
}
