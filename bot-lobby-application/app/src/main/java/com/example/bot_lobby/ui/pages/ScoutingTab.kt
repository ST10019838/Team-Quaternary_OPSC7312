package com.example.bot_lobby.ui.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.example.bot_lobby.R // Ensure this imports your resources
import com.example.bot_lobby.ui.screens.ScoutingScreen

object ScoutingTab: Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = "Scouting" // Change title to "Scouting"
            val icon = painterResource(id = R.drawable.ic_scouting) // Use ic_scouting.png from your drawable resources

            return remember {
                TabOptions(
                    index = 1u, // Ensure this index doesn't conflict with other tabs
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        ScoutingScreen() // Load the ScoutingScreen
    }
}
