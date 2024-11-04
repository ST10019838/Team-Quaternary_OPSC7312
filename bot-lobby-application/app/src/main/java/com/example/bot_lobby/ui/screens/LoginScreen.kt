package com.example.bot_lobby.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.bot_lobby.services.GoogleSignInButton
import com.example.bot_lobby.MainActivity
import com.example.bot_lobby.R
import com.example.bot_lobby.forms.LoginForm
import com.example.bot_lobby.ui.theme.BlueStandard
import com.example.bot_lobby.utils.onFormValueChange
import com.example.bot_lobby.view_models.AuthViewModel
import com.example.bot_lobby.view_models.UserViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LoginScreen : Screen {

    @Composable
    override fun Content() {
        val userViewModel = UserViewModel()
        val navigator = LocalNavigator.currentOrThrow
        val form = LoginForm()
        val context = LocalContext.current

        val userLoggedIn by AuthViewModel.userLoggedIn.collectAsState()
        var areCredentialsValid by remember { mutableStateOf(true) }

        Scaffold(content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(horizontal = 25.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(R.string.sign_in_title),
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Image(
                        painter = painterResource(id = R.drawable.ic_bot_lobby_logo),
                        contentDescription = stringResource(R.string.app_name),
                        modifier = Modifier
                            .fillMaxWidth(1.0f)
                            .aspectRatio(16f / 9f)
                    )

                    Text(
                        text = stringResource(R.string.app_name),
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    GoogleSignInButton(
                        registerService = MainActivity.registerService,
                        loginService = MainActivity.loginService,
                        isReg = false,
                        navigator = navigator
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                /*Row(
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    HorizontalDivider(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100))
                            .fillMaxWidth()
                            .weight(1f),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = stringResource(R.string.or),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    HorizontalDivider(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100))
                            .fillMaxWidth()
                            .weight(1f),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }*/

                Spacer(modifier = Modifier.height(10.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
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
                        placeholder = { Text(stringResource(R.string.username_placeholder_login)) },
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
                        placeholder = { Text(stringResource(R.string.password_placeholder_login)) },
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

                            val description = if (passwordVisible) {
                                stringResource(R.string.hide_password)
                            } else {
                                stringResource(R.string.show_password)
                            }

                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = image,
                                    contentDescription = description
                                )
                            }
                        },
                        singleLine = true,
                        isError = form.password.hasError()
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
                    if (!areCredentialsValid) {
                        Text(
                            text = stringResource(R.string.error_incorrect_credentials),
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }

                    Button(
                        onClick = {
                            form.validate(true)
                            if (form.isValid) {
                                runBlocking {
                                    launch {
                                        userViewModel.loginUser(
                                            username = form.username.state.value!!,
                                            password = form.password.state.value!!
                                        ) { user ->
                                            Log.i("USER", user.toString())
                                            if (user != null) {
                                                navigator.push(LandingScreen())
                                                Toast.makeText(
                                                    context,
                                                    R.string.login_success,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            } else {
                                                Log.i("USER", "Login failed or user is null")
                                                Toast.makeText(
                                                    context,
                                                    R.string.error_invalid_login,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = BlueStandard)
                    ) {
                        Text(stringResource(R.string.login_title), color = Color.White)
                    }

                    TextButton(
                        onClick = { navigator.push(AccountScreen(mode = Mode.ForgotPassword)) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(R.string.forgot_password), style = MaterialTheme.typography.bodyMedium)
                    }

                    TextButton(
                        onClick = { navigator.push(AccountScreen(mode = Mode.SignUp)) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(R.string.register), style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        })
    }
}
