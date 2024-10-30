package com.example.bot_lobby.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.bot_lobby.MainActivity.Companion.connectivityObserver
import com.example.bot_lobby.observers.ConnectivityObserver
import com.example.bot_lobby.ui.composables.PlayerProfile
import com.example.bot_lobby.ui.composables.PlayerSettings
import com.example.bot_lobby.view_models.AuthViewModel
import com.example.bot_lobby.view_models.SessionViewModel
import com.example.bot_lobby.view_models.UserViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
//    playerViewModel: PlayerViewModel,
//    teamViewModel: TeamViewModel,
//    playerTag: String,
//    onExitClick: () -> Unit
) {
    val context = LocalContext.current
    val sessionViewModel = viewModel { SessionViewModel(context) }
    val userViewModel = viewModel<UserViewModel>()

    val session by sessionViewModel.session.collectAsState()
    val navigator = LocalNavigator.currentOrThrow

    val connectivity by connectivityObserver.observe().collectAsState(ConnectivityObserver.Status.Unavailable)
    val isOffline = connectivity != ConnectivityObserver.Status.Available
    val coroutineScope = rememberCoroutineScope()

    LazyColumn {
        item {
            if (session?.userLoggedIn != null) {
                PlayerProfile(session?.userLoggedIn!!, isPersonalProfile = true)
            }

        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            PlayerSettings(isOffline = isOffline,
                onSignOut = {
                    sessionViewModel.signOut()

                    navigator.popUntilRoot()  // Navigate back to the root screen

                    Toast.makeText(context, "Successfully Signed Out", Toast.LENGTH_SHORT)
                        .show()  // Show a confirmation toast
                },
                onDelete = {
                      // Navigate back to the root screen
                    userViewModel.deleteUser(sessionViewModel.session.value?.userLoggedIn?.id!!)

                    sessionViewModel.signOut {
                        navigator.popUntilRoot()
                    }

                    Toast.makeText(context, "Successfully Deleted Account", Toast.LENGTH_SHORT)
                        .show()  // Show a confirmation toast

                },
                onSync = {
                    Toast.makeText(context, "Syncing to Online Database...", Toast.LENGTH_SHORT)
                        .show()  // Show a confirmation toast

                    coroutineScope.launch {
                        val response = userViewModel.getOnlineProfile(session?.userLoggedIn!!)


                        Log.i("My response is: ", response.data.toString())
                    }



                    Toast.makeText(context, "Successfully Synced Data", Toast.LENGTH_SHORT)
                        .show()  // Show a confirmation toast
                }
                )
        }
    }
}
