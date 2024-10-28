package com.example.bot_lobby.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.bot_lobby.models.Team
import com.example.bot_lobby.models.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Upsert
    suspend fun upsertUser(user: User)

//    @Delete
//    suspend fun deleteTeam(team: Team)
//
//    @Query("DELETE FROM teams")
//    suspend fun deleteAll()
//
    @Query("SELECT * FROM teams")
    fun getUsers(): Flow<List<User>>
}