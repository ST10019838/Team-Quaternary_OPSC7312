package com.example.bot_lobby.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import com.example.bot_lobby.models.Session
import com.example.bot_lobby.models.Team
import com.example.bot_lobby.ui.composables.formFields.Select
import com.example.bot_lobby.view_models.AnnouncementViewModel
import com.example.bot_lobby.ui.composables.formFields.TextField
import com.example.bot_lobby.view_models.SessionViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bot_lobby.forms.LoginForm
import com.example.bot_lobby.models.Announcement
import com.example.bot_lobby.utils.onFormValueChange
import java.util.UUID

@Composable
fun NewAnnouncementScreen(
    session: Session,
    viewModel: AnnouncementViewModel,
    onCancel: () -> Unit,
    onPostAnnouncement: () -> Unit,
    currentUserId: String // Pass in the currently logged-in user ID
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedTeam: Team? by remember { mutableStateOf(null) }

    val context = LocalContext.current
    val sessionViewModel = viewModel { SessionViewModel(context) }

    var teamDropdownExpanded by remember { mutableStateOf(false) }


    LaunchedEffect(true) {
        sessionViewModel.refreshUsersTeams()
    }

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
        Select<Team?>(
            label = "Team",
            value = selectedTeam,
            options = session.usersTeams,
//                itemFormatter = form.category.optionItemFormatter,
            isRequired = true,
            onSelect = {
                if (it != null) {
                    selectedTeam = it

                }

//                teamDropdownExpanded = false
//                    onFormValueChange(
//                        value = it,
//                        form = form,
//                        fieldState = form.category
//                    )
            },
//            hasError = form.team.hasError(),
//            errorText = form.team.errorText,
            placeholderText = "Select a Team",
            itemFormatter = { team ->
                team?.tag!!
            }
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

//                onFormValueChange(title, form, form.title)


//                onFormValueChange(
//                    value = it,
//                    form = form,
//                    fieldState = form.description
//                )
            },
//            hasError = form.title.hasError(),
//            errorText = form.title.errorText,
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
            value = /*form.body.state.value ?: "new"*/ description,
            label = "Description",
            isRequired = true,
            onChange = {
                description = it

//                onFormValueChange(description, form, form.body)

//                onFormValueChange(
//                    value = it,
//                    form = form,
//                    fieldState = form.description
//                )
            },
//            hasError = form.body.hasError(),
//            errorText = form.body.errorText,
//            hasError = form.password.hasError(),
//            errorText = form.password.errorText,

            placeholderText = "Add a Description",
            singleLine = false,
            maxLines = 3,
            useTextArea = true
        )

        Spacer(modifier = Modifier.height(3.dp))

        Column {
            Button(
                enabled = title.isNotEmpty() && description.isNotEmpty() && selectedTeam != null,
                onClick = {

                    session.userLoggedIn.id?.let {

//                            viewModel.saveAnnouncement(
//                                title = title,
//                                body = description,
//                                forTeamId = it.id,
//                                createdByUserId = it1
//                            )

                        viewModel.postAnnouncement(
                            Announcement(
                                title = title,
                                body = description,
                                forTeamId = selectedTeam?.id!!,
                                createdByUserId = it
                            )
                        )


                        onPostAnnouncement()  // Trigger callback to close or refresh the screen
                    }
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
