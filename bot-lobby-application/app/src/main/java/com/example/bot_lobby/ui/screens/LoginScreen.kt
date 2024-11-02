package com.example.bot_lobby.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.bot_lobby.MainActivity
import com.example.bot_lobby.R
import com.example.bot_lobby.forms.LoginForm
import com.example.bot_lobby.services.GoogleSignInButton
import com.example.bot_lobby.ui.theme.BlueStandard
import com.example.bot_lobby.utils.BiometricAuthHelper
import com.example.bot_lobby.utils.onFormValueChange
import com.example.bot_lobby.view_models.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginScreen : Screen, AppCompatActivity() {

    @Composable
    override fun Content() {
        val userViewModel = UserViewModel()
        val navigator = LocalNavigator.currentOrThrow
        val form = LoginForm() // Form that manages the login state
        val context = LocalContext.current

        var useBiometrics by remember { mutableStateOf(false) }
        val isBiometricAvailable = BiometricAuthHelper.isBiometricSupported(context)

        // Biometric prompt Setup
        val biometricPromptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Login")
            .setSubtitle("Use your biometric credential to log in")
            .setNegativeButtonText("Cancel")
            .build()

        // Create an instance of BiometricPrompt
        val biometricPrompt = BiometricPrompt(
            context as FragmentActivity,
            ContextCompat.getMainExecutor(context),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    // Handle successful biometric login here
                    if (!form.username.state.value.isNullOrBlank()) {
                        val username = form.username.state.value ?: ""
                        Log.d("LoginScreen", "Attempting biometric login for user: $username")

                        userViewModel.loginWithBiometrics(
                            username = username,
                            activity = context as AppCompatActivity // Pass the current activity context
                        ) { user ->
                            // Handle navigation and user session on successful biometric login
                            if (user != null) {
                                Log.d("LoginScreen", "Biometric login successful for user: $username")
                                navigator.push(LandingScreen()) // Ensure LandingScreen is defined appropriately
                                Toast.makeText(context, "Successfully Logged In", Toast.LENGTH_SHORT).show()
                            } else {
                                Log.e("LoginScreen", "Biometric login failed for user: $username")
                                Toast.makeText(context, "Biometric login failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Log.w("LoginScreen", "Username input is blank or null.")
                    }
                }

                override fun onAuthenticationFailed() {
                    Toast.makeText(context, "Biometric authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
        )

        // Check if biometric login is enabled and available
        if (useBiometrics && isBiometricAvailable) {
            // Trigger the biometric prompt
            biometricPrompt.authenticate(biometricPromptInfo)
        }

        Scaffold { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 25.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top part with the logo and app name
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Sign in Your Account",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

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
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                // Google Sign-In Button
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight() // Ensure it takes enough height
                ) {
                    GoogleSignInButton(
                        registerService = MainActivity.registerService,
                        loginService = MainActivity.loginService,
                        //activity = context as AppCompatActivity,
                        isReg = false, // Registration
                        navigator = navigator,
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Email and Password fields
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Username Field
                    OutlinedTextField(
                        value = form.username.state.value ?: "",
                        onValueChange = { value ->
                            onFormValueChange(value, form, form.username)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Username") },
                        isError = form.username.hasError(),
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Person, contentDescription = "Username Icon")
                        },
                        singleLine = true,
                        visualTransformation = VisualTransformation.None,
                        trailingIcon = {
                            if (form.username.hasError()) {
                                Icon(
                                    imageVector = Icons.Default.Error,
                                    contentDescription = "Error Icon",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    )

                    // Password Field
                    var passwordVisible by remember { mutableStateOf(false) }
                    OutlinedTextField(
                        value = form.password.state.value ?: "",
                        onValueChange = { value ->
                            onFormValueChange(value, form, form.password)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Password") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Lock, contentDescription = "Password Icon")
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(imageVector = image, contentDescription = if (passwordVisible) "Hide password" else "Show password")
                            }
                        },
                        singleLine = true,
                        isError = form.password.hasError()
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Toggle switch for biometric login
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Login with Biometrics")
                    if (!form.username.state.value.isNullOrBlank()) {
                        Switch(
                            checked = useBiometrics,
                            onCheckedChange = { checked ->
                                if (isBiometricAvailable) {
                                    useBiometrics = checked
                                } else {
                                    Toast.makeText(context, "Biometric login not available", Toast.LENGTH_SHORT).show()
                                }
                            }
                        )
                    } else {
                        Text("Username required", color = Color.Gray)
                    }
                }

                // Login Button
                Button(
                    onClick = {
                        if (useBiometrics) {
                            biometricPrompt.authenticate(biometricPromptInfo)
                        } else {
                            form.validate(true)
                            if (form.isValid) {
                                // Launch the coroutine in the correct scope
                                CoroutineScope(Dispatchers.Main).launch {
                                    userViewModel.loginUser(
                                        username = form.username.state.value ?: "",
                                        password = form.password.state.value ?: ""
                                    ) { user ->
                                        if (user != null) {
                                            navigator.push(LandingScreen())
                                            Toast.makeText(context, "Successfully Logged In", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(context, "Username or Password Incorrect", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = BlueStandard)
                ) {
                    Text("Login", color = Color.White)
                }

                // Forgot Password Button
                TextButton(
                    onClick = { navigator.push(AccountScreen(mode = Mode.ForgotPassword)) },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Forgot Password?", style = MaterialTheme.typography.bodyMedium)
                }

                // Register Button
                TextButton(
                    onClick = { navigator.push(AccountScreen(mode = Mode.SignUp)) },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Register", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
