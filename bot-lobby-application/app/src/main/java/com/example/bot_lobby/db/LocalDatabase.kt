package com.example.bot_lobby.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.bot_lobby.dao.TeamDao
import com.example.bot_lobby.dao.UserDao
import com.example.bot_lobby.models.IdAndRoleConverters
import com.example.bot_lobby.models.Team
import com.example.bot_lobby.models.TeamIdsConverters
import com.example.bot_lobby.models.User

@Database(
    entities = [Team::class, User::class],
    version = 1
)
@TypeConverters(IdAndRoleConverters::class, TeamIdsConverters::class)
abstract class LocalDatabase: RoomDatabase() {
    abstract val teamDao: TeamDao

    abstract val userDao: UserDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: LocalDatabase? = null

        fun getDatabase(context: Context): LocalDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocalDatabase::class.java,
                    name = "local-db"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}