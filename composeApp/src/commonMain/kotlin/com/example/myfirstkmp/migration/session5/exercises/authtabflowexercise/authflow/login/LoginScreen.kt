package com.example.myfirstkmp.migration.session5.exercises.authtabflowexercise.authflow.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myfirstkmp.migration.session5.exercises.authtabflowexercise.authflow.login.component.LoginScreenFooter
import com.example.myfirstkmp.migration.session5.exercises.authtabflowexercise.authflow.login.component.LoginScreenInputFields
import com.example.myfirstkmp.migration.session5.exercises.authtabflowexercise.authflow.login.component.LoginScreenTitle
import com.example.myfirstkmp.migration.session5.exercises.authtabflowexercise.authflow.login.component.LoginScreenRememberPassword
import com.example.myfirstkmp.migration.session5.exercises.authtabflowexercise.authflow.login.intent.LoginIntent
import com.example.myfirstkmp.migration.session5.exercises.authtabflowexercise.base.BaseButton
import com.example.myfirstkmp.migration.session5.exercises.authtabflowexercise.navigation.AuthFlow
import com.example.myfirstkmp.migration.session5.exercises.authtabflowexercise.navigation.ForgotPassword
import com.example.myfirstkmp.migration.session5.exercises.authtabflowexercise.navigation.Register

@Composable
fun LoginScreen(
    onAction: (LoginIntent) -> Unit,
    onNavigate: (AuthFlow) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var keepLoggedIn by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6F4))
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(60.dp))

        LoginScreenTitle()

        Spacer(modifier = Modifier.height(32.dp))

        LoginScreenInputFields(
            email = email,
            password = password,
            typeEmail = { email = it },
            typePassword = { password = it },
            forgotPassword = {
                onNavigate(ForgotPassword)
            }
        )

        Spacer(modifier = Modifier.height(14.dp))

        LoginScreenRememberPassword(
            keepLoggedIn
        ) {
            keepLoggedIn = it
        }

        Spacer(modifier = Modifier.height(28.dp))

        BaseButton(
            text = "Sign in",
            icon = Icons.AutoMirrored.Filled.ArrowForward,
            onClick = {
                onAction(LoginIntent.Login)
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        LoginScreenFooter {
            onNavigate(Register)
        }
    }
}