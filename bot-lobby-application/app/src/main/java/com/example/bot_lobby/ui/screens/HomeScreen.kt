package com.example.bot_lobby.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bot_lobby.R
import com.example.bot_lobby.ui.composables.CollapsibleSection

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display the "Welcome Back!" heading centered on the screen
        Text(
            text = stringResource(id = R.string.welcome_back),
            style = MaterialTheme.typography.h4,
            color = Color.Black,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(Alignment.CenterHorizontally)
        )

        // Section for displaying upcoming events
        CollapsibleSection(
            isOpen = false,
            heading = stringResource(id = R.string.upcoming_events),
            content = {
                Text(stringResource(id = R.string.coming_soon))
            }
        )

        // Add space between the two sections
        Spacer(modifier = Modifier.height(16.dp))

        // Section for displaying announcements
        CollapsibleSection(
            isOpen = false,
            heading = stringResource(id = R.string.announcements),
            content = {
                Text(stringResource(id = R.string.coming_soon))
            }
        )
    }
}
