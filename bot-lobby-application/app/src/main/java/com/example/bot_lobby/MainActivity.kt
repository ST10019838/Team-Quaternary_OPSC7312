package com.example.bot_lobby

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.example.bot_lobby.api.RetrofitInstance
import com.example.bot_lobby.api.UserApi
import com.example.bot_lobby.services.LoginService
import com.example.bot_lobby.services.RegisterService

import com.example.bot_lobby.ui.screens.LoginScreen
import com.example.bot_lobby.ui.theme.BotLobbyTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SignalWifiStatusbarConnectedNoInternet4
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.bot_lobby.observers.ConnectivityObserver
import com.example.bot_lobby.observers.NetworkConnectivityObserver


class MainActivity : ComponentActivity() {
    companion object {
        val userApi: UserApi = RetrofitInstance.UserApi
        val loginService = LoginService(userApi)
        val registerService = RegisterService(userApi)
        lateinit var connectivityObserver: ConnectivityObserver
    }

    @SuppressLint("StateFlowValueCalledInComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        connectivityObserver = NetworkConnectivityObserver(applicationContext)
        enableEdgeToEdge()
        setContent {
            val connectivity by connectivityObserver.observe()
                .collectAsState(ConnectivityObserver.Status.Unavailable)
            var dismissedOfflineDialog by remember { mutableStateOf(false) }

//            val showOfflineDialog by remember {mutableStateOf(connectivity != ConnectivityObserver.Status.Available && !dismissedOfllineDialog)}

            val showOfflineDialog =
                connectivity != ConnectivityObserver.Status.Available && !dismissedOfflineDialog

            if (connectivity == ConnectivityObserver.Status.Available) {
                dismissedOfflineDialog = false
            }

            var showStudentNotice by remember { mutableStateOf(true) }


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

//            val status by connectivityObserver.observe().collectAsState(
//                initial = ConnectivityObserver.Status.Unavailable
//            )
//            Box(
//                modifier = Modifier.fillMaxSize(),
//                contentAlignment = Alignment.Center
//            ) {
//                Text(text = "Network status: $status")
//            }

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

            if (showOfflineDialog) {
                AlertDialog(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.SignalWifiStatusbarConnectedNoInternet4,
                            contentDescription = "Offline Mode"
                        )
                    },
                    title = {
                        Text(
                            text = stringResource(R.string.offline_notice_title),
                            textAlign = TextAlign.Center
                        )
                    },
                    text = {
                        Text(stringResource(R.string.offline_notice_body))
                    },
                    onDismissRequest = {
                        dismissedOfflineDialog = true
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                dismissedOfflineDialog = true
                            }
                        ) {
                            Text(stringResource(R.string.confirm_action))
                        }
                    },
                )
            }

            if (showStudentNotice) {
                AlertDialog(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Student Alert"
                        )
                    },
                    title = {
                        Text(
                            text = stringResource(R.string.student_notice_title),
                            textAlign = TextAlign.Center
                        )
                    },
                    text = {
                        Text(
                            stringResource(R.string.student_notice_body),
                            textAlign = TextAlign.Justify
                        )
                    },
                    onDismissRequest = {
                        showStudentNotice = false
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showStudentNotice = false
                            }
                        ) {
                            Text(stringResource(R.string.confirm_action))
                        }
                    },
                )
            }
        }
    }
}


//@Composable
//private fun RowScope.TabNavigationItem(tab: Tab) {
//    val tabNavigator = LocalTabNavigator.current
//    val isSelected = tabNavigator.current == tab
//
//    NavigationBarItem(
//        selected = isSelected,
//        onClick = { tabNavigator.current = tab },
//        icon = {
//            Icon(
//                tab.options.icon!!,
//                contentDescription = tab.options.title,
//                modifier = Modifier.alpha(if (!isSelected) 0.5f else 1f)
//            )
//        },
//        label = {
//            Text(
//                text = tab.options.title,
//                textAlign = TextAlign.Center,
//                maxLines = 1,
//                overflow = TextOverflow.Ellipsis,
//                modifier = Modifier.alpha(if (!isSelected) 0.5f else 1f),
//                style = if (isSelected) MaterialTheme.typography.bodyMedium
//                else MaterialTheme.typography.bodySmall
//            )
//        },
//        colors = NavigationBarItemDefaults.colors(
//            indicatorColor = MaterialTheme.colorScheme.primary,
//            selectedTextColor = MaterialTheme.colorScheme.primary
//        )
//    )
//}
