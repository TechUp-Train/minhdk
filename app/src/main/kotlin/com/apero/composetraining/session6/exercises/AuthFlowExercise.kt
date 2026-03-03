package com.apero.composetraining.session6.exercises

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.apero.composetraining.common.AppTheme
import kotlinx.serialization.Serializable

/**
 * ⭐⭐⭐⭐ BÀI TẬP NÂNG CAO BUỔI 6: Auth Flow với Navigation — nested back stacks
 *
 * Mô tả: App với 2 navigation graph: Auth (Login/Register/ForgotPassword) và Main (Home/Profile/Settings)
 *
 * Auth Flow:                         Main Flow:
 * Login ──→ Register                 Home ──→ Profile
 *   └──→ ForgotPassword               └──→ Settings
 *         ↑ BackHandler dialog
 *
 * Key concepts:
 * - Nested navigation graph (navigation.compose): authGraph + mainGraph
 * - popUpTo + inclusive = true: xóa auth stack sau khi login thành công
 * - BackHandler: override back behavior (ForgotPassword → show confirm dialog)
 * - NavController.navigate + popBackStack: điều hướng đúng cách
 *
 * Lưu ý: File này dùng Navigation Compose (navigation-compose:2.8.5)
 *   thay vì Navigation 3 (alpha) vì stable hơn trong môi trường training.
 *   Navigation 3 sẽ thay đổi pattern per-tab back stacks và NavDisplay.
 */

// ─── Route Definitions (Type-safe với @Serializable) ─────────────────────────

// Auth Graph routes
@Serializable object AuthRoute            // Entry point cho auth graph
@Serializable object LoginRoute
@Serializable object RegisterRoute
@Serializable data class ForgotPasswordRoute(val token: String = "")  // Deep link support

// Main Graph routes
@Serializable object MainRoute            // Entry point cho main graph
@Serializable object HomeRoute
@Serializable object ProfileRoute
@Serializable object SettingsRoute

// ─── App Entry Point ──────────────────────────────────────────────────────────

/**
 * AuthFlowApp — root của app
 *
 * Strategy:
 * - NavHost có 2 nested NavGraph: authGraph và mainGraph
 * - Start destination = AuthRoute (chưa login)
 * - Sau khi login thành công: navigate to MainRoute, popUpTo AuthRoute (inclusive)
 *   → AuthRoute bị xóa khỏi stack → Back button không quay về Login
 * - Sau khi logout: navigate to AuthRoute, popUpTo MainRoute (inclusive)
 */
@Composable
fun AuthFlowApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AuthRoute,
        modifier = modifier,
    ) {
        // ─── Auth Graph ─────────────────────────────────────────────────────
        // Nested navigation graph: gộp tất cả auth screens vào 1 sub-graph
        // Lợi ích: có thể navigate to AuthRoute để vào auth flow từ bất kỳ đâu
        navigation<AuthRoute>(startDestination = LoginRoute) {
            composable<LoginRoute> {
                LoginScreen(
                    onLoginSuccess = {
                        // Login thành công → navigate to MainRoute
                        // popUpTo(AuthRoute) { inclusive = true }:
                        //   → Xóa toàn bộ auth graph khỏi back stack
                        //   → User không thể back về Login sau khi vào Main
                        navController.navigate(MainRoute) {
                            popUpTo<AuthRoute> { inclusive = true }
                        }
                    },
                    onNavigateToRegister = {
                        navController.navigate(RegisterRoute)
                    },
                    onNavigateToForgotPassword = {
                        navController.navigate(ForgotPasswordRoute())
                    },
                )
            }

            composable<RegisterRoute> {
                RegisterScreen(
                    onRegistered = {
                        // Sau register thành công → về Login (không clear stack)
                        navController.popBackStack()
                    },
                    onBack = { navController.popBackStack() },
                )
            }

            composable<ForgotPasswordRoute> {
                // Deep link: "myapp://reset-password?token=xxx" → ForgotPasswordRoute
                // Trong production: handle intent trong MainActivity, parse token
                ForgotPasswordScreen(
                    onBack = { navController.popBackStack() },
                )
            }
        }

        // ─── Main Graph ─────────────────────────────────────────────────────
        navigation<MainRoute>(startDestination = HomeRoute) {
            composable<HomeRoute> {
                HomeScreen(
                    onNavigateToProfile = { navController.navigate(ProfileRoute) },
                    onLogout = {
                        // Logout → navigate về Auth, clear toàn bộ main stack
                        navController.navigate(AuthRoute) {
                            popUpTo<MainRoute> { inclusive = true }
                        }
                    },
                )
            }

            composable<ProfileRoute> {
                ProfileScreen(
                    onNavigateToSettings = { navController.navigate(SettingsRoute) },
                    onBack = { navController.popBackStack() },
                )
            }

            composable<SettingsRoute> {
                SettingsScreen(
                    onBack = { navController.popBackStack() },
                )
            }
        }
    }
}

// ─── Auth Screens ─────────────────────────────────────────────────────────────

@Composable
private fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // Logo/Icon
        Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Welcome Back",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "Sign in to continue",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = onNavigateToForgotPassword,
            modifier = Modifier.align(Alignment.End),
        ) {
            Text("Forgot password?")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onLoginSuccess, // Demo: không validate thật
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Don't have an account? ")
            TextButton(onClick = onNavigateToRegister) {
                Text("Register")
            }
        }
    }
}

@Composable
private fun RegisterScreen(
    onRegistered: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text("Create Account") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
        modifier = modifier,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Fill in your details to create an account",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onRegistered,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Create Account")
            }
        }
    }
}

@Composable
private fun ForgotPasswordScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var email by remember { mutableStateOf("") }
    var showConfirmDialog by remember { mutableStateOf(false) }

    // BackHandler: override hành vi back button
    // Thay vì back ngay → show dialog "Bạn có chắc muốn hủy không?"
    // Tại sao cần BackHandler?
    //   → Người dùng đang nhập email để reset password
    //   → Vô tình bấm back → mất progress → cần confirm
    BackHandler(enabled = email.isNotEmpty()) {
        // Chỉ show dialog khi đang nhập email (có gì để mất)
        showConfirmDialog = true
    }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Discard changes?") },
            text = { Text("Are you sure you want to go back? Your email will be lost.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showConfirmDialog = false
                        onBack()
                    },
                ) {
                    Text("Discard", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("Cancel")
                }
            },
        )
    }

    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text("Reset Password") },
                navigationIcon = {
                    IconButton(onClick = {
                        if (email.isNotEmpty()) {
                            showConfirmDialog = true
                        } else {
                            onBack()
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
        modifier = modifier,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = "Enter your email address and we'll send you a link to reset your password.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            // Deep link info card
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "🔗 Deep Link Support",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                    )
                    Text(
                        text = "myapp://reset-password?token=xxx\n→ Mở màn hình này trực tiếp từ email link",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                    )
                }
            }

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            Button(
                onClick = { /* Send reset email */ },
                modifier = Modifier.fillMaxWidth(),
                enabled = email.contains("@"),
            ) {
                Text("Send Reset Link")
            }
        }
    }
}

// ─── Main Screens ─────────────────────────────────────────────────────────────

@Composable
private fun HomeScreen(
    onNavigateToProfile: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text("Home") },
                actions = {
                    TextButton(onClick = onLogout) {
                        Text("Logout", color = MaterialTheme.colorScheme.error)
                    }
                },
            )
        },
        modifier = modifier,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "Welcome! 🏠",
                style = MaterialTheme.typography.headlineMedium,
            )
            Text(
                text = "Auth stack cleared. Bấm Back sẽ thoát app.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = onNavigateToProfile, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.Person, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Go to Profile")
            }
        }
    }
}

@Composable
private fun ProfileScreen(
    onNavigateToSettings: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text("Profile") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
        modifier = modifier,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text("Profile Screen 👤", style = MaterialTheme.typography.headlineMedium)

            OutlinedButton(onClick = onNavigateToSettings, modifier = Modifier.fillMaxWidth()) {
                Text("Settings")
            }
        }
    }
}

@Composable
private fun SettingsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
        modifier = modifier,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
        ) {
            Text("Settings ⚙️", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Back stack: Home → Profile → Settings\nBấm back 2 lần để về Home.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true, name = "Auth Flow - Light")
@Composable
private fun AuthFlowPreview() {
    AppTheme {
        AuthFlowApp()
    }
}

@Preview(
    showBackground = true,
    name = "Auth Flow - Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun AuthFlowDarkPreview() {
    AppTheme(darkTheme = true) {
        AuthFlowApp()
    }
}
