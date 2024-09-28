package com.example.bot_lobby

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.example.bot_lobby.ui.pages.EventsTab
import com.example.bot_lobby.ui.pages.HomeTab
import com.example.bot_lobby.ui.pages.ProfileTab
import com.example.bot_lobby.ui.pages.ScoutingTab
import com.example.bot_lobby.ui.pages.TeamsTab
import com.example.bot_lobby.ui.theme.BotLobbyTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val client = ApiClient.client
    private val loginService = LoginService(client)
    private val registerService = RegisterService(client)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BotLobbyTheme {
                TabNavigator(HomeTab) {
                    Scaffold(
                        content = { innerPadding ->
//                            AuthScreen(
//                                modifier = Modifier.padding(innerPadding),
//                                loginService = loginService,
//                                registerService = registerService
//                            )

                            Box(
                                modifier = Modifier
                                    .padding(innerPadding)
                                    .padding(horizontal = 25.dp, vertical = 15.dp)
                            ) {
                                CurrentTab()
                            }

                        },
                        bottomBar = {
                            NavigationBar {
                                TabNavigationItem(EventsTab)
                                TabNavigationItem(TeamsTab)
                                TabNavigationItem(HomeTab)
                                TabNavigationItem(ProfileTab)
                                TabNavigationItem(ScoutingTab)
                            }
                        }
                    )
                }
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
