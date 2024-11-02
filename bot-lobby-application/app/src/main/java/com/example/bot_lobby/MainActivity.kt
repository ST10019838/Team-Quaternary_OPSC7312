package com.example.bot_lobby

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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

import com.example.bot_lobby.ui.pages.EventsTab
import com.example.bot_lobby.ui.pages.HomeTab
import com.example.bot_lobby.ui.pages.ProfileTab
import com.example.bot_lobby.ui.pages.ScoutingTab
import com.example.bot_lobby.ui.pages.TeamsTab
import com.example.bot_lobby.ui.screens.LoginScreen
import com.example.bot_lobby.ui.theme.BotLobbyTheme

class MainActivity : AppCompatActivity() {
    companion object {
        val userApi: UserApi = RetrofitInstance.UserApi
        // Declare loginService without instantiation here
        lateinit var loginService: LoginService
        lateinit var registerService: RegisterService
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize services here
        loginService = LoginService(userApi)
        registerService = RegisterService(userApi, this) // Pass the activity here

        setContent {
            BotLobbyTheme {
                Navigator(
                    // You can uncomment this logic if needed later
//                    if (auth.currentUser != null)
//                        LandingScreen()
//                    else
                    LoginScreen()
                ) {
                    SlideTransition(it)
                }

                // Uncomment this if you need to use TabNavigator in the future
//                TabNavigator(HomeTab) {
//                    Scaffold(
//                        content = { innerPadding ->
//                            Box(
//                                modifier = Modifier
//                                    .padding(innerPadding)
//                                    .padding(horizontal = 25.dp, vertical = 15.dp)
//                            ) {
//                                CurrentTab()
//                            }
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