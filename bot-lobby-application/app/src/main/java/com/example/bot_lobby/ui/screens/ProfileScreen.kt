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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.bot_lobby.MainActivity.Companion.connectivityObserver
import com.example.bot_lobby.R
import com.example.bot_lobby.models.Team
import com.example.bot_lobby.observers.ConnectivityObserver
import com.example.bot_lobby.ui.composables.PlayerProfile
import com.example.bot_lobby.ui.composables.PlayerSettings
import com.example.bot_lobby.view_models.AuthViewModel
import com.example.bot_lobby.view_models.SessionViewModel
import com.example.bot_lobby.view_models.TeamViewModel
import com.example.bot_lobby.view_models.UserViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

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
    val teamViewModel = viewModel<TeamViewModel>()


    val session by sessionViewModel.session.collectAsState()
    val navigator = LocalNavigator.currentOrThrow

    val connectivity by connectivityObserver.observe()
        .collectAsState(ConnectivityObserver.Status.Unavailable)
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

                    runBlocking {
                        delay(300L)
                    }

                    navigator.popUntilRoot()  // Navigate back to the root screen

                    Toast.makeText(context, R.string.account_sign_out_success, Toast.LENGTH_SHORT)
                        .show()  // Show a confirmation toast

//                    navigator.popUntilRoot()  // Navigate back to the root screen again as the data hasn't updated

                },
                onDelete = {
                    // Navigate back to the root screen
                    userViewModel.deleteUser(sessionViewModel.session.value?.userLoggedIn?.id!!)

                    sessionViewModel.signOut {
                        navigator.popUntilRoot()
                    }

                    Toast.makeText(context, R.string.account_deleted_success, Toast.LENGTH_SHORT)
                        .show()  // Show a confirmation toast

                },
                onSync = {
                    Toast.makeText(context, R.string.starting_data_sync, Toast.LENGTH_SHORT)
                        .show()  // Show a confirmation toast

                    coroutineScope.launch {
                        val response = userViewModel.getOnlineProfile(session?.userLoggedIn!!)

                        val remoteProfile = response.data
                        val localProfile = session?.userLoggedIn


                        // 1. Determine which teams will need to be updated, deleted or added
                        // - teams that are in the local db but not the remote db need to be added to the remote db
                        // - teams that are in the remote db but not the local db need to be deleted from the remote db
                        // - teams that are in both the local and remote dbs need to be updated
                        val teamsToAdd = localProfile?.teamIds?.subtract(
                            (remoteProfile?.teamIds ?: emptyList()).toSet()
                        )
                        val teamsToUpdate = remoteProfile?.teamIds?.intersect(
                            (localProfile?.teamIds ?: emptyList()).toSet()
                        )
                        val teamsToDelete = remoteProfile?.teamIds?.subtract(
                            (localProfile?.teamIds ?: emptyList()).toSet()
                        )


                        // 2. Perform team actions
                        teamsToAdd?.forEach { teamId ->
                            val teamToAdd = session?.usersTeams?.find { team -> team.id == teamId }

                            if (teamToAdd != null) {
                                teamViewModel.createTeam(teamToAdd) {

                                }
                            }
                        }

                        teamsToUpdate?.forEach { teamId ->
                            val team = session?.usersTeams?.find { team -> team.id == teamId }

                            // TODO: need to exclude the userIdsAndRoles to ensure that everything else is updated,
                            //  so that we don't override any users that may have joined the team
//                            val updatedTeam = Team(
//                                id = team.id,
//                                tag = team.tag,
//                                name = team.name,
//                                isPublic = team.isPublic,
//                                isOpen = team.isOpen,
//                                isLFM = team.isLFM,
//                                maxNumberOfUsers = team.maxNumberOfUsers,
//                                bio = team.bio,
//                                userIdsAndRoles = team.userIdsAndRoles,
//                            )

                            if (team != null) {
                                teamViewModel.updateTeam(team)
                            }
                        }

                        teamsToDelete?.forEach { teamId ->
                            teamViewModel.deleteTeam(teamId)
                        }


                        // 3. Update Profile
                        // - the sync requires the ids stored in player profile to determine what action to perform for a team,
                        // hence the profile will be updated last and not before

                        userViewModel.updateUser(session?.userLoggedIn!!)

                        Log.i("My response is: ", response.data.toString())
                    }



                    Toast.makeText(context, R.string.successful_data_sync, Toast.LENGTH_SHORT)
                        .show()  // Show a confirmation toast
                }
            )
        }
    }
}
