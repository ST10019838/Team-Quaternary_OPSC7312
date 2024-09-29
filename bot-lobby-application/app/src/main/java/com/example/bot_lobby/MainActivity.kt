package com.example.bot_lobby

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.Navigator
import com.example.bot_lobby.ui.screens.*
import com.example.bot_lobby.ui.theme.BotLobbyTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BotLobbyTheme {
                val auth = FirebaseAuth.getInstance()

                // This was taken form the following website to use the mutualstateof function
                // https://medium.com/@ah.shubita/jetpack-compose-remember-mutablestateof-derivedstateof-and-remembersaveable-explained-b6ede7fed673
                // Ahmad Shubita
                // https://medium.com/@ah.shubita
                //
                // Manage SSO
                val isLoggedIn by remember { mutableStateOf(auth.currentUser != null) }

                // Pass either the LoginScreen or HomeScreen to the Navigator
                Navigator(screen = if (isLoggedIn) LandingScreen() else LoginScreen())
            }
        }
    }
}
