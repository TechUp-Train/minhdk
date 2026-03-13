package com.apero.composetraining.session5.exercises

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apero.composetraining.common.AppTheme

/**
 * ⭐⭐ BÀI TẬP 2: Tab App với per-tab back stacks (Medium — 60 phút)
 *
 * Key insight: Navigation 3 = back stack là List → mỗi tab có List riêng!
 *
 * Yêu cầu:
 * - 3 tabs: Home / Explore / Profile
 * - Mỗi tab có back stack RIÊNG (3 mutableStateListOf)
 * - Scaffold + NavigationBar ở dưới
 * - Home tab: có thể navigate vào ArticleDetailKey(articleId)
 * - Explore tab: có thể navigate vào SearchResultKey(query)
 * - Profile tab: có thể navigate vào EditProfileKey
 * - Switch tab: back stack của tab cũ được GIỮ NGUYÊN (không reset)
 * - Back ở root của tab: không crash (backStack.size == 1 → không pop)
 *
 * Gợi ý cấu trúc:
 * ```kotlin
 * // Keys cho từng tab
 * data object HomeKey
 * data class ArticleDetailKey(val articleId: Int)
 * data object ExploreKey
 * data class SearchResultKey(val query: String)
 * data object ProfileKey
 * data object EditProfileKey
 *
 * enum class Tab { HOME, EXPLORE, PROFILE }
 *
 * // 3 back stacks riêng biệt — QUAN TRỌNG
 * val homeStack = rememberMutableStateListOf<Any>(HomeKey)
 * val exploreStack = rememberMutableStateListOf<Any>(ExploreKey)
 * val profileStack = rememberMutableStateListOf<Any>(ProfileKey)
 *
 * var selectedTab by remember { mutableStateOf(Tab.HOME) }
 * val currentStack = when (selectedTab) {
 *     Tab.HOME -> homeStack
 *     Tab.EXPLORE -> exploreStack
 *     Tab.PROFILE -> profileStack
 * }
 *
 * Scaffold(bottomBar = { TabBar(selectedTab, onTabSelect = { selectedTab = it }) }) { padding ->
 *     NavDisplay(
 *         backStack = currentStack,
 *         onBack = { if (currentStack.size > 1) currentStack.removeLastOrNull() },
 *         entryProvider = entryProvider {
 *             entry<HomeKey> { HomeTabScreen(onArticleClick = { homeStack.add(ArticleDetailKey(it)) }) }
 *             entry<ArticleDetailKey> { key -> ArticleDetailScreen(articleId = key.articleId) }
 *             // ... entries cho các tab khác
 *         }
 *     )
 * }
 * ```
 *
 * Tiêu chí nghiệm thu:
 * - Switch tab giữ back stack của tab cũ
 * - Back ở tab root không crash
 * - Navigate vào detail rồi switch tab, quay lại tab → vẫn thấy detail
 */

// TODO: [Session 6] Bài tập 2 - Định nghĩa keys cho 3 tabs + detail screens

// TODO: [Session 6] Bài tập 2 - Định nghĩa enum Tab { HOME, EXPLORE, PROFILE }

// TODO: [Session 6] Bài tập 2 - Implement TabAppScreen với 3 back stacks riêng
@Composable
fun TabAppScreen() {
    // TODO: 3 back stacks
    // val homeStack = rememberMutableStateListOf<Any>(HomeKey)
    // val exploreStack = rememberMutableStateListOf<Any>(ExploreKey)
    // val profileStack = rememberMutableStateListOf<Any>(ProfileKey)

    // TODO: selectedTab state + currentStack

    // TODO: Scaffold + NavigationBar + NavDisplay

    // Placeholder
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("TODO: Implement Tab App với per-tab back stacks")
    }
}

// TODO: [Session 6] Bài tập 2 - Implement screens cho mỗi tab
// HomeTabScreen, ArticleDetailScreen, ExploreTabScreen, SearchResultScreen, ProfileTabScreen, EditProfileScreen

@Preview(showBackground = true)
@Composable
private fun TabAppScreenPreview() {
    AppTheme { TabAppScreen() }
}
