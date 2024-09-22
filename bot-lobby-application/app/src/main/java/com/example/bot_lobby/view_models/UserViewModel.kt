package com.example.bot_lobby.view_models

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bot_lobby.api.RetrofitInstance
import com.example.bot_lobby.models.User
import com.example.bot_lobby.models.UserInsert
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    // StateFlow to hold and emit user data
    private val _userData: MutableStateFlow<List<User>> = MutableStateFlow(listOf())
    val userData: StateFlow<List<User>> = _userData

    // Function to fetch users from API
    fun getUserData() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.UserApi.getUsers(RetrofitInstance.apiKey)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        _userData.value = body
                    } else {
                        Log.e("ERROR!", "Response body is null")
                    }
                } else {
                    Log.e("ERROR", "Failed to fetch users: ${response.errorBody()?.string()}")
                }
            } catch (exception: Exception) {
                Log.e("ERROR!", exception.message.toString())
            }
        }
    }

    // Function to create a new user
    fun createUser(newUser: UserInsert) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.UserApi.createUser(RetrofitInstance.apiKey, newUser)
                if (response.isSuccessful) {
                    Log.i("SUCCESS", "User created: ${response.body()}")
                    getUserData() // Refresh user list after creation
                } else {
                    Log.e("ERROR", "User creation failed: ${response.errorBody()?.string()}")
                }
            } catch (exception: Exception) {
                Log.e("ERROR!", exception.message.toString())
            }
        }
    }

    // Function to update an existing user
    fun updateUser(user: User) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.UserApi.updateUser(
                    RetrofitInstance.apiKey,
                    "eq.${user.user_id}",
                    user
                )
                if (response.isSuccessful) {
                    Log.i("SUCCESS", "User updated: ${response.body()}")
                    getUserData() // Refresh user list after update
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
                val response = RetrofitInstance.UserApi.deleteUser(
                    RetrofitInstance.apiKey,
                    "eq.${userId}"
                )
                if (response.isSuccessful) {
                    Log.i("SUCCESS", "User deleted")
                    getUserData() // Refresh user list after deletion
                } else {
                    Log.e("ERROR", "User deletion failed: ${response.errorBody()?.string()}")
                }
            } catch (exception: Exception) {
                Log.e("ERROR!", exception.message.toString())
            }
        }
    }
}