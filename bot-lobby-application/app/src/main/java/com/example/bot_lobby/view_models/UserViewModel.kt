package com.example.bot_lobby.view_models

import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bot_lobby.api.RetrofitInstance
import com.example.bot_lobby.api.UserApi
import com.example.bot_lobby.models.FetchResponse
import com.example.bot_lobby.models.Team
import com.example.bot_lobby.models.User
import com.example.bot_lobby.services.LoginService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.bot_lobby.api.RetrofitInstance.UserApi
import com.example.bot_lobby.api.RetrofitInstance.apiKey
import com.example.bot_lobby.utils.BiometricAuthHelper
import com.google.android.gms.common.api.Response
import kotlinx.coroutines.runBlocking

class UserViewModel : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching

    private val _searchError: MutableStateFlow<String?> = MutableStateFlow(null)
    val searchError: StateFlow<String?> = _searchError.asStateFlow()

    private val _searchedUsers: MutableStateFlow<List<User>?> = MutableStateFlow(null)
    val searchedUsers: StateFlow<List<User>?> = _searchedUsers.asStateFlow()

    private val _userData: MutableStateFlow<List<User>> = MutableStateFlow(listOf())
    val userData: StateFlow<List<User>> = _userData


    suspend fun getTeamsUsers(team: Team): FetchResponse<List<User>> {
        var users: List<User> = listOf()
        var errorMessage: String? = null

//        viewModelScope.launch {
        try {
            // Create a query string that will be used to search for all users based on their ids
            var queryString = "in.("

            team.userIdsAndRoles.forEach { item ->
                // The first item in the pair is the user id
                queryString += "${item.id}"

                queryString += if (team.userIdsAndRoles.indexOf(item) == (team.userIdsAndRoles.size - 1)) ")" else ","
            }


            val response =
                UserApi.getUsers(RetrofitInstance.apiKey, queryString)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    users = body

                    Log.i("DATA!", body.toString())
                } else {
                    Log.e("ERROR!", "Response body is null")
                }
            } else {
                Log.e("ERROR", "Failed to fetch users: ${response.errorBody()?.string()}")
            }
        } catch (exception: Exception) {
            errorMessage = exception.message.toString()
            Log.e("ERROR!", exception.message.toString())
        }
//        }

        return FetchResponse(users, errorMessage)
    }
    //Register User Biometrics
    fun registerBiometricsForUser(user: User, isBiometricEnabled: Boolean, activity: AppCompatActivity, callback: (User?) -> Unit) {
        if (isBiometricEnabled) {
            viewModelScope.launch {
                val updatedUser = BiometricAuthHelper.registerBiometricData(user, activity)
                if (updatedUser.biometrics != null) {
                    updateUser(updatedUser) // Update the user in your database
                    callback(updatedUser)  // Pass updated user with biometrics registered back via callback
                } else {
                    Log.e("UserViewModel", "Biometric registration failed.")
                    callback(null)
                }
            }
        } else {
            Log.i("UserViewModel", "Biometric registration is disabled for user: ${user.username}")
            callback(user)
        }
    }

//    fun getUsersByName(apiKey: String, username: String): Response<List<User>> {
//        return UserApi.getUsers("username=eq.$username", apiKey)
//    }

    // Function to handle biometric login and fetch the user
    fun loginWithBiometrics(username: String, activity: AppCompatActivity, callback: (User?) -> Unit) {
        viewModelScope.launch {
            Log.d("UserViewModel", "Attempting to login with biometrics for user: $username")

            try {
                // Log API Key for debugging purposes
                Log.d("UserViewModel", "API Key: ${RetrofitInstance.apiKey}")

                // Use the Retrofit instance to fetch user data with API key in headers
                val response = UserApi.getUsersByUsername(RetrofitInstance.apiKey, "eq.$username")

                Log.d("UserViewModel", "API Response Status: ${response.code()}")

                if (response.isSuccessful && response.body() != null && response.body()!!.isNotEmpty()) {
                    val user = response.body()!![0] // Assuming the username is unique and you get a single user

                    // Check if biometrics are enabled
                    if (user.isBiometricEnabled) {
                        // Authenticate using biometrics
                        val isAuthenticated = BiometricAuthHelper.authenticate(activity).await()

                        if (isAuthenticated) {
                            AuthViewModel.updateUsersDetails(user)
                            callback(user) // Successful login
                        } else {
                            Log.e("UserViewModel", "Biometric authentication failed")
                            callback(null) // Authentication failed
                        }
                    } else {
                        Log.e("UserViewModel", "Biometrics not enabled for user")
                        callback(null) // Biometrics not enabled
                    }
                } else {
                    Log.e("UserViewModel", "User not found or API request failed. Response: ${response.errorBody()?.string()}")
                    callback(null) // User retrieval failed
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Exception occurred during login: ${e.message}")
                callback(null) // Handle exceptions gracefully
            }
        }
    }

    // Function to update an existing user
    fun updateUser(updatedUser: User) {
        viewModelScope.launch {
            try {
                val response = UserApi.updateUser(
                    RetrofitInstance.apiKey,
                    "eq.${updatedUser.id}",
                    updatedUser
                )
                if (response.isSuccessful) {
                    Log.i("SUCCESS", "User updated: ${response.body()}")
                    AuthViewModel.updateUsersDetails(updatedUser)
                } else {
                    Log.e("ERROR", "User update failed: ${response.errorBody()?.string()}")
                }
            } catch (exception: Exception) {
                Log.e("ERROR!", exception.message.toString())
            }
        }
    }

    // Function to delete a user
    fun deleteUser(userId: Int) {
        viewModelScope.launch {
            try {
                val response = UserApi.deleteUser(
                    RetrofitInstance.apiKey,
                    "eq.${userId}"
                )
                if (response.isSuccessful) {
                    Log.i("SUCCESS", "User deleted")
                } else {
                    Log.e("ERROR", "User deletion failed: ${response.errorBody()?.string()}")
                }
            } catch (exception: Exception) {
                Log.e("ERROR!", exception.message.toString())
            }
        }
    }

    fun clearSearchQuery() {
        _searchQuery.value = ""
        _searchedUsers.value = null
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun searchForUsers() {
        _isSearching.value = true

        if (searchQuery.value.isEmpty()) {
            return
        }

        viewModelScope.launch {
            try {
                // The following query was adapted from stackoverflow.com
                // Author: bgs (https://stackoverflow.com/users/2298058/bgs)
                // Link: https://stackoverflow.com/questions/17322228/check-if-a-column-contains-text-using-sql
                // Create a query string that will be used to search for all teams based on their ids
                val queryString = "like.*${searchQuery.value}*"


                // Fetch data
                val response =
                    UserApi.getUsersByName(
                        RetrofitInstance.apiKey,
                        queryString
                    )
                val body = response.body()

                Log.i("RESPONSE", response.toString())
                if (body != null) {
                    _searchedUsers.value = body

                    Log.i("DATA", body.toString())
                }
            } catch (exception: Exception) {
                _searchError.value = exception.message.toString()
                Log.i("ERROR!", exception.message.toString())
            }


            _isSearching.value = false

        }

    }

    // Create a new user
    fun createUser(newUser: User, callback: (User?) -> Unit) {
        Log.i("USER TO CREATE", newUser.toString())
        viewModelScope.launch {
            try {
                val response = UserApi.createUser(RetrofitInstance.apiKey, newUser)

                if (response.isSuccessful && response.body() != null) {
                    Log.d("UserViewModel", "User registered successfully: $newUser")
                    callback(newUser)
                } else {
                    Log.e("ERROR", "User creation failed: ${response.errorBody()?.string()}")
                    callback(null)
                }
            } catch (exception: Exception) {
                Log.e("ERROR!", exception.message.toString())
                callback(null)
            }
        }
    }

    // Login user
    fun loginUser(username: String, password: String, callback: (User?) -> Unit) {
        viewModelScope.launch {
            val response = LoginService(UserApi).login(username, password)

            if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                val user = response.body()!![0] // Get the first user from the list
                // save user to state
                AuthViewModel.updateUsersDetails(user)
                callback(user) // Return the user via the callback
            } else {
                Log.e("UserViewModel", "Login failed: ${response.errorBody()?.string()}")
                callback(null) // Return null in case of failure
            }
        }
    }

    // Fetch all users
    fun getUserData() {
        viewModelScope.launch {
            try {
                val response = UserApi.getUsers(RetrofitInstance.apiKey)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _userData.value = it
                        Log.d("UserViewModel", "Fetched users: $it")
                    } ?: Log.e("ERROR!", "Response body is null")
                } else {
                    Log.e("ERROR", "Failed to fetch users: ${response.errorBody()?.string()}")
                }
            } catch (exception: Exception) {
                Log.e("ERROR!", exception.message.toString())
            }
        }
    }

    fun clearData(){
        _searchQuery.value = ""

        _isSearching.value = false

       _searchError.value = null

        _searchedUsers.value = null

        _userData.value = emptyList()
    }
}