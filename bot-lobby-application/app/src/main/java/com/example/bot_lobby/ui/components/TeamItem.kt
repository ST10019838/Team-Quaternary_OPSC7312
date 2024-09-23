package com.example.bot_lobby.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bot_lobby.models.Team

@Composable
fun TeamItem(team: Team) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Placeholder for team icon or image
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(Color.Gray)
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Team name
        Text(
            text = team.name,
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.weight(1f))

        // Number of team members
        Text(
            text = "${team.members.size} members",
            fontSize = 14.sp
        )
    }
}
