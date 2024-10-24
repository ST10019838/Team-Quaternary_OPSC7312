package com.example.bot_lobby.services

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.example.bot_lobby.models.User
import com.example.bot_lobby.view_models.SupabaseAuthViewModel
import com.example.bot_lobby.view_models.UserViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import org.json.JSONObject

@Composable
fun GoogleSignInButton(
    registerService: RegisterService,
    loginService: LoginService,
    viewModel: SupabaseAuthViewModel = viewModel(),
    userModel: UserViewModel = viewModel(),
    isReg: Boolean // This boolean parameter determines if it's a registration or login
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // Configure Google Sign-In
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("812262640049-f75l5h36lguq8d2009g3ca2u3hkhmps7.apps.googleusercontent.com") // Use your actual client ID
        .requestEmail()
        .build()

    val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(context, gso)

    // Define the button's click logic
    val onClick: () -> Unit = {
        // Launch Google Sign-In Intent
        val signInIntent = googleSignInClient.signInIntent
        coroutineScope.launch {
            try {
                // Launch the sign-in intent and handle the result
                val accountTask = GoogleSignIn.getSignedInAccountFromIntent(signInIntent)
                handleSignInResult(accountTask, registerService, loginService, isReg, context)
            } catch (e: Exception) {
                Log.e("GoogleSignInButton", "Sign-in failed: ${e.message}")
            }
        }
    }

    // UI for the button
    Column {
        Button(onClick = onClick) {
            Text(if (isReg) "Register with Google" else "Sign in with Google")
        }
    }
}

// Handle the sign-in result
private suspend fun handleSignInResult(
    task: Task<GoogleSignInAccount>,
    registerService: RegisterService,
    loginService: LoginService,
    isReg: Boolean,
    context: Context // Added context parameter for redirect
) {
    try {
        val account = task.getResult(ApiException::class.java)
        val userEmail = account.email ?: return

        Log.d("GoogleSignInButton", "User Email: $userEmail")

        // Create the new User object
        val newUser = User(
            id = null,
            role = 1,
            bio = null,
            username = userEmail,
            password = null, // No password, since it's an OAuth login
            biometrics = null,
            teamIds = mutableListOf(),
            isPublic = true,
            isLFT = true
        )

        // Check if the user is registering or logging in
        if (isReg) {
            // Register the user using the provided email
            val registrationResult = registerService.register(newUser) // This is a suspend function
            Log.d("GoogleSignInButton", "Registration Result: $registrationResult")
        } else {
            // Login the user using their email
            val loginResult = loginService.login(newUser.username, "") // This is a suspend function
            Log.d("GoogleSignInButton", "Login Result: $loginResult")
        }
    } catch (e: ApiException) {
        Log.e("GoogleSignInButton", "Sign-in failed: ${e.statusCode}")

        // Optionally redirect to Google signup if sign-in fails
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://accounts.google.com/signup"))
        context.startActivity(intent)
    }
}



// Function to decode the Google ID Token
fun parseIdToken(idToken: String): JSONObject {
    val parts = idToken.split(".")
    val payload = parts[1] // The payload is the second part of the token
    val decodedBytes = Base64.decode(payload, Base64.URL_SAFE or Base64.NO_WRAP)
    val json = String(decodedBytes)
    return JSONObject(json)
}
