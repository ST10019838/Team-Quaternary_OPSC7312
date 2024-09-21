package com.example.bot_lobby.ui.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource // Import for using the painterResource function
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.example.bot_lobby.R // Import the resource to access drawable resources
import com.example.bot_lobby.ui.screens.HomeScreen

// Define an object HomeTab that implements the Tab interface from Voyager.
object HomeTab: Tab {

    // Define the Tab options, such as title and icon.
    override val options: TabOptions
        @Composable
        get() {
            // The title of the tab
            val title = "Home"

            // Use painterResource to load the PNG drawable icon from the res/drawable folder.
            val icon = painterResource(id = R.drawable.ic_home)

            // Remember the TabOptions object so that it doesn't get recreated unnecessarily during recompositions.
            return remember {
                TabOptions(
                    index = 0u, // The tab index, used to determine its position
                    title = title, // The title of the tab
                    icon = icon // The icon, which is a painter loaded from the drawable resource
                )
            }
        }

    // Define the content that will be displayed when this tab is active.
    @Composable
    override fun Content() {
        // Load the HomeScreen composable, which represents the content for this tab.
        HomeScreen.Content()
    }
}
