package com.example.bot_lobby.view_models


import android.content.Context
import android.util.Log
import android.widget.Toast
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
import com.example.bot_lobby.models.Session
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
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

    // Function to fetch users from API
//    fun getAllUsers() {
//        viewModelScope.launch {
//            try {
//                val response = RetrofitInstance.UserApi.getUsers(RetrofitInstance.apiKey)
//                if (response.isSuccessful) {
//                    val body = response.body()
//                    if (body != null) {
//                        _userData.value = body
//                    } else {
//                        Log.e("ERROR!", "Response body is null")
//                    }
//                } else {
//                    Log.e("ERROR", "Failed to fetch users: ${response.errorBody()?.string()}")
//                }
//            } catch (exception: Exception) {
//                Log.e("ERROR!", exception.message.toString())
//            }
//        }
//    }


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

    suspend fun getOnlineProfile(localUser: User): FetchResponse<User?> {
        var user: User? = null
        var errorMessage: String? = null

//        viewModelScope.launch {
        try {
            val response = UserApi.getUser(
                RetrofitInstance.apiKey,
                "eq.${localUser.id}",
            )

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    user = body.first()

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

        return FetchResponse(user, errorMessage)
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
//                    AuthViewModel.updateUsersDetails(updatedUser)
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
                }
            } catch (exception: Exception) {
                Log.e("ERROR!", exception.message.toString())
            }
        }
    }

    // Login user
    fun loginUser(username: String, password: String, context: Context, callback: (User?) -> Unit) {
        viewModelScope.launch {
            val response = LoginService(UserApi).login(username, password)

            if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                val user = response.body()!![0] // Get the first user from the list

                val sessionViewModel = SessionViewModel(context)

                // save user to state
//                AuthViewModel.updateUsersDetails(user)

                // get and save users teams
                val teamViewModel = TeamViewModel()
                var usersTeams = emptyList<Team>()
                val response = teamViewModel.getUsersTeams(user)

                if (response.errors.isNullOrEmpty()) {
                    usersTeams = response.data!!
                }

                val newSession = Session(
                    userLoggedIn = user,
                    usersTeams = usersTeams
                )

                sessionViewModel.upsertSession(newSession)

//                AuthViewModel.setUsersTeams(usersTeams)

                Log.d("USER LOGGED IN AS", sessionViewModel.session.value?.userLoggedIn.toString())
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


//import android.util.Log
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.bot_lobby.models.Player
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.SharingStarted
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.combine
//import kotlinx.coroutines.flow.stateIn
//
//class UserViewModel : ViewModel() {
//
//    // StateFlow for the search query
//    private val _searchQuery = MutableStateFlow("")
//    val searchQuery: StateFlow<String> = _searchQuery
//
//    // StateFlow for the list of players
//    private val _players = MutableStateFlow<List<Player>>(emptyList())
//    val players: StateFlow<List<Player>> = _players
//
//    // StateFlow to filter players based on searchQuery and players
//    val filteredPlayers: StateFlow<List<Player>> =
//        combine(_searchQuery, _players) { query, playerList ->
//            if (query.isEmpty()) {
//                playerList
//            } else {
//                playerList.filter {
//                    it.player.contains(query, ignoreCase = true) ||
//                            it.playertag.contains(query, ignoreCase = true) ||
//                            it.teams.any { team -> team.contains(query, ignoreCase = true) }
//                }
//            }
//        }.stateIn(
//            viewModelScope,
//            SharingStarted.Lazily,
//            emptyList()
//        )
//
//    init {
//        loadInitialData()
//        Log.d("UserViewModel", "ViewModel initialized with data")
//    }
//
//    private fun loadInitialData() {
//        val initialPlayers = listOf(
//            Player(
//                player = "user1@demo.com",
//                playertag = "Player Tag 1",
//                teams = listOf("Team A", "Team B", "Team C", "Team D"),
//                description = "Description for User 1"
//            ),
//            Player(
//                player = "user2@demo.com",
//                playertag = "Player Tag 2",
//                teams = listOf("Team A"),
//                description = "Description for User 2"
//            ),
//            Player(
//                player = "user3@demo.com",
//                playertag = "Player Tag 3",
//                teams = emptyList(),
//                description = "Description for User 3"
//            ),
//            Player(
//                player = "user4@demo.com",
//                playertag = "Player Tag 4",
//                teams = emptyList(),
//                description = "Description for User 4"
//            ),
//            Player(
//                player = "user5@demo.com",
//                playertag = "Player Tag 5",
//                teams = listOf("Team A", "Team D"),
//                description = "Description for User 5"
//            ),
//            Player(
//                player = "user6@demo.com",
//                playertag = "Player Tag 6",
//                teams = listOf("Team B", "Team D"),
//                description = "Description for User 6"
//            ),
//            Player(
//                player = "user7@demo.com",
//                playertag = "Player Tag 7",
//                teams = listOf("Team C", "Team D"),
//                description = "Description for User 7"
//            ),
//            Player(
//                player = "user8@demo.com",
//                playertag = "Player Tag 8",
//                teams = listOf("Team A", "Team B", "Team C"),
//                description = "Description for User 8"
//            )
//        )
//        _players.value = initialPlayers
//        Log.d("UserViewModel", "Loaded initial players: ${initialPlayers.size}")
//    }
//
//    fun updateSearchQuery(query: String) {
//        _searchQuery.value = query
//        Log.d("UserViewModel", "Search query updated to: $query")
//    }
//
//    fun reloadData() {
//        loadInitialData()
//        Log.d("UserViewModel", "Data reloaded")
//    }
//}