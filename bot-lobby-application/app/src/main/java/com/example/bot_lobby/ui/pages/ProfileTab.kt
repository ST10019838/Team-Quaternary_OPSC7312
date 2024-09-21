package com.example.bot_lobby.ui.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.example.bot_lobby.R // Ensure this imports your resources, specifically your `ic_profile.png`
import com.example.bot_lobby.ui.screens.ProfileScreen

object ProfileTab: Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = "Profile"
            val icon = painterResource(id = R.drawable.ic_profile) // Use ic_profile.png from the drawable resources

            return remember {
                TabOptions(
                    index = 2u, // Ensure this index doesn't conflict with other tabs
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        ProfileScreen() // Load the ProfileScreen
    }
}
