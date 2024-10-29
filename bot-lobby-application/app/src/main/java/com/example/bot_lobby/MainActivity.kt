package com.example.bot_lobby

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.room.Room
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.example.bot_lobby.api.RetrofitInstance
import com.example.bot_lobby.api.UserApi
import com.example.bot_lobby.services.LoginService
import com.example.bot_lobby.services.RegisterService

import com.example.bot_lobby.ui.pages.EventsTab
import com.example.bot_lobby.ui.pages.HomeTab
import com.example.bot_lobby.ui.pages.ProfileTab
import com.example.bot_lobby.ui.pages.ScoutingTab
import com.example.bot_lobby.ui.pages.TeamsTab
import com.example.bot_lobby.ui.screens.LoginScreen
import com.example.bot_lobby.ui.theme.BotLobbyTheme
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bot_lobby.db.LocalDatabase
import com.example.bot_lobby.models.IdAndRole
import com.example.bot_lobby.models.Session
import com.example.bot_lobby.models.Team
import com.example.bot_lobby.models.User
import com.example.bot_lobby.ui.screens.LandingScreen
import com.example.bot_lobby.view_models.SessionViewModel
import com.example.bot_lobby.view_models.UserViewModel
import kotlinx.coroutines.CoroutineScope
import java.util.UUID


class MainActivity : ComponentActivity() {
    companion object {
        val userApi: UserApi = RetrofitInstance.UserApi
        val loginService = LoginService(userApi)
        val registerService = RegisterService(userApi)
    }

    @SuppressLint("StateFlowValueCalledInComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
//            val coroutineScope = rememberCoroutineScope()
//
//            val vm = TestViewModel(applicationContext)
//
////            LaunchedEffect(true) {
////                models =
////            }
//
//           val state by vm.session.collectAsStateWithLifecycle()

//            val sessionViewModel = SessionViewModel(LocalContext.current)


//            val test by sessionViewModel.test.collectAsState()
//            val session by sessionViewModel.session.collectAsStateWithLifecycle()

//            val shouldLogIn = session != null


//            Log.i("Session?", time.toString() )

//            if(session != null)
//                Log.i("SESSION IS NOT NULL",session.toString() )

//            val screenToDisplay = if(session != null)
//                LandingScreen() else LoginScreen()

            BotLobbyTheme {
                Navigator(
//                    if (auth.currentUser != null)
//                        LandingScreen()
//                    else

//                    if(session != null)
//                        LandingScreen()
//                    else
                        LoginScreen()
//                                screenToDisplay
                ) {
                    SlideTransition(it)
                }

//if(sessionViewModel.session.value !== null){
//    Text("LOG ME IN")
//} else Text("DONT LOG ME IN")


//                Box{
//                    LazyColumn {
//                        items(state){ model ->
//                            Text(model.toString())
//                        }
//                    }

//                    Row (modifier = Modifier.padding(bottom = 100.dp, top = 100.dp).align(Alignment.BottomCenter)){
//                        Button(onClick = {
//                            val newTeam = Team(
//                                tag = "D1",
//                                name = "Damian Team 1",
//                                userIdsAndRoles = listOf(IdAndRole(21, "Owner"), IdAndRole(20, "Member")),
//                                isPublic = false,
//                                isOpen = false,
//                                isLFM = false,
//                                maxNumberOfUsers = 10,
//                                bio = "This is our bio"
//                            )
//                            vm.upsertTeam(newTeam)
//
//                        }) { Text("Add") }
//
//                        Button(
//                            onClick = {
//                                val teamToUpdate = Team(
//                                    id = state.first().id,
//                                    tag = "UR MOM",
//                                    name = "Damian Team 1",
//                                    userIdsAndRoles = listOf(IdAndRole(21, "Owner"), IdAndRole(20, "Member")),
//                                    isPublic = false,
//                                    isOpen = true,
//                                    isLFM = false,
//                                    maxNumberOfUsers = 10,
//                                    bio = "This is our bio"
//                                )
//
//                                vm.upsertTeam(teamToUpdate)
//                            },
//                        ) { Text("Update") }


//                        Button(
//                            onClick = {
//                                vm.deleteTeams()
//                            },
//                        ) { Text("Delete") }

//                        Button(onClick = {
//                            val newUser = User(
//                                id = 1,
//                                role = 1,
//                                bio = "this is the bio",
//                                username = "Dare",
//                                password = "password",
//                                teamIds = listOf(UUID.randomUUID(), UUID.randomUUID()),
//                                isPublic = false,
//                                isLFT = false,
//                                email = "myemail"
//                            )
//                            vm.upsertUser(newUser)
//
//                        }) { Text("Add User") }

//                        Button(onClick = {
//                            val newUser = User(
//                                id = 1,
//                                role = 1,
//                                bio = "this is not the bio",
//                                username = "Dare",
//                                password = "password",
//                                teamIds = listOf(UUID.randomUUID(), UUID.randomUUID()),
//                                isPublic = false,
//                                isLFT = false,
//                                email = "myemail"
//                            )
//                            vm.upsertSession(Session(userLoggedIn = newUser))
//
//                        }) { Text("Add Session") }
//
//                    }
//
//                    if(state.isNotEmpty()){
//                        Text(state.first().userLoggedIn.bio.toString())
//                    }



                }


//                TabNavigator(HomeTab) {
//                    Scaffold(
//                        content = { innerPadding ->
////                            AuthScreen(
////                                modifier = Modifier.padding(innerPadding),
////                                loginService = loginService,
////                                registerService = registerService
////                            )
//
//                            Box(
//                                modifier = Modifier
//                                    .padding(innerPadding)
//                                    .padding(horizontal = 25.dp, vertical = 15.dp)
//                            ) {
//                                CurrentTab()
//                            }
//
//                        },
//                        bottomBar = {
//                            NavigationBar {
//                                TabNavigationItem(EventsTab)
//                                TabNavigationItem(TeamsTab)
//                                TabNavigationItem(HomeTab)
//                                TabNavigationItem(ProfileTab)
//                                TabNavigationItem(ScoutingTab)
//                            }
//                        }
//                    )
//                }
//            }
        }
    }
}


@Composable
private fun RowScope.TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current
    val isSelected = tabNavigator.current == tab

    NavigationBarItem(
        selected = isSelected,
        onClick = { tabNavigator.current = tab },
        icon = {
            Icon(
                tab.options.icon!!,
                contentDescription = tab.options.title,
                modifier = Modifier.alpha(if (!isSelected) 0.5f else 1f)
            )
        },
        label = {
            Text(
                text = tab.options.title,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.alpha(if (!isSelected) 0.5f else 1f),
                style = if (isSelected) MaterialTheme.typography.bodyMedium
                else MaterialTheme.typography.bodySmall
            )
        },
        colors = NavigationBarItemDefaults.colors(
            indicatorColor = MaterialTheme.colorScheme.primary,
            selectedTextColor = MaterialTheme.colorScheme.primary
        )
    )
}
