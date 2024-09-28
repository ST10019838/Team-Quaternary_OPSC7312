package com.example.bot_lobby.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import android.util.Log
import com.example.bot_lobby.models.Player

class PlayerViewModel : ViewModel() {

    // StateFlow for the search query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    // StateFlow for the list of players
    private val _players = MutableStateFlow<List<Player>>(emptyList())
    val players: StateFlow<List<Player>> = _players

    // StateFlow to filter players based on searchQuery and players
    val filteredPlayers: StateFlow<List<Player>> = combine(_searchQuery, _players) { query, playerList ->
        if (query.isEmpty()) {
            playerList
        } else {
            playerList.filter {
                it.player.contains(query, ignoreCase = true) ||
                        it.playertag.contains(query, ignoreCase = true) ||
                        it.teams.any { team -> team.contains(query, ignoreCase = true) }
            }
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        emptyList()
    )

    init {
        loadInitialData()
        Log.d("UserViewModel", "ViewModel initialized with data")
    }

    private fun loadInitialData() {
        val initialPlayers = listOf(
            Player(player = "user1@demo.com", playertag = "Player Tag 1", teams = listOf("Team A", "Team B","Team C", "Team D"), description = "Description for User 1"),
            Player(player = "user2@demo.com", playertag = "Player Tag 2", teams = listOf("Team A"), description = "Description for User 2"),
            Player(player = "user3@demo.com", playertag = "Player Tag 3", teams = emptyList(), description = "Description for User 3"),
            Player(player = "user4@demo.com", playertag = "Player Tag 4", teams = emptyList(), description = "Description for User 4"),
            Player(player = "user5@demo.com", playertag = "Player Tag 5", teams = listOf("Team A", "Team D"), description = "Description for User 5"),
            Player(player = "user6@demo.com", playertag = "Player Tag 6", teams = listOf("Team B", "Team D"), description = "Description for User 6"),
            Player(player = "user7@demo.com", playertag = "Player Tag 7", teams = listOf("Team C", "Team D"), description = "Description for User 7"),
            Player(player = "user8@demo.com", playertag = "Player Tag 8", teams = listOf("Team A", "Team B", "Team C"), description = "Description for User 8")
        )
        _players.value = initialPlayers
        Log.d("UserViewModel", "Loaded initial players: ${initialPlayers.size}")
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        Log.d("UserViewModel", "Search query updated to: $query")
    }

    fun reloadData() {
        loadInitialData()
        Log.d("UserViewModel", "Data reloaded")
    }
}