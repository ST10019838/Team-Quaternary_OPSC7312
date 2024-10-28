package com.example.bot_lobby

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.bot_lobby.db.LocalDatabase
import com.example.bot_lobby.models.RoomTestModel
import com.example.bot_lobby.models.Team
import com.example.bot_lobby.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TestViewModel(context: Context) : ViewModel() {
    val someContext = context

    val testStuff = LocalDatabase.getDatabase(context).teamDao.getTeams().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    val users = LocalDatabase.getDatabase(context).userDao.getUsers().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )


    fun upsertTeam(team: Team){
        viewModelScope.launch {
            LocalDatabase.getDatabase(someContext).teamDao.upsertTeam(team)
        }
    }

    fun deleteTeams(){
        viewModelScope.launch {
            LocalDatabase.getDatabase(someContext).teamDao.deleteAll()
        }
    }

    fun upsertUser(user: User){
        viewModelScope.launch {
            LocalDatabase.getDatabase(someContext).userDao.upsertUser(user)
        }
    }

}