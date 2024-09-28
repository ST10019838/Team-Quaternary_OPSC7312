package com.example.bot_lobby.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import com.example.bot_lobby.ui.composables.PlayerProfile
import com.example.bot_lobby.ui.composables.PlayerSettings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
//    playerViewModel: PlayerViewModel,
//    teamViewModel: TeamViewModel,
//    playerTag: String,
//    onExitClick: () -> Unit
) {
    LazyColumn {
        item {
            PlayerProfile()
        }

        item {
            PlayerSettings()
        }
    }
}
