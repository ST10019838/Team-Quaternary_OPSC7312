package com.example.bot_lobby.ui.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.example.bot_lobby.R
import com.example.bot_lobby.ui.screens.EventsScreen

object EventsTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = "Events"
            // Use painterResource to load ic_event.png from res/drawable
            val icon = painterResource(id = R.drawable.ic_event)

            return remember {
                TabOptions(
                    index = 0u, // Tab index, you can adjust this if needed
                    title = title, // Tab title
                    icon = icon // Tab icon
                )
            }
        }

    @Composable
    override fun Content() {
        // Load the EventsScreen content
        EventsScreen()
    }
}
