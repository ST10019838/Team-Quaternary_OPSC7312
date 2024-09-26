package com.example.bot_lobby.ui.pages

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.example.bot_lobby.ui.screens.ScoutingScreen

object ScoutingTab: Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = "Scouting"
            val icon = rememberVectorPainter(Icons.Default.Visibility) // Change Icon

            return remember {
                TabOptions(
                    index = 4u, // Change
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        ScoutingScreen()
    }
}