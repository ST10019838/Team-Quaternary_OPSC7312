package com.example.bot_lobby.view_models

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bot_lobby.api.RetrofitInstance
import com.example.bot_lobby.models.AuthRequest
import com.example.bot_lobby.models.AuthResponse
import com.example.bot_lobby.models.UserInsert
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class AuthViewModel : ViewModel() {

    // StateFlow to observe the login/registration response
    private val _authState = MutableStateFlow<AuthResponse?>(null)
    val authState: StateFlow<AuthResponse?> = _authState

    // For login error state
    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState

    // Function to login
    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            try {
                val request = AuthRequest(email, password)
                val response = RetrofitInstance.AuthApi.login(RetrofitInstance.apiKey, request) // Pass request directly to body

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
    fun registerUser(email: String, password: String, username: String, typeId: Int, firstname: String, lastname: String, age: Int) {
        viewModelScope.launch {
            try {
                val request = AuthRequest(email, password)
                val response = RetrofitInstance.AuthApi.register(RetrofitInstance.apiKey, request)

                if (response.isSuccessful) {
                    val authResponse = response.body()
                    _authState.value = authResponse  // Update auth state with the response
                    Log.i("SUCCESS", "Registration successful: ${authResponse?.access_token}")

                    // Step to save user data in the users table
                    // No need to fetch userId, as it is auto-incremented
                    saveUserDataInTable(typeId, username, password, firstname, lastname, age)
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

    private fun saveUserDataInTable(typeId: Int, username: String, password: String, firstname: String, lastname: String, age: Int) {
        val userInsert = UserInsert(typeId, username, password, null, firstname, lastname, age)
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.UserApi.createUser(RetrofitInstance.apiKey, userInsert)
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

    // To clear the auth state after successful login/registration
    fun clearAuthState() {
        _authState.value = null
        _errorState.value = null
    }
}