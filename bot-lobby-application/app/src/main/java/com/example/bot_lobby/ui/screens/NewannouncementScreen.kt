package com.example.bot_lobby.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import com.example.bot_lobby.ui.composables.formFields.Select
import com.example.bot_lobby.view_models.AnnouncementViewModel
import com.example.bot_lobby.ui.composables.formFields.TextField

@Composable
fun NewAnnouncementScreen(
    viewModel: AnnouncementViewModel,
    onCancel: () -> Unit,
    onPostAnnouncement: () -> Unit,
    currentUserId: String // Pass in the currently logged-in user ID
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedTeam by remember { mutableStateOf("") }
    val teamList = listOf("Team A", "Team B", "Team C")
    var teamDropdownExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp)
    ) {
        Text(
            text = "New Announcement",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(1.dp))
        Select<String?>(
            label = "Team",
            value = selectedTeam,
            options = teamList,
//                itemFormatter = form.category.optionItemFormatter,
            isRequired = true,
            onSelect = {
                if (it != null) {
                    selectedTeam = it
                }
                teamDropdownExpanded = false
//                    onFormValueChange(
//                        value = it,
//                        form = form,
//                        fieldState = form.category
//                    )
            },
//                hasError = form.category.hasError(),
//                errorText = form.category.errorText,
            placeholderText = "Select a Team",
//                canCreateIfEmpty = true,
//                creationContent = {
//                    var isDialogOpen by rememberSaveable { mutableStateOf(false) }
//
//                    Button(
//                        onClick = { isDialogOpen = true },
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        Text("Create Category")
//                    }
//
//                    if (isDialogOpen) {
//                        CategoryCreationDialog(onDismiss = {
//                            isDialogOpen = false
//                        })
//                    }
//                }
        )

//        Spacer(modifier = Modifier.height(1.dp))
        HorizontalDivider(
            thickness = 1.dp,
//            color = Color.Gray,
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .align(Alignment.CenterHorizontally)
        )
//        Spacer(modifier = Modifier.height(1.dp))

        TextField(
            value = title,
            label = "Title",
            isRequired = true,
            onChange = {
                title = it
//                onFormValueChange(
//                    value = it,
//                    form = form,
//                    fieldState = form.description
//                )
            },
//            hasError = form.description.hasError(),
//            errorText = form.description.errorText,
            placeholderText = "Add a Title"
        )

//        Text(text = "Title *")
//        BasicTextField(
//            value = title,
//            onValueChange = { title = it },
//            modifier = Modifier
//                .fillMaxWidth()
//                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
//                .padding(8.dp),
//            textStyle = TextStyle(color = Color.Black)
//        )

//        Spacer(modifier = Modifier.height(16.dp))

//        Text(text = "Description *")
//        BasicTextField(
//            value = content,
//            onValueChange = { content = it },
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(120.dp)
//                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
//                .padding(8.dp),
//            textStyle = TextStyle(color = Color.Black)
//        )

        TextField(
            value = description,
            label = "Description",
            isRequired = false,
            onChange = {
                description = it
//                onFormValueChange(
//                    value = it,
//                    form = form,
//                    fieldState = form.description
//                )
            },
//            hasError = form.description.hasError(),
//            errorText = form.description.errorText,
            placeholderText = "Add a Description",
            singleLine = false,
            maxLines = 3,
            useTextArea = true
        )

        Spacer(modifier = Modifier.height(3.dp))

        Column {
            Button(
                onClick = {
                    viewModel.saveAnnouncement(
                        title = title,
                        content = description,
                        team = selectedTeam,
                        currentUserId = currentUserId
                    )
                    onPostAnnouncement()  // Trigger callback to close or refresh the screen
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
            ) {
                Text("Post Announcement")
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancel")
            }
        }

    }
}
