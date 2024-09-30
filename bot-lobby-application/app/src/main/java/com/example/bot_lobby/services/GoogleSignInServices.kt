package com.example.bot_lobby.services

import android.util.Base64
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bot_lobby.models.User
import com.example.bot_lobby.view_models.SupabaseAuthViewModel
import com.example.bot_lobby.view_models.UserViewModel
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.security.MessageDigest
import java.util.UUID
import android.content.Intent
import android.net.Uri

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
    val credentialManager = CredentialManager.create(context)

    // Define the button's click logic
    val onClick: () -> Unit = {
        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        val hashedNonce = digest.fold("") { str, it -> str + "%02x".format(it) }

        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId("812262640049-f75l5h36lguq8d2009g3ca2u3hkhmps7.apps.googleusercontent.com") // Use your actual client ID
            .setNonce(hashedNonce)
            .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        coroutineScope.launch {
            try {
                // Step 1: Get the Google ID Token
                val result = credentialManager.getCredential(context, request)
                val googleIdTokenCredential =
                    GoogleIdTokenCredential.createFrom(result.credential.data)
                val googleIdToken = googleIdTokenCredential.idToken
                val userData = parseIdToken(googleIdToken)
                val userEmail = userData.getString("email")

                Log.d("GoogleSignInButton", "User Email: $userEmail")

                val newUser = User(
                    id = null,
                    role = 1,
                    bio = null,
                    username = userEmail,
                    password = null,
                    biometrics = null,
                    teamIds = null,
                    isPublic = true,
                    isLFT = true
                )

                // Check if the user is registering or logging in
                if (isReg) {
                    val registrationResult = registerService.register(newUser)
                    Log.d("GoogleSignInButton", "Registration Result: $registrationResult")
                } else {
                    val loginResult =
                        loginService.login(newUser.username, newUser.password.toString())
                    Log.d("GoogleSignInButton", "Login Result: $loginResult")
                }

            } catch (e: GetCredentialException) {
                Log.e("GoogleSignInButton", "GetCredentialException: ${e.message}")

                // Check if the message indicates no credentials available
                if (e.message?.contains("no credentials available") == true) {
                    // Launch intent to create a Google account
                    val intent =
                        Intent(Intent.ACTION_VIEW, Uri.parse("https://accounts.google.com/signup"))
                    context.startActivity(intent)
                }
            } catch (e: Exception) {
                Log.e("GoogleSignInButton", "Exception: ${e.message}")
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


// Function to decode the Google ID Token
fun parseIdToken(idToken: String): JSONObject {
    val parts = idToken.split(".")
    val payload = parts[1] // The payload is the second part of the token
    val decodedBytes = Base64.decode(payload, Base64.URL_SAFE or Base64.NO_WRAP)
    val json = String(decodedBytes)
    return JSONObject(json)
}