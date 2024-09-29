package com.example.bot_lobby.view_models

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bot_lobby.api.RetrofitInstance
import com.example.bot_lobby.models.Team
import com.example.bot_lobby.models.TeamInsert
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TeamViewModel : ViewModel() {
    private val _teamData: MutableStateFlow<List<Team>> = MutableStateFlow(listOf())
    val teamData: StateFlow<List<Team>> = _teamData.asStateFlow()

    fun getTeamData() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.TeamApi.getTeams(RetrofitInstance.apiKey)
                val body = response.body()
                if (body != null) {
                    _teamData.value = response.body()!!
                }
            } catch (exception: Exception) {
                Log.i("ERROR!", exception.message.toString())
            }
        }
    }

    fun createTeam(newTeam: TeamInsert) {
        viewModelScope.launch {
            try {
                RetrofitInstance.TeamApi.createTeam(RetrofitInstance.apiKey, newTeam)
            } catch (exception: Exception) {
                Log.i("ERROR!", exception.message.toString())
            }
        }
    }

    fun updateTeam(team: Team) {
        viewModelScope.launch {
            try {
                RetrofitInstance.TeamApi.updateTeam(
                    RetrofitInstance.apiKey,
                    "eq.${team.id}",
                    team
                )
            } catch (exception: Exception) {
                Log.i("ERROR!", exception.message.toString())
            }
        }
    }

    fun deleteTeam(teamId: Int) {
        viewModelScope.launch {
            try {
                RetrofitInstance.TeamApi.deleteTeam(
                    RetrofitInstance.apiKey,
                    "eq.${teamId}",
                )
            } catch (exception: Exception) {
                Log.i("ERROR FOR!", exception.message.toString())
            }
        }
    }
}


//import android.util.Log
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.bot_lobby.models.Member
//import com.example.bot_lobby.models.Team
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.SharingStarted
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.combine
//import kotlinx.coroutines.flow.stateIn

// Define the ViewModel to handle team data
//class TeamViewModel : ViewModel() {
//
//    // StateFlow for the search query
//    private val _searchQuery = MutableStateFlow("")
//    val searchQuery: StateFlow<String> = _searchQuery
//
//    // StateFlow for the list of teams
//    private val _teams = MutableStateFlow<List<Team>>(emptyList())
//    val teams: StateFlow<List<Team>> = _teams
//
//    // StateFlow to filter teams based on searchQuery and teams
//    val filteredTeams: StateFlow<List<Team>> = combine(_searchQuery, _teams) { query, teamList ->
//        if (query.isEmpty()) {
//            teamList
//        } else {
//            teamList.filter {
//                it.teamtag.contains(query, ignoreCase = true) ||
//                        it.teamname.contains(
//                            query,
//                            ignoreCase = true
//                        ) ||  // Include search by teamname
//                        it.members.any { member ->
//                            member.playertag.contains(
//                                query,
//                                ignoreCase = true
//                            )
//                        }
//            }
//        }
//    }.stateIn(
//        viewModelScope, // Use viewModelScope for lifecycle-awareness
//        SharingStarted.Lazily, // Sharing strategy
//        emptyList() // Initial value
//    )
//
//    // Initialize the ViewModel with default data
//    init {
//        loadInitialData()
//        Log.d("TeamViewModel", "ViewModel initialized with data")
//    }
//
//    // Load initial data (teams and members)
//    private fun loadInitialData() {
//        val initialTeams = listOf(
//            Team(
//                teamtag = "Team Tag 1",
//                teamname = "Alpha Team",
//                members = listOf(
//                    Member(playertag = "Player Tag 1", role = "Leader"),
//                    Member(playertag = "Player Tag 2", role = "Member"),
//                    Member(playertag = "Player Tag 3", role = "Member"),
//                    Member(playertag = "Player Tag 4", role = "Member")
//                ),
//                isPublic = true
//            ),
//            Team(
//                teamtag = "Team Tag 2",
//                teamname = "Bravo Team",
//                members = listOf(
//                    Member(playertag = "Player Tag 4", role = "Leader"),
//                    Member(playertag = "Player Tag 6", role = "Member")
//                ),
//                isPublic = false
//            ),
//            Team(
//                teamtag = "Team Tag 3",
//                teamname = "Charlie Team",
//                members = listOf(
//                    Member(playertag = "Player Tag 7", role = "Leader"),
//                    Member(playertag = "Player Tag 8", role = "Member")
//                ),
//                isPublic = true
//            ),
//            Team(
//                teamtag = "Team Tag 4",
//                teamname = "Delta Team",
//                members = listOf(
//                    Member(playertag = "Player Tag 9", role = "Leader"),
//                    Member(playertag = "Player Tag 10", role = "Member")
//                ),
//                isPublic = false
//            )
//        )
//        _teams.value = initialTeams
//        Log.d("TeamViewModel", "Loaded initial teams: ${initialTeams.size}")
//    }
//
//    // Update the search query
//    fun updateSearchQuery(query: String) {
//        _searchQuery.value = query
//        Log.d("TeamViewModel", "Search query updated to: $query")
//    }
//
//    // Reload data (for manual reload testing)
//    fun reloadData() {
//        loadInitialData()
//        Log.d("TeamViewModel", "Data reloaded")
//    }
//}
