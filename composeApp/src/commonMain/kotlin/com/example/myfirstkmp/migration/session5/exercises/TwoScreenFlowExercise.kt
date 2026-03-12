@file:Suppress("DEPRECATION")

package com.example.myfirstkmp.migration.session5.exercises

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.myfirstkmp.theme.AppTheme
import kotlinx.serialization.Serializable


/**
 * ⭐ BÀI TẬP 1: 2-Screen Flow (Easy — 30 phút)
 *
 * Học Navigation 3: Back stack là MutableList<Any>, không phải NavController
 *
 * Yêu cầu:
 * - Định nghĩa 2 keys: data object WelcomeKey + data object HomeKey
 * - Back stack: rememberMutableStateListOf<Any>(WelcomeKey)
 * - NavDisplay với entryProvider mapping key → screen
 * - Welcome screen: Button "Get Started" → backStack.add(HomeKey)
 * - Home screen: Button "Logout" → backStack.clear(); backStack.add(WelcomeKey)
 * - Back button (hardware/gesture) từ Home → Welcome hoạt động đúng
 *
 * Gợi ý:
 * ```kotlin
 * // 1. Keys
 * data object WelcomeKey
 * data object HomeKey
 *
 * // 2. Back stack
 * val backStack = rememberMutableStateListOf<Any>(WelcomeKey)
 *
 * // 3. NavDisplay
 * NavDisplay(
 *     backStack = backStack,
 *     onBack = { backStack.removeLastOrNull() },
 *     entryProvider = entryProvider {
 *         entry<WelcomeKey> { WelcomeScreen(onGetStarted = { backStack.add(HomeKey) }) }
 *         entry<HomeKey> { HomeScreen(onLogout = { backStack.clear(); backStack.add(WelcomeKey) }) }
 *     }
 * )
 * ```
 *
 * Tiêu chí nghiệm thu:
 * - Type-safe keys (không dùng String route)
 * - Navigate và back hoạt động đúng
 * - Logout clear stack (không còn back về Home sau logout)
 */

@Serializable
private data object Welcome

@Serializable
private data object Home


@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun TwoScreenFlowApp() {

    val entries = remember { mutableStateListOf<Any>(Welcome) }


//    BackHandler {
//        if (entries.size > 1) entries.removeLastOrNull()
//    }

    NavDisplay(
        backStack = entries,
        entryProvider = entryProvider {

            entry<Welcome> {
                WelcomeScreen {
                    entries.add(Home)
                }
            }

            entry<Home> {
                HomeScreen {
                    entries.remove(Home)
                }
            }
        }
    )
}

@Preview
@Composable
fun WelcomeScreen(
    onGetStarted: () -> Unit = {}
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "👋 Welcome to Compose!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Learn Jetpack Compose Navigation 3",
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = "with simple examples",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onGetStarted,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Start")
        }
    }
}

@Preview
@Composable
fun HomeScreen(
    onBack: () -> Unit = {}
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "🏠 Home",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "You are in Home Screen",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Go back")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TwoScreenFlowPreview() {
    AppTheme { TwoScreenFlowApp() }
}
