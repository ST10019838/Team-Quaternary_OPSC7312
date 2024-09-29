package com.example.bot_lobby.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.example.bot_lobby.ui.pages.*
import com.example.bot_lobby.ui.theme.BlueStandard
import com.example.bot_lobby.ui.theme.GreyUnselected
import androidx.compose.ui.graphics.Color

// Main layout function for the screen, defining the scaffold with a top bar and a bottom navigation bar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenLayout(tabNavigator: TabNavigator) {
    Scaffold(
        // Top app bar without a title
        topBar = {
            TopAppBar(
                title = {
                    // Removed the title text by leaving this composable empty
                    Spacer(modifier = Modifier) // Placeholder to keep the layout consistent
                }
            )
        },
        // Bottom navigation bar with tab items
        bottomBar = {
            NavigationBar {
                TabNavigationItem(EventsTab)  // Events tab
                TabNavigationItem(TeamsTab)   // Teams tab
                TabNavigationItem(HomeTab)    // Home tab
                TabNavigationItem(ProfileTab) // Profile tab
                TabNavigationItem(ScoutingTab) // Scouting tab
            }
        },
        // Content area for the currently selected tab
        content = {
            Box(
                modifier = Modifier
                    .padding(it) // Handle system window insets (e.g., status bar, navigation bar)
                    .padding(horizontal = 16.dp, vertical = 0.dp) // Add custom padding for layout
            ) {
                CurrentTab() // Show content of the currently selected tab
            }
        }
    )
}

// Function to define a single tab item in the bottom navigation bar
@Composable
private fun RowScope.TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current
    val isSelected = tabNavigator.current == tab

    // Define the appearance and behavior of a navigation item
    NavigationBarItem(
        selected = isSelected, // Whether the item is selected
        onClick = { tabNavigator.current = tab }, // Handle click events for the tab
        icon = {
            // Display the icon for the tab
            Icon(
                tab.options.icon!!, // Icon for the tab
                contentDescription = tab.options.title, // Accessibility description for the icon
                modifier = Modifier.alpha(if (!isSelected) 0.5f else 1f) // Adjust opacity based on selection
            )
        },
        label = {
            // Display the label for the tab
            Text(
                text = tab.options.title, // Tab title
                textAlign = TextAlign.Center, // Center-align the text
                maxLines = 1, // Limit the text to one line
                overflow = TextOverflow.Ellipsis, // Use ellipsis if the text overflows
                modifier = Modifier.alpha(if (!isSelected) 0.8f else 1f), // Adjust opacity based on selection
                style = if (isSelected) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.bodySmall
            )
        },
        // Use colors available in Material3's NavigationBarItemDefaults.colors()
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = BlueStandard, // Icon color when selected
            unselectedIconColor = GreyUnselected, // Dark grey color for unselected icon
            selectedTextColor = BlueStandard, // Text color when selected
            unselectedTextColor = GreyUnselected // Dark grey color for unselected text
        )
    )
}
