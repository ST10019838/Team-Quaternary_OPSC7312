package com.example.bot_lobby.ui.screens

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.example.bot_lobby.ui.composables.ScreenLayout
import com.example.bot_lobby.ui.pages.HomeTab
import com.example.bot_lobby.view_models.SessionViewModel

class LandingScreen() : Screen {
    override val key = uniqueScreenKey

    @Composable
    override fun Content() {
//        val context = LocalContext.current
//        val sessionViewModel = viewModel { SessionViewModel(context) }
//        val session by sessionViewModel.session.collectAsStateWithLifecycle()
//        val navigator = LocalNavigator.currentOrThrow
//
//        if(session != null){
//            navigator.popUntilRoot()
//        }

        TabNavigator(HomeTab) { tabNavigator ->
            ScreenLayout(tabNavigator)
        }
    }
}