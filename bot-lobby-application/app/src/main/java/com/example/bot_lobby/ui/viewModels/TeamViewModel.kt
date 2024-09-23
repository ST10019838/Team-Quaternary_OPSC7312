package com.example.bot_lobby.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.* // Import StateFlow, Flow, and coroutines
import android.util.Log
import com.example.bot_lobby.models.*


// Define the ViewModel to handle team data
class TeamViewModel : ViewModel() {

    // StateFlow for the search query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    // StateFlow for the list of teams
    private val _teams = MutableStateFlow<List<Team>>(emptyList())
    val teams: StateFlow<List<Team>> = _teams

    // StateFlow to filter teams based on searchQuery and teams
    val filteredTeams: StateFlow<List<Team>> = combine(_searchQuery, _teams) { query, teamList ->
        if (query.isEmpty()) {
            teamList
        } else {
            teamList.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.members.any { member -> member.name.contains(query, ignoreCase = true) }
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
        Log.d("TeamViewModel", "ViewModel initialized with data")
    }

    // Load initial data (teams and members)
    private fun loadInitialData() {
        val initialTeams = listOf(
            Team(
                name = "Team Tag 1",
                members = listOf(
                    Member(name = "Player Tag 3", role = "Leader"),
                    Member(name = "Player Tag 1", role = "Member")
                ),
                isPublic = true
            ),
            Team(
                name = "Team Tag 2",
                members = listOf(
                    Member(name = "Player Tag 4", role = "Leader"),
                    Member(name = "Player Tag 6", role = "Member")
                ),
                isPublic = false
            ),
            Team(
                name = "Team Tag 3",
                members = listOf(
                    Member(name = "Player Tag 5", role = "Leader"),
                    Member(name = "Player Tag 7", role = "Member")
                ),
                isPublic = true
            ),
            Team(
                name = "Team Tag 4",
                members = listOf(
                    Member(name = "Player Tag 8", role = "Leader")
                ),
                isPublic = false
            )
        )
        _teams.value = initialTeams
        Log.d("TeamViewModel", "Loaded initial teams: ${initialTeams.size}")
    }

    // Update the search query
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        Log.d("TeamViewModel", "Search query updated to: $query")
    }

    // Reload data (for manual reload testing)
    fun reloadData() {
        loadInitialData()
        Log.d("TeamViewModel", "Data reloaded")
    }
}
