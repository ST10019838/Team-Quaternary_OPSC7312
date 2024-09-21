package com.example.bot_lobby.ui.screens

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.example.bot_lobby.ui.composables.ScreenLayout
import com.example.bot_lobby.ui.pages.HomeTab

class LandingScreen() : Screen {
    @Composable
    override fun Content() {
        TabNavigator(HomeTab) { tabNavigator ->
            ScreenLayout(tabNavigator)
        }
    }
}