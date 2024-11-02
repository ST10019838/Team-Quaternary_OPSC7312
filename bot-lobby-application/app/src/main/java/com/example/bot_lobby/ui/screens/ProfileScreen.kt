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
//    val userViewModel = viewModel<UserViewModel>()
//    val teamViewModel = viewModel<TeamViewModel>()


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
                    sessionViewModel.signOut {
                        runBlocking {
                            delay(300L)

                            navigator.popUntilRoot()  // Navigate back to the root screen
                        }
                    }



                    Toast.makeText(context, R.string.account_sign_out_success, Toast.LENGTH_SHORT)
                        .show()  // Show a confirmation toast

//                    navigator.popUntilRoot()  // Navigate back to the root screen again as the data hasn't updated

                },
                onDelete = {
                    // Navigate back to the root screen
                    UserViewModel.deleteUser(sessionViewModel.session.value?.userLoggedIn?.id!!)

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
                        val response = UserViewModel.getOnlineProfile(session?.userLoggedIn!!)

                        val remoteProfile = response.data
                        val localProfile = session?.userLoggedIn

                        Log.i("Rem profile: ", remoteProfile.toString())
                        Log.i("loc profile: ", localProfile.toString())


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
                            val isOwner = teamToAdd?.userIdsAndRoles
                                ?.find { (it.id == session!!.userLoggedIn.id) }
                                ?.isOwner

                            // the user needs to be the owner of the team to add it
                            if (teamToAdd != null && isOwner == true) {
                                TeamViewModel.createTeam(teamToAdd)
                            }
                        }

                        teamsToUpdate?.forEach { teamId ->
                            val team = session?.usersTeams?.find { team -> team.id == teamId }

                            val isOwner = team?.userIdsAndRoles
                                ?.find { (it.id == session!!.userLoggedIn.id) }
                                ?.isOwner

                            if (team != null && isOwner == true) {
                                // The online data is fetched so that we can don't override the teams
                                // userIdsAndRoles so that we don't accidentally remove a user that might
                                // have joined team when the owner was offline.
                                TeamViewModel.getTeam(teamId) { remoteTeam ->

                                    val updatedTeam = Team(
                                        id = team.id,
                                        tag = team.tag,
                                        name = team.name,
                                        isPublic = team.isPublic,
                                        isOpen = team.isOpen,
                                        isLFM = team.isLFM,
                                        maxNumberOfUsers = team.maxNumberOfUsers,
                                        bio = team.bio,
                                        userIdsAndRoles = remoteTeam.userIdsAndRoles,
                                    )

                                    TeamViewModel.updateTeam(updatedTeam)
                                }
                            }
                        }


                        teamsToDelete?.forEach { teamId ->
                            // wont work as the team is already deleted
                            TeamViewModel.getTeam(teamId) { teamToDelete ->
                                val isOwner = teamToDelete.userIdsAndRoles
                                    ?.find { (it.id == session!!.userLoggedIn.id) }
                                    ?.isOwner

                                // the user needs to be the owner of the team to delete it
                                if (isOwner == true) {
                                    TeamViewModel.deleteTeam(teamId)
                                } else if (isOwner == false) {
                                    // If the user is not a member of the team and it is no longer there,
                                    // the user has left the team
                                    TeamViewModel.getTeam(teamToDelete.id) { teamToUpdate ->
                                        val updatedIdsAndRoles =
                                            teamToUpdate.userIdsAndRoles?.filter {
                                                it.id != session?.userLoggedIn?.id
                                            }

                                        val updatedTeam = Team(
                                            id = teamToUpdate.id,
                                            tag = teamToUpdate.tag,
                                            name = teamToUpdate.name,
                                            bio = teamToUpdate.bio,
                                            isPublic = teamToUpdate.isPublic,
                                            isLFM = teamToUpdate.isLFM,
                                            isOpen = teamToUpdate.isOpen,
                                            userIdsAndRoles = updatedIdsAndRoles,
                                            maxNumberOfUsers = teamToUpdate.maxNumberOfUsers
                                        )

                                        TeamViewModel.updateTeam(updatedTeam)

//                                    sessionViewModel.removeTeamFromUser(team = teamToUpdate) {
//                                        if (it != null) {
//                                            userViewModel.updateUser(it)
//                                        }
//                                    }
                                    }

                                }
//                                teamViewModel.leaveTeam(teamId)
                            }
                        }


                        // 3. Update Profile
                        // - the sync requires the ids stored in player profile to determine what action to perform for a team,
                        // hence the profile will be updated last and not before

                        UserViewModel.updateUser(session?.userLoggedIn!!)
                    }



                    Toast.makeText(context, R.string.successful_data_sync, Toast.LENGTH_SHORT)
                        .show()  // Show a confirmation toast
                }
            )
        }
    }
}
