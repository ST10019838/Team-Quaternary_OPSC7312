package com.example.bot_lobby.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import java.util.UUID

@Entity
data class User(
    @PrimaryKey(autoGenerate = false)
    val id: String = null.toString(),           // Assuming the ID is auto-generated
    val role: String = 1.toString(),               // The type_id references user type (User, Admin, etc.)
    val bio: String? = null,
    val username: String,               // Username of the user
    val password: String? = null,       // Password of the user (may want to consider encryption later)
    var biometrics: String? = null,     // Nullable in case biometrics are not provided
    var teamIds: List<UUID>? = null,
    var isPublic: Boolean = true,
    var isLFT: Boolean = true,
    var email: String? = null
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
