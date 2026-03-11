package com.apero.composetraining.session5.exercises.authtabflowexercise.authflow.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apero.composetraining.session5.exercises.authtabflowexercise.authflow.register.component.RegisterScreenButtons
import com.apero.composetraining.session5.exercises.authtabflowexercise.authflow.register.component.RegisterScreenFields
import com.apero.composetraining.session5.exercises.authtabflowexercise.authflow.register.component.RegisterScreenHeader
import com.apero.composetraining.session5.exercises.authtabflowexercise.authflow.register.component.RegisterScreenTerm
import com.apero.composetraining.session5.exercises.authtabflowexercise.authflow.register.component.RegisterScreenTitle
import com.apero.composetraining.session5.exercises.authtabflowexercise.authflow.register.component.RegisterScreenToLogin
import com.apero.composetraining.session5.exercises.authtabflowexercise.base.BaseButton
import com.apero.composetraining.session5.exercises.authtabflowexercise.navigation.AuthFlow

@Composable
fun RegisterScreen(
    onNavigate: (AuthFlow) -> Unit
) {
    var fullName by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6F4))
            .padding(horizontal = 24.dp)
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        RegisterScreenHeader {
            onNavigate(AuthFlow.Back)
        }

        Spacer(modifier = Modifier.height(28.dp))

        RegisterScreenTitle()

        Spacer(modifier = Modifier.height(32.dp))

        RegisterScreenFields(
            fullName = fullName,
            email = email,
            password = password,
            typeFullName = { fullName = it },
            typeEmail = { email = it },
            typePassword = { password = it }
        )

        Spacer(modifier = Modifier.height(24.dp))

        BaseButton(
            text = "Register",
            icon = null,
            onClick = {}
        )

        Spacer(modifier = Modifier.height(20.dp))

        RegisterScreenTerm()

        Spacer(modifier = Modifier.height(24.dp))

        RegisterScreenButtons()

        Spacer(modifier = Modifier.height(32.dp))

        RegisterScreenToLogin {
            onNavigate(AuthFlow.Login)
        }
    }
}