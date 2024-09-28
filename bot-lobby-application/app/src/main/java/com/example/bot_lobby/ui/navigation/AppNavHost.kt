package com.example.bot_lobby.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bot_lobby.ui.screens.ScoutingScreen
import com.example.bot_lobby.ui.screens.TeamProfileScreen
import com.example.bot_lobby.ui.screens.PlayerProfileScreen // Updated to use PlayerProfileScreen
import com.example.bot_lobby.ui.viewmodels.TeamViewModel
import com.example.bot_lobby.ui.viewmodels.PlayerViewModel

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(), // Ensure NavHostController is passed
    teamViewModel: TeamViewModel, // Include view models
    playerViewModel: PlayerViewModel,
    onExitClick: () -> Unit // Provide exit function
) {
    // Define the NavHost and the routes for your app
    NavHost(navController = navController, startDestination = "scouting") {
        // ScoutingScreen composable where we pass the navController
        composable("scouting") {
            ScoutingScreen(navController = navController) // Pass navController to ScoutingScreen
        }

        // TeamProfileScreen composable with a dynamic teamTag passed from the navigation
        composable("team_profile/{teamTag}") { backStackEntry ->
            val teamTag = backStackEntry.arguments?.getString("teamTag") ?: "Unknown Team"
            TeamProfileScreen(
                teamTag = teamTag,
                teamViewModel = teamViewModel, // Pass teamViewModel
                onExitClick = onExitClick // Pass the onExitClick handler
            )
        }

        // PlayerProfileScreen composable with a dynamic playerTag passed from the navigation
        composable("player_profile/{playerTag}") { backStackEntry ->
            val playerTag = backStackEntry.arguments?.getString("playerTag") ?: "Player Tag 1"
            PlayerProfileScreen( // Use PlayerProfileScreen for player profile
                playerTag = playerTag,
                playerViewModel = playerViewModel, // Pass playerViewModel
                teamViewModel = teamViewModel, // Pass teamViewModel as required
                onExitClick = onExitClick // Handle exit
            )
        }
    }
}
