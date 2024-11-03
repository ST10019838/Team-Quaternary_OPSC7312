package com.example.bot_lobby.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.bot_lobby.models.Session
import com.example.bot_lobby.models.Team
import com.example.bot_lobby.models.User
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {
    @Query("SELECT * FROM session LIMIT 1")
    fun getSession(): Flow<Session?>

    @Query("DELETE FROM session")
    fun deleteSession()

    @Upsert
    suspend fun upsertSession(session: Session)

    @Delete
    suspend fun clearSession(session: Session)
}