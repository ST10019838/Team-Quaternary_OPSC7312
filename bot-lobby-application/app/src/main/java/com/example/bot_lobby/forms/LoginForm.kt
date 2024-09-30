package com.example.bot_lobby.forms

import androidx.compose.runtime.mutableStateOf
import ch.benlu.composeform.FieldState
import ch.benlu.composeform.Form
import ch.benlu.composeform.FormField
import ch.benlu.composeform.validators.EmailValidator
import ch.benlu.composeform.validators.MinLengthValidator
import com.example.bot_lobby.forms.validators.IsRequiredValidator
import com.example.bot_lobby.forms.validators.MaxLengthValidator

class LoginForm : Form() {

    override fun self(): Form {
        return this
    }

    @FormField
    val username = FieldState(
        state = mutableStateOf<String?>(""),
        validators = mutableListOf(
            IsRequiredValidator(),
            MaxLengthValidator(100),
            EmailValidator()
        )
    )

    @FormField
    val password = FieldState(
        state = mutableStateOf<String?>(""),
        validators = mutableListOf(
            IsRequiredValidator(),
            MinLengthValidator(8)
        )
    )
}