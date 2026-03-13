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
 */

// ─── Route Definitions (Type-safe với @Serializable) ─────────────────────────

@Serializable object AuthRoute
@Serializable object LoginRoute
@Serializable object RegisterRoute
@Serializable data class ForgotPasswordRoute(val token: String = "")

@Serializable object MainRoute
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
    // TODO: Implement AuthFlowApp
    // 1. val navController = rememberNavController()
    //
    // 2. NavHost(navController, startDestination = AuthRoute) {
    //
    //    // Auth Graph — nested navigation
    //    navigation<AuthRoute>(startDestination = LoginRoute) {
    //        composable<LoginRoute> {
    //            LoginScreen(
    //                onLoginSuccess = {
    //                    // Navigate + clear auth stack
    //                    navController.navigate(MainRoute) {
    //                        popUpTo<AuthRoute> { inclusive = true }
    //                    }
    //                },
    //                onNavigateToRegister = { navController.navigate(RegisterRoute) },
    //                onNavigateToForgotPassword = { navController.navigate(ForgotPasswordRoute()) }
    //            )
    //        }
    //        composable<RegisterRoute> { RegisterScreen(onRegistered = { navController.popBackStack() }, ...) }
    //        composable<ForgotPasswordRoute> { ForgotPasswordScreen(onBack = { navController.popBackStack() }) }
    //    }
    //
    //    // Main Graph
    //    navigation<MainRoute>(startDestination = HomeRoute) {
    //        composable<HomeRoute> {
    //            HomeScreen(
    //                onNavigateToProfile = { navController.navigate(ProfileRoute) },
    //                onLogout = {
    //                    navController.navigate(AuthRoute) {
    //                        popUpTo<MainRoute> { inclusive = true }
    //                    }
    //                }
    //            )
    //        }
    //        composable<ProfileRoute> { ProfileScreen(...) }
    //        composable<SettingsRoute> { SettingsScreen(...) }
    //    }
    // }
    //
    // GỢI Ý: popUpTo<AuthRoute> { inclusive = true }
    // → Xóa toàn bộ auth graph khỏi back stack
    // → User không thể back về Login sau khi vào Main
    Box {}
}

// ─── Auth Screens ─────────────────────────────────────────────────────────────

@Composable
private fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // TODO: Implement LoginScreen
    // - Column(fillMaxSize, padding=24.dp, Center, Center)
    // - Icon(Lock, size=64.dp, primary)
    // - Spacer + Text "Welcome Back" + Text subtitle
    // - Spacer(32.dp)
    // - OutlinedTextField email + OutlinedTextField password (PasswordVisualTransformation)
    // - TextButton "Forgot password?" (align End)
    // - Spacer + Button "Login" (fillMaxWidth) → onLoginSuccess
    // - Spacer + Row: Text "Don't have an account?" + TextButton "Register"
    Box {}
}

@Composable
private fun RegisterScreen(
    onRegistered: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // TODO: Implement RegisterScreen
    // - Scaffold với TopAppBar ("Create Account", navigationIcon = back)
    // - Column(padding=24.dp, spacedBy=12.dp):
    //   → Text subtitle
    //   → OutlinedTextField name, email, password
    //   → Spacer + Button "Create Account"
    Box {}
}

@Composable
private fun ForgotPasswordScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // TODO: Implement ForgotPasswordScreen với BackHandler
    // 1. var email by remember { mutableStateOf("") }
    //    var showConfirmDialog by remember { mutableStateOf(false) }
    //
    // 2. BackHandler(enabled = email.isNotEmpty()) {
    //        showConfirmDialog = true
    //    }
    //    GỢI Ý: BackHandler override back khi user đang nhập email
    //    → Tránh mất progress vô tình
    //
    // 3. if (showConfirmDialog) {
    //        AlertDialog(
    //            title = "Discard changes?",
    //            text = "Are you sure you want to go back?",
    //            confirmButton = TextButton("Discard", error color) { showConfirmDialog = false; onBack() },
    //            dismissButton = TextButton("Cancel") { showConfirmDialog = false }
    //        )
    //    }
    //
    // 4. Scaffold với TopAppBar + Column content:
    //    → Text instruction
    //    → Card info về deep link support
    //    → OutlinedTextField email
    //    → Button "Send Reset Link" (enabled = email.contains("@"))
    Box {}
}

// ─── Main Screens ─────────────────────────────────────────────────────────────

@Composable
private fun HomeScreen(
    onNavigateToProfile: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // TODO: Implement HomeScreen
    // - Scaffold với TopAppBar("Home", action = TextButton "Logout" error color)
    // - Column(padding=16.dp, spacedBy=12.dp):
    //   → Text "Welcome! 🏠"
    //   → Text "Auth stack cleared. Bấm Back sẽ thoát app."
    //   → Button "Go to Profile" (fillMaxWidth)
    Box {}
}

@Composable
private fun ProfileScreen(
    onNavigateToSettings: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // TODO: Implement ProfileScreen
    // - Scaffold với TopAppBar("Profile", navigationIcon = back)
    // - Column: Text "Profile Screen 👤" + OutlinedButton "Settings"
    Box {}
}

@Composable
private fun SettingsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // TODO: Implement SettingsScreen
    // - Scaffold với TopAppBar("Settings", navigationIcon = back)
    // - Column: Text "Settings ⚙️" + Text about back stack depth
    Box {}
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
