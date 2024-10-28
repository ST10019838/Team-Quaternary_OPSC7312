package com.example.bot_lobby.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.bot_lobby.models.User
import com.example.bot_lobby.services.LoginService
import com.example.bot_lobby.services.RegisterService
import com.example.bot_lobby.utils.BiometricAuthHelper
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    loginService: LoginService,
    registerService: RegisterService
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var enableBiometrics by remember { mutableStateOf(false) } // Toggle state
    var result by remember { mutableStateOf("") }
    var isLoginMode by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        if (!isLoginMode) { // Only show for Registration
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Enable Biometric Registration")
                Switch(
                    checked = enableBiometrics,
                    onCheckedChange = { enableBiometrics = it }
                )
            }
        }

        Button(onClick = {
            coroutineScope.launch {
                if (!isLoginMode) {
                    val newUser = User(username = username, password = password)
                    if (enableBiometrics) {
                        registerBiometricData(newUser)
                    }
                    val response = registerService.register(newUser)
                    result = if (response.isSuccessful) "Registration successful!" else "Registration failed!"
                }
            }
        }) {
            Text(text = if (isLoginMode) "Login" else "Register")
        }
    }
}
fun registerBiometricData(user: User): User {
    // Example: Assigning a mock biometric data for demonstration purposes
    user.biometrics = "sample_biometric_data"  // You would replace this with actual biometric data logic
    Log.d("BiometricAuthHelper", "Biometric data registered for user: ${user.username}")

    return user
}

