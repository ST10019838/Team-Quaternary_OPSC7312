package com.example.bot_lobby.ui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bot_lobby.ui.composables.PlayerProfile
import com.example.bot_lobby.ui.composables.PlayerSettings
import com.example.bot_lobby.view_models.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
//    playerViewModel: PlayerViewModel,
//    teamViewModel: TeamViewModel,
//    playerTag: String,
//    onExitClick: () -> Unit
) {
    val userLoggedIn = AuthViewModel.userLoggedIn.collectAsState()


    LazyColumn {
        item {
            if (userLoggedIn.value != null) {
                PlayerProfile(userLoggedIn.value!!, isPersonalProfile = true)
            }

        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            PlayerSettings()
        }
    }
}
