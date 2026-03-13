package com.apero.composetraining.session5.exercises

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apero.composetraining.common.AppTheme

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

// TODO: [Session 6] Bài tập 1 - Định nghĩa type-safe keys
// data object WelcomeKey
// data object HomeKey

// TODO: [Session 6] Bài tập 1 - Implement TwoScreenFlowApp
@Composable
fun TwoScreenFlowApp() {
    // TODO: Tạo back stack
    // val backStack = rememberMutableStateListOf<Any>(WelcomeKey)

    // TODO: NavDisplay với entryProvider cho 2 screens
    // NavDisplay(
    //     backStack = backStack,
    //     onBack = { backStack.removeLastOrNull() },
    //     entryProvider = entryProvider {
    //         entry<WelcomeKey> { ... }
    //         entry<HomeKey> { ... }
    //     }
    // )

    // Placeholder — xóa khi implement
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("TODO: Implement 2-Screen Flow với Navigation 3")
    }
}

// TODO: [Session 6] Bài tập 1 - WelcomeScreen
// Params: onGetStarted: () -> Unit
// UI: Column centered, Text "👋 Chào mừng đến với Compose!", Button "Bắt đầu"
@Composable
fun WelcomeScreen(onGetStarted: () -> Unit) {
    // TODO
}

// TODO: [Session 6] Bài tập 1 - HomeScreen
// Params: onLogout: () -> Unit
// UI: Column centered, Text "🏠 Trang chủ", OutlinedButton "Đăng xuất"
@Composable
fun HomeScreen(onLogout: () -> Unit) {
    // TODO
}

@Preview(showBackground = true)
@Composable
private fun TwoScreenFlowPreview() {
    AppTheme { TwoScreenFlowApp() }
}
