package com.example.bot_lobby.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.bot_lobby.MainActivity.Companion.loginService
import com.example.bot_lobby.R
import com.example.bot_lobby.api.RetrofitInstance
import com.example.bot_lobby.forms.SignUpForm
import com.example.bot_lobby.models.User
import com.example.bot_lobby.ui.theme.BlueStandard
import com.example.bot_lobby.utils.onFormValueChange
import com.example.bot_lobby.view_models.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class Mode {
    SignUp, ForgotPassword
}

data class AccountScreen(
    val mode: Mode
) : Screen {
    override val key = uniqueScreenKey

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val context = LocalContext.current as AppCompatActivity
        val navigator = LocalNavigator.currentOrThrow
        val form = SignUpForm()
//        val userViewModel = UserViewModel()
//        val auth = remember { FirebaseAuth.getInstance() }

        var isBiometricEnabled by remember { mutableStateOf(false) }
        var actionWasSuccessful by remember { mutableStateOf(true) }
        val coroutineScope = rememberCoroutineScope()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            if (mode == Mode.SignUp) "Register" else "Reset Password",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.headlineSmall
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Arrow Back"
                            )
                        }
                    }
                )
            },
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 25.dp)
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_bot_lobby_logo),
                        contentDescription = "App Logo",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                    )

                    Text(
                        text = "Bot Lobby",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )

                    OutlinedTextField(
                        value = form.username.state.value ?: "",
                        onValueChange = { value -> onFormValueChange(value, form, form.username) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("User Name") },
                        isError = form.username.hasError(),
                        leadingIcon = {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "Username Icon"
                            )
                        },
                        singleLine = true
                    )

                    var passwordVisible by remember { mutableStateOf(false) }
                    OutlinedTextField(
                        value = form.password.state.value ?: "",
                        onValueChange = { value -> onFormValueChange(value, form, form.password) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text(if (mode == Mode.SignUp) "Password" else "New Password") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Lock,
                                contentDescription = "Password Icon"
                            )
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image =
                                if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    image,
                                    contentDescription = if (passwordVisible) "Hide password" else "Show password"
                                )
                            }
                        },
                        singleLine = true,
                        isError = form.password.hasError()
                    )

                    OutlinedTextField(
                        value = form.passwordConfirmation.state.value ?: "",
                        onValueChange = { value ->
                            onFormValueChange(
                                value,
                                form,
                                form.passwordConfirmation
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Confirm Password") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Lock,
                                contentDescription = "Confirm Password Icon"
                            )
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image =
                                if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    image,
                                    contentDescription = if (passwordVisible) "Hide password" else "Show password"
                                )
                            }
                        },
                        singleLine = true,
                        isError = form.passwordConfirmation.hasError()
                    )

                    Spacer(Modifier.height(20.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Register with Biometrics")
                        Switch(
                            checked = isBiometricEnabled,
                            onCheckedChange = { isBiometricEnabled = it }
                        )
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (!actionWasSuccessful) {
                            Text(
                                "Something went wrong, please try again",
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.Center
                            )
                        }
                        Button(
                            onClick = {
                                form.validate(true)

                                if (form.isValid) {
                                    val username = form.username.state.value!!
                                    val password = form.password.state.value!!


                                    if (username.isNullOrEmpty() || password.isNullOrEmpty()) {
                                        Toast.makeText(
                                            context,
                                            "Username or password cannot be empty",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                    val newUser = User(
                                        username = username,
                                        password = password,
                                        isBiometricEnabled = isBiometricEnabled
                                    )

                                    UserViewModel.createUser(
                                        User(
                                            username = form.username.state.value!!,
                                            password = form.password.state.value!!
                                        )
                                    ) {
                                        UserViewModel.loginUser(
                                            username = form.username.state.value!!,
                                            password = form.password.state.value!!,
                                            context
                                        ) { user ->
                                            if (user == null) {
                                                Toast.makeText(
                                                    context,
                                                    "Username or Password doesn't exist",
                                                    Toast.LENGTH_SHORT
                                                )
                                                    .show() // Toast message to indicate the process
                                            } else {
                                                navigator.push(LandingScreen())
                                            }
                                        }
                                    }

                                    // User registration with callback
                                    UserViewModel.createUser(newUser) { createdUser ->
                                        if (createdUser != null) {
                                            // Successfully created user, now log in
                                            coroutineScope.launch {
                                                try {
                                                    // Log the user in after registration
                                                    val loginResult = withContext(Dispatchers.IO) {
                                                        val usernameNonNull = createdUser.username
                                                        val passwordNonNull = createdUser.password
                                                            ?: throw IllegalArgumentException("Password cannot be null")

                                                        loginService.login(
                                                            usernameNonNull,
                                                            passwordNonNull
                                                        )
                                                    }

                                                    if (loginResult.isSuccessful) {
                                                        withContext(Dispatchers.Main) {
                                                            Toast.makeText(
                                                                context,
                                                                "Login successful",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                            navigator.push(LandingScreen())
                                                        }
                                                    } else {
                                                        withContext(Dispatchers.Main) {
                                                            Toast.makeText(
                                                                context,
                                                                "Login failed",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        }
                                                    }
                                                } catch (loginException: Exception) {
                                                    withContext(Dispatchers.Main) {
                                                        Toast.makeText(
                                                            context,
                                                            "An error occurred during login: ${loginException.message}",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                }
                                            }
                                        } else {
                                            // Handle registration failure
                                            Toast.makeText(
                                                context,
                                                "User registration failed",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }

                                    }
                                } else {
                                    // Handle forgot password
                                    Toast.makeText(
                                        context,
                                        "Password reset functionality is not yet implemented.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = BlueStandard)
                        ) {
                            Text(if (mode == Mode.SignUp) "Register" else "Reset Password")
                        }
                        if (mode == Mode.SignUp) {
                            TextButton(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = { navigator.pop() }
                            ) {
                                Text(
                                    buildAnnotatedString {
                                        append("Already have an account? ")
                                        withStyle(style = SpanStyle(color = BlueStandard)) {
                                            append("Log in")
                                        }
                                    },
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}