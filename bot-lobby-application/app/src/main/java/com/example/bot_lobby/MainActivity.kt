package com.example.bot_lobby

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.example.bot_lobby.models.User
import com.example.bot_lobby.ui.pages.EventsTab
import com.example.bot_lobby.ui.pages.HomeTab
import com.example.bot_lobby.ui.pages.ProfileTab
import com.example.bot_lobby.ui.pages.ScoutingTab
import com.example.bot_lobby.ui.pages.TeamsTab
import com.example.bot_lobby.ui.theme.BotLobbyTheme
import com.example.bot_lobby.view_models.SupabaseAuthViewModel
import com.example.bot_lobby.view_models.UserViewModel
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID

class MainActivity : ComponentActivity() {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json() // Use Kotlinx serialization for JSON
        }
    }

    private val loginService = LoginService(client)
    private val registerService = RegisterService(client)

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BotLobbyTheme {
                TabNavigator(HomeTab) {
                    Scaffold(
                        content = {
                            if (LocalTabNavigator.current.current == HomeTab) {
                                // Show Google Sign-In button in Home tab
                                GoogleSignInButton() // Add the button here
                            } else {
                                // For other tabs, show the current tab content
                                CurrentTab()
                            }
                        },
                        bottomBar = {
                            NavigationBar {
                                TabNavigationItem(EventsTab)
                                TabNavigationItem(TeamsTab)
                                TabNavigationItem(HomeTab)
                                TabNavigationItem(ProfileTab)
                                TabNavigationItem(ScoutingTab)
                            }
                        }
                    )
                }
            }
        }
    }
}



@Composable
private fun RowScope.TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current
    val isSelected = tabNavigator.current == tab

    NavigationBarItem(
        selected = isSelected,
        onClick = { tabNavigator.current = tab },
        icon = {
            Icon(
                tab.options.icon!!,
                contentDescription = tab.options.title,
                modifier = Modifier.alpha(if (!isSelected) 0.5f else 1f)
            )
        },
        label = {
            Text(
                text = tab.options.title,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.alpha(if (!isSelected) 0.5f else 1f),
                style = if (isSelected) MaterialTheme.typography.bodyMedium
                else MaterialTheme.typography.bodySmall
            )
        },
        colors = NavigationBarItemDefaults.colors(
            indicatorColor = MaterialTheme.colorScheme.primary,
            selectedTextColor = MaterialTheme.colorScheme.primary
        )
    )
}
@Composable
fun GoogleSignInButton(
    viewModel: SupabaseAuthViewModel = viewModel(),
    usermodel: UserViewModel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // State variable for age input
    var age by remember { mutableStateOf("") }

    val onClick: () -> Unit = {
        val credentialManager = CredentialManager.create(context)

        // Generate a nonce and hash it with SHA-256
        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        val hashedNonce = digest.fold("") { str, it -> str + "%02x".format(it) }

        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId("812262640049-f75l5h36lguq8d2009g3ca2u3hkhmps7.apps.googleusercontent.com")
            .setNonce(hashedNonce)
            .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        coroutineScope.launch {
            try {
                // Step 1: Get the Google ID Token
                val result = credentialManager.getCredential(
                    request = request,
                    context = context,
                )

                val googleIdTokenCredential = GoogleIdTokenCredential
                    .createFrom(result.credential.data)

                val googleIdToken = googleIdTokenCredential.idToken

                // Decode the ID token to extract user data
                val userData = parseIdToken(googleIdToken)
                val userEmail = userData.getString("email")
                val userFirstName = userData.getString("given_name")
                val userLastName = userData.getString("family_name")

                // Check for birthdate in user data
                val birthdateString: String? = userData.optString("birthdate") // Access as String
                val userAge = if (!birthdateString.isNullOrEmpty()) {
                    calculateAge(birthdateString) // Call with String birthdate
                } else {
                    age.toIntOrNull() ?: 0 // Fallback to input age if birthdate not available
                }

                // Log user information
                Log.d("GoogleSignInButton", "User Email: $userEmail")
                Log.d("GoogleSignInButton", "User First Name: $userFirstName")
                Log.d("GoogleSignInButton", "User Last Name: $userLastName")
                Log.d("GoogleSignInButton", "User Age: $userAge")

                // Create a new user object with data retrieved from Google
                val newUser = User(
                    biometrics = null,
                    role = 1,
                    password = null, // Handle password if needed
                    username = userEmail,
                    teamIds = null
                )

                // Log the new user object
                Log.d("GoogleSignInButton", "New User: $newUser")

                // Call the ViewModel's createUser function to save the user to db api
                usermodel.createUser(newUser)

                // Log successful user creation attempt
                Log.d("GoogleSignInButton", "Attempting to save user to database.")

                // Handle successful sign-in (e.g., navigate to the next screen)

            } catch (e: GetCredentialException) {
                Log.e("GoogleSignInButton", "GetCredentialException: ${e.message}")
            } catch (e: Exception) {
                Log.e("GoogleSignInButton", "Exception: ${e.message}")
            }
        }
    }

    // UI for collecting user data
    Column {
        // Button to trigger Google Sign-In and user registration
        Button(onClick = onClick) {
            Text("Sign in with Google")
        }
    }
}


// Function to calculate age from birthdate
fun calculateAge(birthDate: String): Int {
    // Assume birthDate is in format "YYYY-MM-DD"
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val birth = sdf.parse(birthDate)

    val birthCalendar = Calendar.getInstance()
    birthCalendar.time = birth

    val today = Calendar.getInstance()

    var age = today.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR)

    // Adjust age if the birthday hasn't occurred yet this year
    if (today.get(Calendar.DAY_OF_YEAR) < birthCalendar.get(Calendar.DAY_OF_YEAR)) {
        age--
    }

    return age
}


// Function to decode the Google ID Token
fun parseIdToken(idToken: String): JSONObject {
    val parts = idToken.split(".")
    val payload = parts[1] // The payload is the second part of the token
    val decodedBytes = Base64.decode(payload, Base64.URL_SAFE or Base64.NO_WRAP)
    val json = String(decodedBytes)
    return JSONObject(json)
}