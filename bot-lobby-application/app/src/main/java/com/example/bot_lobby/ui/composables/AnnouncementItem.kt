package com.example.bot_lobby.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp
import com.example.bot_lobby.models.Announcement
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AnnouncementItem(
    announcement: Announcement,
    onClick: () -> Unit,
    cornerRadius: Dp = 12.dp  // Default corner radius for rounded edges
) {
    var expanded by remember { mutableStateOf(false) }
    var isOpen by rememberSaveable { mutableStateOf(false) }
    val rotation by animateFloatAsState(if (isOpen) 180f else 0f, label = "arrowRotationAnimation")

    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(cornerRadius))
            .fillMaxWidth()
//            .padding(5.dp)
            .clickable(onClick = {
                isOpen = !isOpen
            }),  // Make the entire item clickable
        border = BorderStroke(1.dp, Color.Gray),
        shape = RoundedCornerShape(cornerRadius),  // Rounded corners with specified radius
        color = MaterialTheme.colors.surface
    ) {
        Column(modifier = Modifier.padding(6.dp)) {
//            TextButton(
//                onClick = { isOpen = !isOpen },
//                modifier = Modifier.fillMaxWidth(),
////            colors = if (isOpen) ButtonColors(
////                containerColor = MaterialTheme.colorScheme.primary,
////                contentColor = MaterialTheme.colorScheme.background,
////                disabledContainerColor = MaterialTheme.colorScheme.primary,
////                disabledContentColor = MaterialTheme.colorScheme.primary
////            ) else ButtonDefaults.textButtonColors()
//            ) {
////            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
//
//
////            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
////            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
//
//
//            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = announcement.title,
                    style = MaterialTheme.typography.subtitle1
                )
//                Icon(
//                    Icons.Filled.KeyboardArrowDown,
//                    contentDescription = "arrow",
//                    modifier = Modifier.rotate(rotation)
//                )
            }



            AnimatedVisibility(visible = isOpen) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    HorizontalDivider(
                        thickness = 1.dp,
                        modifier = Modifier
                            .fillMaxWidth(0.25f)
                            .align(Alignment.CenterHorizontally)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        // Header with title and dropdown icon
                        Text(
                            text = announcement.content,
//                            style = MaterialTheme.typography.subtitle1
                        )
                    }


//                    Text(
//                        text = "${announcement.title}",
//                        style = MaterialTheme.typography.subtitle1
//                    )
//                    HorizontalDivider(
//                        thickness = 1.dp,
//                        modifier = Modifier
//                            .fillMaxWidth(0.25f)
//                            .align(Alignment.CenterHorizontally)
//                    )

                    Spacer(modifier = Modifier.height(1.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Team Tag: ",
//                            style = MaterialTheme.typography.subtitle1
                        )

                        Text(
                            text = announcement.team.tag,
//                            style = MaterialTheme.typography.subtitle1
                        )
                    }


                    // Format date
                    val dateFormatter =
                        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                    val formattedDate = dateFormatter.format(announcement.dateCreated)

                    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        // Date Created
                        Text(
                            text = "Date Created: $formattedDate",
                            style = MaterialTheme.typography.caption,
                            color = Color.Gray
                        )

                        // Created By (on a new line)
                        Text(
                            text = "Created By: ${announcement.createdByUserId.username}",
                            style = MaterialTheme.typography.caption,
                            color = Color.Gray
                        )
                    }

                }
            }
        }


    }

//    Column(
//        modifier = Modifier
//            .padding(10.dp)
//            .clip(RoundedCornerShape(12.dp))
//            .border(BorderStroke(1.dp, SolidColor(Color.Black)))
//    ) {
//
//
//        Surface(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 4.dp)
//                .clickable(onClick = onClick),  // Make the entire item clickable
//            border = BorderStroke(1.dp, Color.Gray),
//            shape = RoundedCornerShape(cornerRadius),  // Rounded corners with specified radius
//            color = MaterialTheme.colors.surface
//        ) {
//            Column(
//                modifier = Modifier.padding(8.dp)
//            ) {
//                // Header with title and dropdown icon
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(
//                        text = "${announcement.team}: ${announcement.title}",
//                        style = MaterialTheme.typography.subtitle1
//                    )
//                    Spacer(modifier = Modifier.weight(1f))
//                    IconButton(onClick = { expanded = !expanded }) {
//                        Icon(
//                            imageVector = Icons.Default.ArrowDropDown,
//                            contentDescription = "Expand announcement details",
//                            tint = if (expanded) MaterialTheme.colors.primary else Color.Gray
//                        )
//                    }
//                }
//
//                // Expanded content layout
//                if (expanded) {
//                    Column(modifier = Modifier.padding(top = 4.dp)) {
//                        Text(
//                            text = announcement.content,
//                            style = MaterialTheme.typography.body2,
//                            modifier = Modifier.padding(bottom = 8.dp)
//                        )
//
//                        // Format date
//                        val dateFormatter =
//                            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
//                        val formattedDate = dateFormatter.format(announcement.dateCreated)
//
//                        // Date Created
//                        Text(
//                            text = "Date Created: $formattedDate",
//                            style = MaterialTheme.typography.caption,
//                            color = Color.Gray
//                        )
//
//                        // Created By (on a new line)
//                        Text(
//                            text = "Created By: ${announcement.createdByUserId}",
//                            style = MaterialTheme.typography.caption,
//                            color = Color.Gray
//                        )
//                    }
//                }
//            }
//        }
//
//    }
}
