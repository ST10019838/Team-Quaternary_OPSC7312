package com.example.bot_lobby

import android.os.Bundle

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.StrictMode
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels

import androidx.appcompat.app.AppCompatActivity

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SignalWifiStatusbarConnectedNoInternet4
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
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

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.example.bot_lobby.models.AccessToken
import com.example.bot_lobby.observers.ConnectivityObserver
import com.example.bot_lobby.observers.NetworkConnectivityObserver
import com.example.bot_lobby.ui.screens.LoginScreen
import com.example.bot_lobby.ui.theme.BotLobbyTheme
import com.example.bot_lobby.view_models.SessionViewModel



import com.example.bot_lobby.ui.pages.EventsTab
import com.example.bot_lobby.ui.pages.HomeTab
import com.example.bot_lobby.ui.pages.ProfileTab
import com.example.bot_lobby.ui.pages.ScoutingTab
import com.example.bot_lobby.ui.pages.TeamsTab
import com.example.bot_lobby.ui.screens.LoginScreen
import com.example.bot_lobby.ui.theme.BotLobbyTheme

// class MainActivity : ComponentActivity(), AppCompatActivity() {
//     companion object {
//         val userApi: UserApi = RetrofitInstance.UserApi
//         // Declare loginService without instantiation here
//
class MainActivity :  /*ComponentActivity(),*/ AppCompatActivity() {
    companion object {
        val userApi: UserApi = RetrofitInstance.UserApi
        val loginService = LoginService(userApi)
        val registerService = RegisterService(userApi)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        loginService = LoginService(userApi)
        registerService = RegisterService(userApi, this)

        // Check and request notification permission
        if (!isNotificationPermissionGranted()) {
            requestNotificationPermission()
        }

//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        setContent {
            BotLobbyTheme {
                Navigator(
//                    if (auth.currentUser != null)
//                        LandingScreen()
//                    else
                    LoginScreen()
                ) {
                    SlideTransition(it)
                }
            }

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

            // FIREBASE MESSAGING TEST
//            Surface(
//                color = MaterialTheme.colorScheme.background,
//                modifier = Modifier.fillMaxSize()
//            ) {
//                val state = viewModel.state
//                if (state.isEnteringToken) {
//                    EnterTokenDialog(
//                        token = state.remoteToken,
//                        onTokenChange = viewModel::onRemoteTokenChange,
//                        onSubmit = viewModel::onSubmitRemoteToken
//                    )
//                } else {
//                    ChatScreen(
//                        messageText = state.messageText,
//                        onMessageSend = {
//                            viewModel.sendMessage(isBroadcast = false)
//                        },
//                        onMessageBroadcast = {
//                            viewModel.sendMessage(isBroadcast = true)
//                        },
//                        onMessageChange = viewModel::onMessageChange
//                    )
//                }
//            }
        }
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
            }
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
