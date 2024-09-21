package com.example.bot_lobby.forms

import androidx.compose.runtime.mutableStateOf
import ch.benlu.composeform.FieldState
import ch.benlu.composeform.Form
import ch.benlu.composeform.FormField
import ch.benlu.composeform.validators.EmailValidator
import ch.benlu.composeform.validators.IsEqualValidator
import ch.benlu.composeform.validators.MinLengthValidator
import com.example.bot_lobby.forms.validators.IsRequiredValidator
import com.example.bot_lobby.forms.validators.MaxLengthValidator

class SignUpForm : Form() {

    override fun self(): Form {
        return this
    }

    // First Name Field
    @FormField
    val firstName = FieldState(
        state = mutableStateOf<String?>(""),
        validators = mutableListOf(
            IsRequiredValidator(),
            MaxLengthValidator(50)  // Max length for first name
        )
    )

    // Last Name Field
    @FormField
    val lastName = FieldState(
        state = mutableStateOf<String?>(""),
        validators = mutableListOf(
            IsRequiredValidator(),
            MaxLengthValidator(50)  // Max length for last name
        )
    )

    // Email Field
    @FormField
    val email = FieldState(
        state = mutableStateOf<String?>(""),
        validators = mutableListOf(
            IsRequiredValidator(),
            MaxLengthValidator(100),
            EmailValidator()
        )
    )

    // Password Field
    @FormField
    val password = FieldState(
        state = mutableStateOf<String?>(""),
        validators = mutableListOf(IsRequiredValidator(), MinLengthValidator(8))
    )

    // Password Confirmation Field
    @FormField
    val passwordConfirmation = FieldState(
        state = mutableStateOf<String?>(""),
        validators = mutableListOf(
            IsRequiredValidator(),
            MaxLengthValidator(100),
            IsEqualValidator({ password.state.value }, errorText = "Passwords don't match")
        )
    )
}
