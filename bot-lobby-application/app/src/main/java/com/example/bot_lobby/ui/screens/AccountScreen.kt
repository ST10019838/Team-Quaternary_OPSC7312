package com.example.bot_lobby.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.bot_lobby.R
import com.example.bot_lobby.forms.SignUpForm
import com.example.bot_lobby.models.User
import com.example.bot_lobby.ui.theme.BlueStandard
import com.example.bot_lobby.utils.onFormValueChange
import com.example.bot_lobby.view_models.UserViewModel

enum class Mode {
    SignUp, ForgotPassword
}

data class AccountScreen(
    val mode: Mode
) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val context = LocalContext.current
        val navigator = LocalNavigator.currentOrThrow
        val form = SignUpForm()
        val userViewModel = UserViewModel()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(id = if (mode == Mode.SignUp) R.string.register else R.string.reset_password),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.headlineSmall
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.arrow_back)
                            )
                        }
                    },
                )
            },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 25.dp)
                        .padding(it)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_bot_lobby_logo),
                        contentDescription = stringResource(R.string.app_logo),
                        modifier = Modifier
                            .fillMaxWidth(1.0f)
                            .aspectRatio(16f / 9f)
                    )

                    Text(
                        text = "Bot Lobby",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )

                    OutlinedTextField(
                        value = form.username.state.value ?: "",
                        onValueChange = { value ->
                            onFormValueChange(
                                value = value,
                                form = form,
                                fieldState = form.username
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text(stringResource(R.string.username_placeholder)) },
                        isError = form.username.hasError(),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = stringResource(R.string.username_icon)
                            )
                        },
                        singleLine = true,
                        visualTransformation = VisualTransformation.None,
                        trailingIcon = {
                            if (form.username.hasError()) {
                                Icon(
                                    imageVector = Icons.Default.Error,
                                    contentDescription = stringResource(R.string.error_icon),
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    )

                    var passwordVisible by remember { mutableStateOf(false) }
                    OutlinedTextField(
                        value = form.password.state.value ?: "",
                        onValueChange = { value ->
                            onFormValueChange(
                                value = value,
                                form = form,
                                fieldState = form.password
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text(stringResource(if (mode == Mode.SignUp) R.string.password_placeholder_signup else R.string.password_placeholder_reset)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = stringResource(R.string.password_icon)
                            )
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image = if (passwordVisible)
                                Icons.Filled.Visibility
                            else
                                Icons.Filled.VisibilityOff

                            val description =
                                if (passwordVisible) stringResource(R.string.hide_password) else stringResource(R.string.show_password)

                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(imageVector = image, contentDescription = description)
                            }
                        },
                        singleLine = true,
                        isError = form.password.hasError()
                    )

                    OutlinedTextField(
                        value = form.passwordConfirmation.state.value ?: "",
                        onValueChange = { value ->
                            onFormValueChange(
                                value = value,
                                form = form,
                                fieldState = form.passwordConfirmation
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text(stringResource(R.string.confirm_password_placeholder)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = stringResource(R.string.confirm_password_icon)
                            )
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image = if (passwordVisible)
                                Icons.Filled.Visibility
                            else
                                Icons.Filled.VisibilityOff

                            val description =
                                if (passwordVisible) stringResource(R.string.hide_password) else stringResource(R.string.show_password)

                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(imageVector = image, contentDescription = description)
                            }
                        },
                        singleLine = true,
                        isError = form.passwordConfirmation.hasError()
                    )

                    Spacer(Modifier.height(20.dp))

                    var actionWasSuccessful by remember { mutableStateOf(true) }
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (!actionWasSuccessful) {
                            Text(
                                stringResource(R.string.error_something_wrong),
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.Center
                            )
                        }

                        Button(
                            onClick = {
                                form.validate(true)
                                if (form.isValid) {
                                    if (mode == Mode.SignUp) {
                                        userViewModel.createUser(
                                            User(
                                                username = form.username.state.value!!,
                                                password = form.password.state.value!!
                                            )
                                        ){
                                            userViewModel.loginUser(
                                                username = form.username.state.value!!,
                                                password = form.password.state.value!!
                                            ) { user ->
                                                if(user == null){
                                                    Toast.makeText(context,
                                                        stringResource(R.string.error_invalid_login),
                                                        Toast.LENGTH_SHORT)
                                                        .show()
                                                } else{
                                                    navigator.push(LandingScreen())
                                                }
                                            }
                                        }
                                    } else {
                                        Toast.makeText(
                                            context,
                                            stringResource(R.string.password_reset_unimplemented),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = BlueStandard
                            )
                        ) {
                            Text(text = stringResource(if (mode == Mode.SignUp) R.string.register else R.string.reset_password))
                        }

                        if (mode == Mode.SignUp) {
                            TextButton(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = { navigator.pop() }
                            ) {
                                Text(
                                    buildAnnotatedString {
                                        append(stringResource(R.string.already_have_account))
                                        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.error)) {
                                            append(stringResource(R.string.sign_in_here))
                                        }
                                    },
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}
