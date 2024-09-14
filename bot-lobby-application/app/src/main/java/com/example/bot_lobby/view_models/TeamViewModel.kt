package com.example.bot_lobby.view_models

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bot_lobby.api.RetrofitInstance
import com.example.bot_lobby.models.Team
import com.example.bot_lobby.models.TeamInsert
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Objects

class TeamViewModel : ViewModel() {
    private val _teamData: MutableStateFlow<List<Team>> = MutableStateFlow(listOf())
    val teamData: StateFlow<List<Team>> = _teamData

    fun getTeamData() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.Api.getTeams(RetrofitInstance.apiKey)
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
                RetrofitInstance.Api.createTeam(RetrofitInstance.apiKey, newTeam)
            } catch (exception: Exception) {
                Log.i("ERROR!", exception.message.toString())
            }
        }
    }

    fun updateTeam(team: Team) {
        viewModelScope.launch {
            try {
                RetrofitInstance.Api.updateTeam(
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
                RetrofitInstance.Api.deleteTeam(
                    RetrofitInstance.apiKey,
                    "eq.${teamId}",
                )
            } catch (exception: Exception) {
                Log.i("ERROR FOR!", exception.message.toString())
            }
        }
    }

}