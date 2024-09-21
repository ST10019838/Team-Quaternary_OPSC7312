package com.example.bot_lobby.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileScreen() {
    // Get the current FirebaseAuth instance and navigator
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current
    val navigator = LocalNavigator.currentOrThrow

    // Main layout for the ProfileScreen
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Profile screen content
        Text(
            text = "Welcome to your Profile!",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Logoff Button
        Button(
            onClick = {
                // Call the Firebase signOut() method to log off
                auth.signOut()

                // Show a toast message to notify the user
                Toast.makeText(context, "Successfully logged off", Toast.LENGTH_SHORT).show()

                // Navigate back to the root screen (LoginScreen) after signing out
                navigator.popUntilRoot() // This will reset the navigation stack
                navigator.push(LoginScreen())
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            Text("Log Off")
        }

    }
}
