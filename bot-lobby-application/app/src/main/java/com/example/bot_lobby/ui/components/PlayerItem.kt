package com.example.bot_lobby.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.* // For layout components
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.* // For Material 3 components
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bot_lobby.R
import com.example.bot_lobby.models.Member

@Composable
fun PlayerItem(member: Member, onProfileClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Player icon (using ic_player_tag.png)
        Image(
            painter = painterResource(id = R.drawable.ic_player_tag),
            contentDescription = "Player Icon",
            modifier = Modifier.size(48.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Player tag
        Text(
            text = member.playertag,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.weight(1f))

        // Role button
        Button(
            onClick = { /* Handle role button click if needed */ },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            border = BorderStroke(1.dp, Color.Gray),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = member.role.uppercase(), fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.width(8.dp))

        // View Profile IconButton
        IconButton(
            onClick = onProfileClick
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_eye_profile),
                contentDescription = "View Profile",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
