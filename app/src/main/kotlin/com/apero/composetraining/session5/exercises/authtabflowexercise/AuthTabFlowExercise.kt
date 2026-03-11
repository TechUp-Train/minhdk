package com.apero.composetraining.session5.exercises.authtabflowexercise

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.apero.composetraining.common.AppTheme
import com.apero.composetraining.common.Login
import com.apero.composetraining.session5.exercises.authtabflowexercise.authflow.forgotpassword.ForgotPasswordScreen
import com.apero.composetraining.session5.exercises.authtabflowexercise.authflow.login.LoginScreen
import com.apero.composetraining.session5.exercises.authtabflowexercise.authflow.register.RegisterScreen
import com.apero.composetraining.session5.exercises.authtabflowexercise.base.BaseBottomBar
import com.apero.composetraining.session5.exercises.authtabflowexercise.navigation.AuthFlow
import com.apero.composetraining.session5.exercises.authtabflowexercise.navigation.navbarItems

/**
 * ⭐⭐⭐⭐ BÀI TẬP 4: Auth + Tab App (Advanced — 120 phút)
 *
 * Pattern này xuất hiện trong HẦU HẾT production apps.
 *
 * Yêu cầu:
 * - Auth flow: LoginKey → RegisterKey → ForgotPasswordKey
 * - Main flow: 3 tabs (Feed, Discover, Profile) với per-tab stacks
 * - `isAuthenticated: Boolean` state ở App level
 * - Login success → isAuthenticated = true → hiện Main flow (Auth stack bị unmount)
 * - Logout → isAuthenticated = false → hiện Auth flow
 * - Back từ Main KHÔNG về Login sau khi đã login
 * - BackHandler trên ForgotPasswordKey: "Bạn có chắc muốn quay lại?"
 * - LaunchedEffect khi vào ProfileKey: giả lập fetch user data (delay + update state)
 */

fun handleNavigateAuthFlow(destination: AuthFlow, backStack: NavBackStack<NavKey>) {
    when(destination) {
        is AuthFlow.Login -> {
            if(backStack.contains(AuthFlow.Login)) {
                while(backStack.last() is AuthFlow.Login) {
                    backStack.removeLastOrNull()
                }
            }
        }

        is AuthFlow.Register -> {
            backStack.add(AuthFlow.Register)
        }

        is AuthFlow.ForgotPassword -> {
            backStack.add(AuthFlow.ForgotPassword)
        }

        is AuthFlow.Back -> {
            if(backStack.size > 1) backStack.removeLastOrNull()
        }
    }
}

@Composable
fun AuthFlow() {
    val backstack = rememberNavBackStack(AuthFlow.Login)

    NavDisplay(
        backStack = backstack,
        entryProvider = entryProvider {
            entry<AuthFlow.Login> {
                LoginScreen()
            }

            entry<AuthFlow.Register> {
                RegisterScreen {
                    handleNavigateAuthFlow(it, backstack)
                }
            }

            entry<AuthFlow.ForgotPassword> {
                ForgotPasswordScreen {
                    handleNavigateAuthFlow(it, backstack)
                }
            }
        }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun AppFlow() {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BaseBottomBar(
                modifier = Modifier
                    .fillMaxSize()
                    .height(80.dp),
                items = navbarItems,
                isSelected = { pos ->
                    true
                },
                onItemSelected = { pos ->

                }
            )
        }
    ) { _ ->
        
    }
}

@Composable
fun AuthTabApp() {
    var isAuthenticated by remember { mutableStateOf(false) }

    if (isAuthenticated) AppFlow() else AuthFlow()
}

@Preview(showBackground = true)
@Composable
private fun AuthTabAppPreview() {
    AppTheme { AuthTabApp() }
}
