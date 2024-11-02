package com.example.bot_lobby.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.bot_lobby.R
import com.example.bot_lobby.services.LoginService
import com.example.bot_lobby.services.RegisterService
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    loginService: LoginService,
    registerService: RegisterService
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var isLoginMode by remember { mutableStateOf(true) }

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Text(
            text = if (isLoginMode) stringResource(id = R.string.login_title) else stringResource(id = R.string.register),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text(stringResource(id = R.string.username_placeholder_login)) }
        )

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(id = R.string.password_placeholder_login)) },
            visualTransformation = PasswordVisualTransformation()
        )

        if (!isLoginMode) {
            TextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text(stringResource(id = R.string.first_name_placeholder)) }
            )

            TextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text(stringResource(id = R.string.last_name_placeholder)) }
            )

            TextField(
                value = age,
                onValueChange = { age = it },
                label = { Text(stringResource(id = R.string.age_placeholder)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        Button(
            onClick = {
                coroutineScope.launch {
                    result = if (isLoginMode) {
                        loginService.login(username, password)
                    } else {
                        val ageInt = age.toIntOrNull() ?: 0
                        registerService.register(username, password, firstName, lastName, ageInt)
                    }
                }
            }
        ) {
            Text(text = if (isLoginMode) stringResource(id = R.string.login_title) else stringResource(id = R.string.register))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = result)

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { isLoginMode = !isLoginMode }) {
            Text(text = if (isLoginMode) stringResource(id = R.string.no_account_register) else stringResource(id = R.string.already_have_account_login))
        }
    }
}
