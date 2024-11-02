package com.example.bot_lobby.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.bot_lobby.services.GoogleSignInButton
import com.example.bot_lobby.MainActivity
import com.example.bot_lobby.MainActivity.Companion.connectivityObserver
import com.example.bot_lobby.R
import com.example.bot_lobby.forms.LoginForm
import com.example.bot_lobby.observers.ConnectivityObserver
import com.example.bot_lobby.ui.theme.BlueStandard
import com.example.bot_lobby.utils.onFormValueChange
import com.example.bot_lobby.view_models.AuthViewModel
import com.example.bot_lobby.view_models.SessionViewModel
import com.example.bot_lobby.view_models.UserViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LoginScreen : Screen {
    override val key = uniqueScreenKey

    @Composable
    override fun Content() {
        val context = LocalContext.current
        val sessionViewModel = viewModel { SessionViewModel(context) }
        val session by sessionViewModel.session.collectAsStateWithLifecycle()
        val navigator = LocalNavigator.currentOrThrow

        val connectivity by connectivityObserver.observe()
            .collectAsState(ConnectivityObserver.Status.Unavailable)
        val isOffline = connectivity != ConnectivityObserver.Status.Available


        var isDeterminingLogin by remember { mutableStateOf(true) }

        LaunchedEffect(true) {
            delay(300L)
            isDeterminingLogin = false
        }

        if (session != null && !isDeterminingLogin) {
            navigator.push(LandingScreen())
        }

//        val userViewModel = UserViewModel()

        val form = LoginForm() // Form that manages the login state

        // Initialize areCredentialsValid state
        val areCredentialsValid by remember { mutableStateOf(true) }

        Scaffold(content = {
            // Main Column container for the login screen
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(horizontal = 25.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally, // Align all children to the center horizontally
                verticalArrangement = Arrangement.SpaceBetween // Space the content vertically
            ) {
                // Top part with the logo and app name
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp), // Spacing between items
                    horizontalAlignment = Alignment.CenterHorizontally // Center the content horizontally
                ) {
                    // First empty row (Spacer)
                    Spacer(modifier = Modifier.height(16.dp)) // Adjust the height as needed

                    // Second empty row (Spacer)
                    Spacer(modifier = Modifier.height(16.dp)) // Adjust the height as needed

                    // Title: "Sign in Your Account"
                    Text(
                        text = "Sign in Your Account",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground, // Color of the text based on the current theme
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // App Logo Image
                    Image(
                        painter = painterResource(id = R.drawable.ic_bot_lobby_logo),
                        contentDescription = "App Logo", // Description for accessibility
                        modifier = Modifier
                            .fillMaxWidth(1.0f) // Fill up the width
                            .aspectRatio(16f / 9f) // Maintain the aspect ratio
                    )

                    // App Name Text: "BotLobby"
                    Text(
                        text = "Bot Lobby",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onBackground, // Color of the text
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                // Spacer to center email and password fields between "BotLobby" and the buttons
                Spacer(modifier = Modifier.weight(1f))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth(),

                    ) {
                    GoogleSignInButton(
                        registerService = MainActivity.registerService,
                        loginService = MainActivity.loginService,
                        isReg = false, // Registration,
                        navigator = navigator,
                        enabled = !isOffline
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    HorizontalDivider(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100))
                            .fillMaxWidth()
                            .weight(1f),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "or",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    HorizontalDivider(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100))
                            .fillMaxWidth()
                            .weight(1f),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Email and Password fields
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Email Input Field with Email icon
                    OutlinedTextField(
                        value = form.username.state.value
                            ?: "", // Retrieve the current value of email from the form
                        onValueChange = { value -> // Handle text change and update the form
                            onFormValueChange(
                                value = value,
                                form = form,
                                fieldState = form.username
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Username") }, // Placeholder text inside the input
                        isError = form.username.hasError(), // Show error if there is one in the form
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person, // Display email icon inside the input field
                                contentDescription = "Username Icon"
                            )
                        },
                        singleLine = true, // Ensure the input stays a single line
                        visualTransformation = VisualTransformation.None, // No transformation (plain text)
                        trailingIcon = {
                            // Show error icon if there's an error
                            if (form.username.hasError()) {
                                Icon(
                                    imageVector = Icons.Default.Error,
                                    contentDescription = "Error Icon",
                                    tint = MaterialTheme.colorScheme.error // Error color based on theme
                                )
                            }
                        }
                    )

                    // Password Input Field with visibility toggle and lock icon
                    var passwordVisible by remember { mutableStateOf(false) } // Manage password visibility
                    OutlinedTextField(
                        value = form.password.state.value
                            ?: "", // Retrieve the current value of password from the form
                        onValueChange = { value -> // Handle password text change
                            onFormValueChange(
                                value = value,
                                form = form,
                                fieldState = form.password
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Password") }, // Placeholder text
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock, // Display lock icon inside the input field
                                contentDescription = "Password Icon"
                            )
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(), // Handle password visibility
                        trailingIcon = {
                            val image = if (passwordVisible)
                                Icons.Filled.Visibility
                            else
                                Icons.Filled.VisibilityOff

                            val description =
                                if (passwordVisible) "Hide password" else "Show password"

                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = image,
                                    contentDescription = description
                                ) // Password visibility toggle
                            }
                        },
                        singleLine = true, // Single-line input
                        isError = form.password.hasError() // Show error if there's one
                    )
                }

                // Spacer to ensure Login button is at the bottom
                Spacer(modifier = Modifier.weight(1f))

                // Column for buttons and error messages
                Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
                    if (!areCredentialsValid) {
                        // Display error message for invalid credentials
                        Text(
                            "Incorrect Email or Password",
                            color = MaterialTheme.colorScheme.error, // Error color
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }

                    // Login Button with BlueStandard background color
                    Button(
                        onClick = {
                            form.validate(true) // Validate form inputs

                            if (form.isValid) {
//                                AuthViewModel.loginUser(
//                                    email = form.email.state.value!!,
//                                    password = form.password.state.value!!
//                                )

                                runBlocking {
                                    launch {
                                        UserViewModel.loginUser(
                                            username = form.username.state.value!!,
                                            password = form.password.state.value!!,
                                            context
                                        ) { user -> // Provide the callback here
                                            Log.i("USER", user.toString())

                                            // Handle successful user login
                                            if (user != null) {
                                                // Navigate to LandingScreen if login is successful
                                                navigator.push(LandingScreen())

                                                Toast.makeText(
                                                    context,
                                                    "Successfully Logged In",
                                                    Toast.LENGTH_SHORT
                                                )
                                                    .show()  // Show a confirmation toast
                                            } else {
                                                Log.i("USER", "Login failed or user is null")

                                                Toast.makeText(
                                                    context,
                                                    "Username or Password Incorrect",
                                                    Toast.LENGTH_SHORT
                                                )
                                                    .show()  // Show a confirmation toast
                                            }
                                        }
                                    }
                                }


//                                if (userLoggedIn != null) {
//                                    navigator.push(LandingScreen())
//                                }
//
//                                Log.i("USER", userLoggedIn.toString())
//
//                                userViewModel.loginUser(
//                                    username = "eq.${form.email.state.value!!}",
//                                    password = "eq.${form.password.state.value!!}"
//                                )
//
//                                runBlocking {
//                                    launch {
//
//                                    }
//
//
//                                }
//
//                                if (userLoggedIn != null) {
//                                    navigator.push(LandingScreen())
//                                }
//
//                                Log.i("USER", userLoggedIn.toString())


                                // Firebase sign in attempt
//                                auth.signInWithEmailAndPassword(
//                                    form.email.state.value!!,
//                                    form.password.state.value!!
//                                ).addOnCompleteListener { task ->
//                                    if (task.isSuccessful) {
//                                        // Display success message
//                                        Toast.makeText(
//                                            context,
//                                            "Log In Successful",
//                                            Toast.LENGTH_SHORT
//                                        ).show()
//                                        // Navigate to landing screen
//                                        navigator.push(LandingScreen())
//                                    } else {
//                                        // Sign in failed: Display error message
//                                        areCredentialsValid = false
//                                        Toast.makeText(
//                                            context,
//                                            "Log In Failed: ${task.exception?.message}",
//                                            Toast.LENGTH_SHORT
//                                        ).show()
//                                    }
//                                }


                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = BlueStandard),// Set button background to BlueStandard
                        enabled = !isOffline
                    ) {
                        Text("Login", color = Color.White) // White text for contrast
                    }

                    // Forgot Password Button
                    TextButton(
                        onClick = { navigator.push(AccountScreen(mode = Mode.ForgotPassword)) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = false /*!isOffline*/
                        // as the forgot password functionality is not yet added, there's not really a point in having users access the page
                    ) {
                        Text(
                            "Forgot Password?",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    // Register Button for new users
                    TextButton(
                        onClick = { navigator.push(AccountScreen(mode = Mode.SignUp)) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isOffline
                    ) {
                        Text(
                            "Register",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        })
    }
}
