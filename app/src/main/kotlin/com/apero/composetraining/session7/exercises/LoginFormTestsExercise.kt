package com.apero.composetraining.session7.exercises

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apero.composetraining.common.AppTheme

/**
 * ⭐⭐ BÀI TẬP 2: Login Form + Tests (Medium)
 *
 * Yêu cầu:
 * - Email TextField + Password TextField + Login Button
 * - Validation: email not empty, password >= 6 chars
 * - Button disabled khi invalid
 *
 * Tests (8 tests) — viết ở androidTest:
 * 1. Initial: button disabled
 * 2. Valid email + valid password → button enabled
 * 3. Empty email → button disabled
 * 4. Short password → button disabled
 * 5. Click login → loading indicator
 * 6. Error state → error message displayed
 * 7. testTag cho mỗi component
 * 8. Semantic: contentDescription cho accessibility
 */

@Composable
fun LoginFormScreen() {
    // TODO: [Session 7] Bài tập 2 - Tạo state cho email, password, isLoading, error
    // var email by remember { mutableStateOf("") }
    // var password by remember { mutableStateOf("") }
    // var isLoading by remember { mutableStateOf(false) }
    // var errorMessage by remember { mutableStateOf<String?>(null) }

    // TODO: [Session 7] Bài tập 2 - Validation
    // val isValid = email.isNotBlank() && password.length >= 6

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("login_screen"),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Login", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        // TODO: [Session 7] Bài tập 2 - Email TextField
        // OutlinedTextField(
        //     value = email,
        //     onValueChange = { email = it },
        //     label = { Text("Email") },
        //     modifier = Modifier.fillMaxWidth().testTag("email_field")
        // )

        // TODO: [Session 7] Bài tập 2 - Password TextField
        // OutlinedTextField(
        //     value = password,
        //     onValueChange = { password = it },
        //     label = { Text("Password") },
        //     modifier = Modifier.fillMaxWidth().testTag("password_field"),
        //     visualTransformation = PasswordVisualTransformation()
        // )

        // TODO: [Session 7] Bài tập 2 - Error message (nếu có)
        // errorMessage?.let { Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.testTag("error_text")) }

        // TODO: [Session 7] Bài tập 2 - Login Button (disabled khi invalid)
        // Button(
        //     onClick = { isLoading = true },
        //     enabled = isValid && !isLoading,
        //     modifier = Modifier.fillMaxWidth().testTag("login_button")
        // ) { if (isLoading) CircularProgressIndicator() else Text("Login") }

        // Placeholder
        Text("Bắt đầu code Login Form ở đây!")
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginFormScreenPreview() {
    AppTheme { LoginFormScreen() }
}
