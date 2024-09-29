package com.example.bot_lobby.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.bot_lobby.LoginService
import com.example.bot_lobby.RegisterService
import com.example.bot_lobby.models.User
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    loginService: LoginService,
    registerService: RegisterService
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var isLoginMode by remember { mutableStateOf(true) }
    var result by remember { mutableStateOf("") }
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

        // Fields for Registration
        if (!isLoginMode) {
            TextField(
                value = bio,
                onValueChange = { bio = it },
                label = { Text("Bio") }
            )

            // Other fields for registration can be added here if needed
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
                        bio = bio,
                        role = 1, // Default role, update as necessary
                        teamIds = listOf() // Assuming no teams at registration, adjust as needed
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
