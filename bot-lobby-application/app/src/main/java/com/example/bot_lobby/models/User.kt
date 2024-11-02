package com.example.bot_lobby.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import java.util.UUID

@Entity(
    tableName = "users"
)
data class User(
    @PrimaryKey(autoGenerate = false)
    val id: Int? = null,                // Assuming the ID is auto-generated
    val role: Int = 1,                  // The type_id references user type (User, Admin, etc.)
    val bio: String? = null,
    val username: String,               // Username of the user
    val password: String? = null,       // Password of the user (may want to consider encryption later)
    var biometrics: String? = null,     // Nullable in case biometrics are not provided
    var teamIds: List<UUID>? = null,
    var isPublic: Boolean = true,
    var isLFT: Boolean = true,
    var email: String? = null,
    var isBiometricEnabled: Boolean = false  // New property for biometric registration
)

class TeamIdsConverters {

    @TypeConverter
    fun teamIdsToString(list: List<UUID>): String? {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun stringToTeamIds(string: String?): List<UUID> {
        return Gson().fromJson(string, Array<UUID>::class.java).asList()
    }
}
