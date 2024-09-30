package com.example.bot_lobby.view_models

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bot_lobby.api.RetrofitInstance
import com.example.bot_lobby.api.RetrofitInstance.UserApi
import com.example.bot_lobby.models.AuthRequest
import com.example.bot_lobby.models.AuthResponse
import com.example.bot_lobby.models.Team
import com.example.bot_lobby.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

object AuthViewModel : ViewModel() {
    private val _userLoggedIn = MutableStateFlow<User?>(
//        User(
//            id = 1,
//            password = "123",
//            teamIds = listOf(1, 2),
//            username = "DJDare",
//            bio = "This is me DJDare"
//        )
        null
    )
    val userLoggedIn: StateFlow<User?> = _userLoggedIn.asStateFlow()

    private val _usersTeams: MutableStateFlow<List<Team>> = MutableStateFlow(listOf())
    val usersTeams: StateFlow<List<Team>> = _usersTeams.asStateFlow()

//    private val _usersTeamsUsers: MutableStateFlow<MutableMap<UUID, List<User>>> =
//        MutableStateFlow(mutableMapOf())
//    val usersTeamsUsers: StateFlow<Map<UUID, List<User>>> = _usersTeamsUsers.asStateFlow()

    // StateFlow to observe the login/registration response
    private val _authState = MutableStateFlow<AuthResponse?>(null)
    val authState: StateFlow<AuthResponse?> = _authState.asStateFlow()

    // For login error state
    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState.asStateFlow()

    // Function to login
    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            try {
                val request = AuthRequest(email, password)
                val response = RetrofitInstance.AuthApi.login(
                    RetrofitInstance.apiKey,
                    request
                ) // Pass request directly to body

                if (response.isSuccessful) {
                    val authResponse = response.body()
                    _authState.value = authResponse  // Update auth state with the response
                    Log.i("SUCCESS", "Login successful: ${authResponse?.access_token}")
                } else {
                    _errorState.value = response.errorBody()?.string()  // Update error state
                    Log.e("ERROR", "Login failed: ${response.errorBody()?.string()}")
                }
            } catch (e: HttpException) {
                _errorState.value = "Network error"
                Log.e("ERROR", "HttpException during login: ${e.message}")
            } catch (e: IOException) {
                _errorState.value = "IO error"
                Log.e("ERROR", "IOException during login: ${e.message}")
            }
        }
    }

    // Function to register
    fun registerUser(
        user: User
    ) {
        viewModelScope.launch {
            try {
                val request = AuthRequest(user.username, user.password.toString())
                val response = RetrofitInstance.AuthApi.register(RetrofitInstance.apiKey, request)

                if (response.isSuccessful) {
                    val authResponse = response.body()
                    _authState.value = authResponse  // Update auth state with the response
                    Log.i("SUCCESS", "Registration successful: ${authResponse?.access_token}")

                    // Step to save user data in the users table
                    // No need to fetch userId, as it is auto-incremented
                    saveUserDataInTable(user)
                } else {
                    _errorState.value = response.errorBody()?.string()  // Update error state
                    Log.e("ERROR", "Registration failed: ${response.errorBody()?.string()}")
                }
            } catch (e: HttpException) {
                _errorState.value = "Network error"
                Log.e("ERROR", "HttpException during registration: ${e.message}")
            } catch (e: IOException) {
                _errorState.value = "IO error"
                Log.e("ERROR", "IOException during registration: ${e.message}")
            }
        }
    }


    private fun saveUserDataInTable(
        user: User
    ) {
        viewModelScope.launch {
            try {
                val response =
                    UserApi.createUser(RetrofitInstance.apiKey, user)
                if (response.isSuccessful) {
                    Log.i("SUCCESS", "User data saved successfully: ${response.body()}")
                } else {
                    Log.e("ERROR", "Failed to save user data: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("ERROR", "Exception during saving user data: ${e.message}")
            }
        }
    }

//    // Login user
//    fun loginUser(username: String, password: String) {
//        viewModelScope.launch {
//            val response = LoginService(UserApi).login(username, password)
//            if (response.isSuccessful && response.body() != null) {
//                Log.d("UserViewModel", "Login successful: ${response.body()}")
//            } else {
//                Log.e("UserViewModel", "Login failed: ${response.errorBody()?.string()}")
//            }
//        }
//    }
//
//    // Register user
//    fun registerUser(newUser: User) {
//        viewModelScope.launch {
//            val response = RegisterService(UserApi).register(newUser)
//            if (response.isSuccessful && response.body() != null) {
//                Log.d("UserViewModel", "Registration successful: ${response.body()}")
//            } else {
//                Log.e("UserViewModel", "Registration failed: ${response.errorBody()?.string()}")
//            }
//        }
//    }

    fun signOut() {
        _userLoggedIn.value = null
    }

    // To clear the auth state after successful login/registration
    fun clearAuthState() {
        _authState.value = null
        _errorState.value = null
    }

    fun updateUsersDetails(updatedUser: User) {
        _userLoggedIn.value = updatedUser
        Log.d("Auth", "${updatedUser}")
        Log.d("Auth", "${_userLoggedIn.value}")

    }

    fun updateUsersTeam(team: Team) {
        // the following code was adapted from baeldung.com
        // Author: Albert Ache (https://www.baeldung.com/kotlin/author/albertache)
        // Link: https://www.baeldung.com/kotlin/list-mutable-change-element#:~:text=Using%20the%20map()%20Method,value%20we%20wish%20to%20update.
        _usersTeams.value = _usersTeams.value.map {
            if (it.id == team.id) {
                team
            } else {
                it
            }
        }
    }

    fun addTeamToUser(newTeam: Team, callback: (User?) -> Unit) {
        _usersTeams.value += newTeam

        val userCopy = _userLoggedIn.value

        _userLoggedIn.value = userCopy
        Log.i("VALS", _userLoggedIn.value.toString())

        callback(userCopy)

//        _usersTeamsUsers.value[newTeam.id!!] = listOf()
    }

//    fun updateTeamsUsers(teamId: UUID, teamsUsers: List<User>) {
//        _usersTeamsUsers.value[teamId] = teamsUsers
//    }

    fun removeTeamFromUser(team: Team) {
        _usersTeams.value -= team

//        _usersTeamsUsers.value.remove(team.id)
    }
}