package com.example.bot_lobby.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.bot_lobby.R
import com.example.bot_lobby.forms.SignUpForm
import com.example.bot_lobby.ui.theme.BlueStandard
import com.example.bot_lobby.utils.onFormValueChange

// Enum to handle screen mode
enum class Mode {
    SignUp, ForgotPassword
}

// Main AccountScreen class with screen mode

data class AccountScreen(
    val mode: Mode // Determines whether it's SignUp or ForgotPassword mode
) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val context = LocalContext.current
        val navigator = LocalNavigator.currentOrThrow
        val form = SignUpForm()
//        val auth = remember { FirebaseAuth.getInstance() }

        // Scaffold to provide the layout
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            if (mode == Mode.SignUp) "Register" else "Reset Password", // Dynamic title
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
                    },
                )
            },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 25.dp)
                        .padding(it)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Title text
//                    Text(
//                        text = if (mode == Mode.SignUp) "Create Your Account" else "Reset Your Password",
//                        fontSize = 26.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = MaterialTheme.colorScheme.onBackground,
//                        textAlign = TextAlign.Center
//                    )

                    // App logo
                    Image(
                        painter = painterResource(id = R.drawable.ic_bot_lobby_logo),
                        contentDescription = "App Logo",
                        modifier = Modifier
                            .fillMaxWidth(1.0f)
                            .aspectRatio(16f / 9f)
                    )

                    // App name text
                    Text(
                        text = "Bot Lobby",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )


                    // Email Input Field
                    OutlinedTextField(
                        value = form.email.state.value ?: "",
                        onValueChange = { value ->
                            onFormValueChange(
                                value = value,
                                form = form,
                                fieldState = form.email
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Email") },
                        isError = form.email.hasError(),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "Email Icon"
                            )
                        },
                        singleLine = true,
                        visualTransformation = VisualTransformation.None,
                        trailingIcon = {
                            if (form.email.hasError()) {
                                Icon(
                                    imageVector = Icons.Default.Error,
                                    contentDescription = "Error Icon",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    )

                    // Show the First Name and Last Name fields only in SignUp mode
                    if (mode == Mode.SignUp) {
                        // First Name Input Field
                        OutlinedTextField(
                            value = form.firstName.state.value ?: "",
                            onValueChange = { value ->
                                onFormValueChange(
                                    value = value,
                                    form = form,
                                    fieldState = form.firstName
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("First Name") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "First Name Icon"
                                )
                            },
                            singleLine = true,
                            isError = form.firstName.hasError()
                        )

                        // Last Name Input Field
                        OutlinedTextField(
                            value = form.lastName.state.value ?: "",
                            onValueChange = { value ->
                                onFormValueChange(
                                    value = value,
                                    form = form,
                                    fieldState = form.lastName
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Last Name") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Last Name Icon"
                                )
                            },
                            singleLine = true,
                            isError = form.lastName.hasError()
                        )
                    }

                    // Password Input Field with visibility toggle
                    var passwordVisible by remember { mutableStateOf(false) }
                    OutlinedTextField(
                        value = form.password.state.value ?: "",
                        onValueChange = { value ->
                            onFormValueChange(
                                value = value,
                                form = form,
                                fieldState = form.password
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text(if (mode == Mode.SignUp) "Password" else "New Password") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Password Icon"
                            )
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image = if (passwordVisible)
                                Icons.Filled.Visibility
                            else
                                Icons.Filled.VisibilityOff

                            val description =
                                if (passwordVisible) "Hide password" else "Show password"

                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(imageVector = image, contentDescription = description)
                            }
                        },
                        singleLine = true,
                        isError = form.password.hasError()
                    )

                    // Confirm Password Input Field
                    OutlinedTextField(
                        value = form.passwordConfirmation.state.value ?: "",
                        onValueChange = { value ->
                            onFormValueChange(
                                value = value,
                                form = form,
                                fieldState = form.passwordConfirmation
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Confirm Password") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Confirm Password Icon"
                            )
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image = if (passwordVisible)
                                Icons.Filled.Visibility
                            else
                                Icons.Filled.VisibilityOff

                            val description =
                                if (passwordVisible) "Hide password" else "Show password"

                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(imageVector = image, contentDescription = description)
                            }
                        },
                        singleLine = true,
                        isError = form.passwordConfirmation.hasError()
                    )

                    Spacer(Modifier.height(20.dp)) // Spacer between form and button

                    // Buttons
                    var actionWasSuccessful by remember { mutableStateOf(true) }
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

                        // Register/Reset Button with BlueStandard background
                        Button(
                            onClick = {
                                form.validate(true)
                                if (form.isValid) {
                                    if (mode == Mode.SignUp) {
//                                        createUser(
//                                            email = form.email.state.value!!,
//                                            password = form.password.state.value!!,
////                                            auth = auth,
//                                            context = context
//                                        )
                                    } else {
                                        // Handle forgot password
                                        Toast.makeText(
                                            context,
                                            "Password reset functionality is not yet implemented.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = BlueStandard // Use BlueStandard from your theme
                            )
                        ) {
                            Text(if (mode == Mode.SignUp) "Register" else "Reset Password")
                        }

                        // Show "Already have an account? Sign in here" only in SignUp mode
                        if (mode == Mode.SignUp) {
                            TextButton(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = { navigator.pop() }
                            ) {
                                Text(
                                    buildAnnotatedString {
                                        append("Already have an account? ")
                                        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.error)) {
                                            append("Sign in here")
                                        }
                                    },
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}

// Function to handle user registration
//fun createUser(
//    email: String,
//    password: String,
//    auth: FirebaseAuth,
//    context: android.content.Context
//) {
//    if (email.isNotEmpty() && password.isNotEmpty()) {
//        auth.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    Toast.makeText(
//                        context,
//                        "Registration successful! Please log in.",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                } else {
//                    val errorMessage = task.exception?.message
//                    Toast.makeText(context, "Registration failed: $errorMessage", Toast.LENGTH_LONG)
//                        .show()
//                }
//            }
//            .addOnFailureListener { exception ->
//                Toast.makeText(
//                    context,
//                    "Registration failed: ${exception.message}",
//                    Toast.LENGTH_LONG
//                ).show()
//            }
//    } else {
//        Toast.makeText(context, "Email and password cannot be empty", Toast.LENGTH_SHORT).show()
//    }
//}
