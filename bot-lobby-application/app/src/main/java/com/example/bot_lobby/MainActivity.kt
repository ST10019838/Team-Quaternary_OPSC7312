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
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.example.bot_lobby.models.User
import com.example.bot_lobby.ui.pages.*
import com.example.bot_lobby.ui.theme.BotLobbyTheme
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json() // Use Kotlinx serialization for JSON
        }
    }

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
                            AuthScreen(
                                modifier = Modifier.padding(innerPadding),
                                loginService = loginService,
                                registerService = registerService
                            )
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
fun AuthScreen(
    modifier: Modifier = Modifier,
    loginService: LoginService,
    registerService: RegisterService
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var firstname by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var isLoginMode by remember { mutableStateOf(true) }
    var isLoading by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Text(
            text = if (isLoginMode) "Login" else "Register",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        if (!isLoginMode) {
            TextField(
                value = firstname,
                onValueChange = { firstname = it },
                label = { Text("First Name") }
            )

            TextField(
                value = lastname,
                onValueChange = { lastname = it },
                label = { Text("Last Name") }
            )

            TextField(
                value = age,
                onValueChange = { age = it },
                label = { Text("Age") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
        }

        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
        )

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )

        Button(onClick = {
            coroutineScope.launch {
                isLoading = true
                result = if (isLoginMode) {
                    loginService.login(username, password)
                } else {
                    // Create a User instance and pass it to register
                    val user = User(
                        username = username,
                        password = password,
                        firstname = firstname,
                        lastname = lastname,
                        age = age.toIntOrNull() ?: 0 // Use a default value if parsing fails
                    )
                    registerService.register(user)
                }
                isLoading = false
            }
        }) {
            Text(text = if (isLoginMode) "Login" else "Register")
        }

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = result)

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { isLoginMode = !isLoginMode }) {
            Text(text = if (isLoginMode) "Don't have an account? Register" else "Already have an account? Login")
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