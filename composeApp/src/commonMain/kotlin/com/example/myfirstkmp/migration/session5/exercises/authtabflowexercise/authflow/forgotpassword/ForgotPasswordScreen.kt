package com.example.myfirstkmp.migration.session5.exercises.authtabflowexercise.authflow.forgotpassword

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myfirstkmp.migration.session5.exercises.authtabflowexercise.base.BaseButton
import com.example.myfirstkmp.migration.session5.exercises.authtabflowexercise.base.BaseInputField
import com.example.myfirstkmp.migration.session5.exercises.authtabflowexercise.navigation.AuthFlow
import com.example.myfirstkmp.migration.session5.exercises.authtabflowexercise.navigation.Back
import com.example.myfirstkmp.migration.session5.exercises.authtabflowexercise.navigation.Login


@Composable
fun ForgotPasswordScreen(
    onNavigate: (AuthFlow) -> Unit
) {
    var email by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6F4))
            .padding(horizontal = 24.dp)
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        // Back
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = null,
            modifier = Modifier
                .size(28.dp)
                .clickable {
                    onNavigate(Back)
                }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Reset Password",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF101828)
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Enter your email address and we'll send you a link to reset your password.",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color(0xFF475467)
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        BaseInputField(
            label = "Email Address",
            value = email,
            onValueChange = { email = it },
            placeholder = "example@email.com"
        )

        Spacer(modifier = Modifier.height(28.dp))

        BaseButton(
            text = "Reset Password",
            icon = null,
            onClick = {}
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Back to Login",
            color = Color(0xFF344054),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable {
                    onNavigate(Login)
                }
        )

        Spacer(modifier = Modifier.height(60.dp))
    }
}