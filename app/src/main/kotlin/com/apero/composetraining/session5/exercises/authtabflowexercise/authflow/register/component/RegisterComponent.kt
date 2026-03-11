package com.apero.composetraining.session5.exercises.authtabflowexercise.authflow.register.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apero.composetraining.session5.exercises.authtabflowexercise.base.BaseButton
import com.apero.composetraining.session5.exercises.authtabflowexercise.base.BaseInputField
import com.apero.composetraining.session5.exercises.authtabflowexercise.base.PasswordInputField

@Composable
fun RowScope.SocialButton(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .weight(1f)
            .height(48.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.White,
            contentColor = Color(0xFF101828)
        )
    ) {
        Icon(imageVector = icon, contentDescription = null)
        Spacer(Modifier.width(8.dp))
        Text(text)
    }
}

@Composable
fun RegisterScreenHeader(
    clickBack: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { clickBack() }
    ) {
        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
        Spacer(Modifier.width(8.dp))
        Text(
            text = "Back to Login",
            color = Color(0xFF101828),
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun RegisterScreenTitle() {
    Text(
        text = "Create Account",
        style = MaterialTheme.typography.headlineSmall.copy(
            fontWeight = FontWeight.Bold,
            color = Color(0xFF101828)
        )
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = "Join us to start your journey today.",
        color = Color(0xFF475467),
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
fun RegisterScreenFields(
    fullName: String,
    email: String,
    password: String,
    typeFullName: (String) -> Unit,
    typeEmail: (String) -> Unit,
    typePassword: (String) -> Unit
) {
    BaseInputField(
        label = "FULL NAME",
        value = fullName,
        onValueChange = typeFullName,
        placeholder = "John Doe"
    )

    Spacer(modifier = Modifier.height(20.dp))

    BaseInputField(
        label = "EMAIL ADDRESS",
        value = email,
        onValueChange = typeEmail,
        placeholder = "name@example.com"
    )

    Spacer(modifier = Modifier.height(20.dp))

    PasswordInputField(
        label = "PASSWORD",
        value = password,
        onValueChange = typePassword,
        onForgotClick = {}
    )
}

@Composable
fun ColumnScope.RegisterScreenTerm() {
    Text(
        text = "By creating an account, you agree to our\nTerms of Service and Privacy Policy.",
        color = Color(0xFF475467),
        textAlign = TextAlign.Center,
        fontSize = 13.sp,
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(28.dp))


    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = DividerDefaults.Thickness,
            color = DividerDefaults.color
        )
        Text(
            "  OR REGISTER WITH  ",
            color = Color(0xFF98A2B3),
            style = MaterialTheme.typography.bodySmall
        )
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = DividerDefaults.Thickness,
            color = DividerDefaults.color
        )
    }
}

@Composable
fun ColumnScope.RegisterScreenButtons() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SocialButton(text = "Google", icon = Icons.Default.AccountCircle) {}
        SocialButton(text = "Apple", icon = Icons.Default.Apps) {}
    }
}

@Composable
fun ColumnScope.RegisterScreenToLogin(
    onClickLogin: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text("Already have an account? ", color = Color(0xFF475467))
        Text(
            "Log In",
            color = Color(0xFFE85D0C),
            fontWeight = FontWeight.Medium,
            modifier = Modifier.clickable { onClickLogin() }
        )
    }
}