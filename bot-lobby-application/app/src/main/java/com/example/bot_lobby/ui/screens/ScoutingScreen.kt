package com.example.bot_lobby.ui.screens

import androidx.compose.foundation.layout.* // Import layout components
import androidx.compose.material3.* // Import Material Design 3 components
import androidx.compose.runtime.* // Import Compose runtime state functions
import androidx.compose.ui.Modifier // Import Modifier for UI configurations
import androidx.compose.ui.unit.dp // Import dp for padding and size units
import androidx.navigation.NavController // Import NavController for navigation
import com.example.bot_lobby.ui.pages.PlayersTab // Import PlayersTab composable
import com.example.bot_lobby.ui.pages.ScoutTeamsTab // Ensure correct import for ScoutTeamsTab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoutingScreen(navController: NavController) {
    // Define the available tabs: "Players" and "Teams"
    val tabs = listOf("Players", "Teams")

    // This was taken form the following website to use the mutualstateof function
    // https://medium.com/@ah.shubita/jetpack-compose-remember-mutablestateof-derivedstateof-and-remembersaveable-explained-b6ede7fed673
    // Ahmad Shubita
    // https://medium.com/@ah.shubita

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
                    // "Players" Tab Navigation Item
                    NavigationBarItem(
                        icon = {}, // No icon for Players tab
                        label = { Text("Players") }, // Tab label
                        selected = selectedTabIndex == 0, // Check if "Players" tab is selected
                        onClick = { selectedTabIndex = 0 } // Switch to Players tab when clicked
                    )

                    // "Teams" Tab Navigation Item
                    NavigationBarItem(
                        icon = {}, // No icon for Teams tab
                        label = { Text("Teams") }, // Tab label
                        selected = selectedTabIndex == 1, // Check if "Teams" tab is selected
                        onClick = { selectedTabIndex = 1 } // Switch to Teams tab when clicked
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
                // Switch content based on selectedTabIndex (0: Players, 1: Teams)
                when (selectedTabIndex) {
                    0 -> PlayersTab(navController = navController)  // Pass navController to PlayersTab
                    1 -> ScoutTeamsTab(navController = navController)  // Fix the reference to ScoutTeamsTab and pass navController
                }
            }
        }
    }
}
