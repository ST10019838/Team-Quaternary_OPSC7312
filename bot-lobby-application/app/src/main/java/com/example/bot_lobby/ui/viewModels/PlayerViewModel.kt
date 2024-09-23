package com.example.bot_lobby.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import android.util.Log
import com.example.bot_lobby.models.*

// Define the ViewModel to handle player data
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
                it.tag.contains(query, ignoreCase = true) || it.name.contains(query, ignoreCase = true)
            }
        }
    }.stateIn(
        viewModelScope, // Use viewModelScope for lifecycle-awareness
        SharingStarted.Lazily, // Sharing strategy
        emptyList() // Initial value
    )

    // Initialize the ViewModel with default data
    init {
        loadInitialData()
        Log.d("PlayerViewModel", "ViewModel initialized with data")
    }

    // Load initial data (sample players)
    private fun loadInitialData() {
        val initialPlayers = listOf(
            Player(tag = "player1", name = "Player Tag 1", team = "", description =""),
            Player(tag = "player2", name = "Player Tag 2", team = "", description =""),
            Player(tag = "player3", name = "Player Tag 3", team = "Team Tag 1", description =""),
            Player(tag = "player4", name = "Player Tag 4", team = "Team Tag 2", description =""),
            Player(tag = "player5", name = "Player Tag 5", team = "Team Tag 3", description =""),
            Player(tag = "player6", name = "Player Tag 6", team = "Team Tag 2", description =""),
            Player(tag = "player7", name = "Player Tag 7", team = "Team Tag 3", description =""),
            Player(tag = "player8", name = "Player Tag 8", team = "Team Tag 4", description ="")
        )
        _players.value = initialPlayers
        Log.d("PlayerViewModel", "Loaded initial players: ${initialPlayers.size}")
    }

    // Update the search query
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        Log.d("PlayerViewModel", "Search query updated to: $query")
    }

    // Reload data (for manual reload testing)
    fun reloadData() {
        loadInitialData()
        Log.d("PlayerViewModel", "Data reloaded")
    }
}
