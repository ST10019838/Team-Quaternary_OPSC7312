package com.example.bot_lobby.services

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.navigator.Navigator
import com.example.bot_lobby.api.RetrofitInstance
import com.example.bot_lobby.models.Team
import com.example.bot_lobby.ui.screens.LandingScreen
import com.example.bot_lobby.view_models.AuthViewModel
import com.example.bot_lobby.view_models.TeamViewModel
import com.google.android.gms.tasks.Task
import org.json.JSONObject

@Composable
fun GoogleSignInButton(
    registerService: RegisterService,
    loginService: LoginService,
    viewModel: SupabaseAuthViewModel = viewModel(),
    userModel: UserViewModel = viewModel(),
    isReg: Boolean, // Determines if it's a registration or login,
    navigator: Navigator
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // Configure Google Sign-In
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("812262640049-f75l5h36lguq8d2009g3ca2u3hkhmps7.apps.googleusercontent.com") // Your client ID
        .requestEmail()
        .build()

    val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(context, gso)

    // Define the Activity Result Launcher
    val signInLauncher: ActivityResultLauncher<Intent> = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        when (result.resultCode) {
            Activity.RESULT_OK -> {
                result.data?.let { data ->
                    handleSignInResult(data, registerService, loginService, isReg, coroutineScope, context, navigator)
                } ?: Log.e("GoogleSignInButton", "Sign-in canceled or failed: No data returned.")
            }
            Activity.RESULT_CANCELED -> {
                Log.e("GoogleSignInButton", "User canceled the sign-in.")
            }
            else -> {
                Log.e("GoogleSignInButton", "Sign-in failed with unexpected result code: ${result.resultCode}")
            }
        }
    }

    // Define the button's click logic
    val onClick: () -> Unit = {
        // Launch Google Sign-In Intent
        val signInIntent = googleSignInClient.signInIntent
        Log.d("GoogleSignInButton", "Launching Google Sign-In Intent")

        // Provide feedback to the user
        Toast.makeText(context, "Starting Google Sign-In...", Toast.LENGTH_SHORT).show() // Toast message to indicate the process

        signInLauncher.launch(signInIntent) // Launch the sign-in intent using the launcher
    }


    // UI for the button
    Column {
        Button(onClick = onClick) {
            Text(if (isReg) "Register with Google" else "Sign in with Google")
        }
    }
}

private fun handleSignInResult(
    data: Intent,
    registerService: RegisterService,
    loginService: LoginService,
    isReg: Boolean,
    coroutineScope: CoroutineScope,
    context: Context,
    navigator: Navigator
) {
    // Get the signed-in account from the Intent data
    val accountTask: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
    try {
        // Retrieve the signed-in account
        val account = accountTask.getResult(ApiException::class.java)
        Log.d("GoogleSignInButton", "Account retrieved successfully.")

        // Extract the email
        val userEmail = account.email ?: throw Exception("Email not found")
        Log.d("GoogleSignInButton", "User Email: $userEmail")

        // Create the new User object
        val newUser = User(
            id = null,
            role = 1,
            bio = null,
            username = userEmail,
            password = "password",
            biometrics = null,
            teamIds = listOf(),
            isPublic = true,
            isLFT = true,
            email = userEmail,
        )
        Log.d("GoogleSignInButton", "New User Object Created: $newUser")

        // Check if the user is registering or logging in
        coroutineScope.launch {
            // check if user exists. if they dont, register them, otherwise log them in
            val queryString = "eq.$userEmail"

            val res = RetrofitInstance.UserApi.getUsers(RetrofitInstance.apiKey, email = queryString)

            if(res.isSuccessful){
                // Register if nothing is found
                if(res.body().isNullOrEmpty()){
                    val registrationResult = registerService.register(newUser)
                    Log.d("GoogleSignInButton", "Registration Result: $registrationResult")
                }

                val loginResult = loginService.login(newUser.username, "")
                Log.d("GoogleSignInButton", "Login Result: $loginResult")

                if(loginResult.isSuccessful){
//                    Log.d("LoginService", "Login successful!")
////
//                    Log.d("LoginService 2", loginResult.body().toString())
//////
//////
//////                    // save user to state
//                    AuthViewModel.updateUsersDetails(loginResult.body()!![0] )
//
//                    // get and save users teams
//                    val teamViewModel = TeamViewModel()
//                    var usersTeams = emptyList<Team>()
//                    val res = teamViewModel.getUsersTeams(loginResult.body()!![0] )
//
//                    if (res.errors.isNullOrEmpty()) {
//                        usersTeams = res.data!!
//                    }
//
//                    AuthViewModel.setUsersTeams(usersTeams)

                    val userViewModel = UserViewModel()

                    userViewModel.loginUser(newUser.username, "password"){ user ->
                        Log.d("LoginService 2", user.toString())
                    }

                    navigator.push(LandingScreen())
                }
            }
//            if (isReg) {
//
//            } else {
//
//            }
        }
    } catch (e: ApiException) {
        Log.e("GoogleSignInButton", "Sign-in failed: ${e.statusCode}, Message: ${e.message}")
    } catch (e: Exception) {
        Log.e("GoogleSignInButton", "Error: ${e.message}")
    }
}

// Function to decode the Google ID Token (if needed)
fun parseIdToken(idToken: String): JSONObject {
    val parts = idToken.split(".")
    if (parts.size != 3) throw IllegalArgumentException("Invalid ID token format.")
    val payload = parts[1] // The payload is the second part of the token
    val decodedBytes = Base64.decode(payload, Base64.URL_SAFE or Base64.NO_WRAP)
    val json = String(decodedBytes)
    return JSONObject(json)
}