package com.example.bot_lobby.ui.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.example.bot_lobby.R // Ensure this imports your resources, specifically your `ic_teams.png`
import com.example.bot_lobby.ui.screens.TeamsScreen

object TeamsTab: Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = "Teams"
            val icon = painterResource(id = R.drawable.ic_teams) // Use ic_teams.png from the drawable resources

            return remember {
                TabOptions(
                    index = 1u, // Change to a different index than HomeTab
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        TeamsScreen() // Load the TeamsScreen
    }
}
