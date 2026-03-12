package com.example.myfirstkmp.migration.session5.exercises.authtabflowexercise.authflow.login.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myfirstkmp.migration.session5.exercises.authtabflowexercise.base.BaseInputField
import com.example.myfirstkmp.migration.session5.exercises.authtabflowexercise.base.PasswordInputField

@Composable
fun ColumnScope.LoginScreenTitle() {
    Box(
        modifier = Modifier
            .size(64.dp)
            .background(Color(0xFFFDE9DF), shape = RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = null,
            tint = Color(0xFFE85D0C),
            modifier = Modifier.size(28.dp)
        )
    }

    Spacer(modifier = Modifier.height(32.dp))

    Text(
        text = "Welcome back",
        style = MaterialTheme.typography.headlineSmall.copy(
            fontWeight = FontWeight.Bold,
            color = Color(0xFF101828)
        )
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = "Enter your credentials to access your account",
        style = MaterialTheme.typography.bodyMedium.copy(
            color = Color(0xFF475467)
        ),
        textAlign = TextAlign.Center
    )
}

@Composable
fun ColumnScope.LoginScreenInputFields(
    email: String,
    password: String,
    typeEmail: (String) -> Unit,
    typePassword: (String) -> Unit,
    forgotPassword: () -> Unit
) {
    BaseInputField(
        label = "Email address",
        value = email,
        onValueChange = typeEmail,
        placeholder = "name@company.com"
    )

    Spacer(modifier = Modifier.height(20.dp))

    PasswordInputField(
        label = "Password",
        value = password,
        onValueChange = typePassword,
        onForgotClick = forgotPassword
    )
}

@Composable
fun ColumnScope.LoginScreenRememberPassword(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.align(Alignment.Start)
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text("Keep me logged in", color = Color(0xFF475467))
    }
}

@Composable
fun ColumnScope.LoginScreenFooter(
    createAccount: () -> Unit
) {
    Row {
        Text(
            "Don't have an account? ",
            color = Color(0xFF475467)
        )
        Text(
            "Create account",
            color = Color(0xFFE85D0C),
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clickable { createAccount() }
        )
    }

    Spacer(modifier = Modifier.height(40.dp))

    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Privacy Policy", color = Color(0xFF98A2B3), fontSize = 12.sp)
        Spacer(Modifier.width(16.dp))
        Text("Terms of Service", color = Color(0xFF98A2B3), fontSize = 12.sp)
        Spacer(Modifier.width(16.dp))
        Text("Support", color = Color(0xFF98A2B3), fontSize = 12.sp)
    }
}

