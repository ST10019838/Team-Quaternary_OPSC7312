package com.example.bot_lobby.ui.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.example.bot_lobby.R
import com.example.bot_lobby.ui.screens.ProfileScreen
import com.example.bot_lobby.ui.viewmodels.PlayerViewModel
import com.example.bot_lobby.ui.viewmodels.TeamViewModel

// Ensure this object is part of your tab navigation system
object ProfileTab: Tab {

    override val options: TabOptions
        @Composable
        get() {
            val title = "Profile"
            val icon = painterResource(id = R.drawable.ic_profile) // Ensure ic_profile.png exists in your drawable folder

            return remember {
                TabOptions(
                    index = 2u, // Ensure the index is unique across tabs
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        // Assuming you have access to ViewModels, pass them to the screen
        val playerViewModel = remember { PlayerViewModel() } // Replace with actual instance
        val teamViewModel = remember { TeamViewModel() }     // Replace with actual instance
        val playerTag = "yourPlayerTag" // Replace with actual player tag, possibly fetched dynamically

        ProfileScreen(
            playerViewModel = playerViewModel,
            teamViewModel = teamViewModel,
            playerTag = playerTag,
            onExitClick = {
                // Handle the exit action, such as navigating back or showing a dialog
            }
        )
    }
}
