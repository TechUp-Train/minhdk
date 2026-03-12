package com.apero.composetraining.session6.exercises

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.apero.composetraining.common.AppTheme
import kotlinx.serialization.Serializable

/**
 * ⭐⭐⭐⭐⭐ BÀI TẬP NÂNG CAO BUỔI 6: Full Tab App với per-tab navigation
 *
 * Mô tả: 3-tab app với NavController riêng cho từng tab, giống Instagram/Reddit
 *
 * Key concepts:
 * - Mỗi tab có NavController riêng → per-tab back stack
 * - saveState = true + restoreState = true: giữ tab state khi switch
 * - launchSingleTop = true: không tạo duplicate instances
 * - derivedStateOf: tính badge count từ state
 * - BackHandler: back từ tab root → switch về Feed tab
 */

// ─── Tab Definition ───────────────────────────────────────────────────────────

enum class MainTab(
    val label: String,
    val icon: ImageVector,
    val route: String,
) {
    FEED("Feed", Icons.Default.Home, "feed"),
    EXPLORE("Explore", Icons.Default.Search, "explore"),
    PROFILE("Profile", Icons.Default.Person, "profile"),
}

// ─── Route Definitions ────────────────────────────────────────────────────────

@Serializable object FeedScreenRoute
@Serializable data class ArticleDetailRoute(val articleId: Int)

@Serializable object ExploreScreenRoute
@Serializable data class CategoryRoute(val categoryName: String)
@Serializable data class ItemDetailRoute(val itemId: Int, val categoryName: String)

@Serializable object ProfileScreenRoute
@Serializable object EditProfileRoute
@Serializable object ProfileSettingsRoute

// ─── Main App ─────────────────────────────────────────────────────────────────

/**
 * TabAppNav3Screen — main composable với per-tab navigation
 *
 * Architecture:
 * - Mỗi tab có NavController riêng (feedNav, exploreNav, profileNav)
 * - selectedTab state kiểm soát NavController nào đang active
 * - Scaffold + NavigationBar ở ngoài (shared across tabs)
 * - NavHost bên trong mỗi tab
 */
@Composable
fun TabAppNav3Screen(modifier: Modifier = Modifier) {
    // TODO: Implement TabAppNav3Screen
    // 1. State setup:
    //    var selectedTab by remember { mutableStateOf(MainTab.FEED) }
    //    val feedNavController = rememberNavController()
    //    val exploreNavController = rememberNavController()
    //    val profileNavController = rememberNavController()
    //    var unreadCount by remember { mutableStateOf(3) }
    //
    // 2. Map tab → NavController:
    //    val navControllerMap = remember(...) { mapOf(FEED to feedNav, EXPLORE to exploreNav, ...) }
    //    val currentNavController = navControllerMap[selectedTab] ?: feedNavController
    //
    // 3. Badge count với derivedStateOf:
    //    val exploreBadge by remember { derivedStateOf { if (unreadCount > 0) unreadCount else null } }
    //
    // 4. Scaffold với bottomBar:
    //    TabNavigationBar(selectedTab, exploreBadge, onTabSelected = { tab →
    //        if (tab == selectedTab) {
    //            // Double-tap → pop to root
    //            currentNavController.popBackStack(getStartRoute(tab), inclusive = false)
    //        } else {
    //            selectedTab = tab
    //            if (tab == MainTab.EXPLORE) unreadCount = 0  // Clear badge
    //        }
    //    })
    //
    // 5. Box bên trong: 3 TabNavHost (chỉ visible khi tab được chọn)
    //    FeedTabNavHost(feedNavController, visible = selectedTab == FEED)
    //    ExploreTabNavHost(exploreNavController, visible = selectedTab == EXPLORE)
    //    ProfileTabNavHost(profileNavController, visible = selectedTab == PROFILE)
    //
    // 6. BackHandler: khi ở tab root (không phải Feed) → switch về Feed
    //    BackHandler(enabled = selectedTab != MainTab.FEED && isAtRoot) {
    //        selectedTab = MainTab.FEED
    //    }
    Box {}
}

private fun getStartRoute(tab: MainTab): Any = when (tab) {
    MainTab.FEED -> FeedScreenRoute
    MainTab.EXPLORE -> ExploreScreenRoute
    MainTab.PROFILE -> ProfileScreenRoute
}

// ─── Bottom Navigation Bar ────────────────────────────────────────────────────

@Composable
private fun TabNavigationBar(
    selectedTab: MainTab,
    exploreBadge: Int?,
    onTabSelected: (MainTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    // TODO: Implement TabNavigationBar
    // - NavigationBar:
    //   MainTab.entries.forEach { tab →
    //       NavigationBarItem(
    //           icon = {
    //               if (tab == EXPLORE && exploreBadge != null) {
    //                   BadgedBox(badge = { Badge { Text(exploreBadge.toString()) } }) { Icon(...) }
    //               } else { Icon(...) }
    //           },
    //           label = { Text(tab.label) },
    //           selected = selectedTab == tab,
    //           onClick = { onTabSelected(tab) }
    //       )
    //   }
    Box {}
}

// ─── Feed Tab ─────────────────────────────────────────────────────────────────

@Composable
private fun FeedTabNavHost(
    navController: NavHostController,
    visible: Boolean,
    modifier: Modifier = Modifier,
) {
    // TODO: Implement FeedTabNavHost
    // if (!visible) return
    // NavHost(navController, startDestination = FeedScreenRoute, fillMaxSize) {
    //     composable<FeedScreenRoute> { FeedListScreen(onArticleClick = { id → navController.navigate(ArticleDetailRoute(id)) }) }
    //     composable<ArticleDetailRoute> { backStackEntry →
    //         val route = backStackEntry.toRoute<ArticleDetailRoute>()
    //         ArticleDetailScreen(articleId = route.articleId, onBack = { navController.popBackStack() })
    //     }
    // }
    Box {}
}

// ─── Explore Tab ──────────────────────────────────────────────────────────────

@Composable
private fun ExploreTabNavHost(
    navController: NavHostController,
    visible: Boolean,
    modifier: Modifier = Modifier,
) {
    // TODO: Implement ExploreTabNavHost
    // 3 levels deep: Explore → Category → ItemDetail
    // Tương tự FeedTabNavHost
    Box {}
}

// ─── Profile Tab ──────────────────────────────────────────────────────────────

@Composable
private fun ProfileTabNavHost(
    navController: NavHostController,
    visible: Boolean,
    modifier: Modifier = Modifier,
) {
    // TODO: Implement ProfileTabNavHost
    // Profile → EditProfile / ProfileSettings
    Box {}
}

// ─── Feed Tab Screens ─────────────────────────────────────────────────────────

@Composable
private fun FeedListScreen(
    onArticleClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    // TODO: Implement FeedListScreen
    // - Column(fillMaxSize, padding=16.dp)
    // - Text "Feed 🏠" (headlineMedium, Bold)
    // - Spacer(16.dp)
    // - repeat(5) { index →
    //     Card(onClick = { onArticleClick(index + 1) }, fillMaxWidth, padding bottom=8.dp) {
    //         Column(padding=16.dp): Text "Article #${index + 1}" + Text "Tap to read..."
    //     }
    //   }
    Box {}
}

@Composable
private fun ArticleDetailScreen(
    articleId: Int,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // TODO: Implement ArticleDetailScreen
    // - Scaffold với TopAppBar("Article #$articleId", navigationIcon = back)
    // - Column: Text "📰 Article Detail" + Text về back stack preservation
    Box {}
}

// ─── Explore Tab Screens ──────────────────────────────────────────────────────

@Composable
private fun ExploreScreen(
    onCategoryClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    // TODO: Implement ExploreScreen
    // - Column với categories list (Technology, Sports, Art, Music, Travel)
    // - Mỗi category là ListItem với trailing arrow
    Box {}
}

@Composable
private fun CategoryScreen(
    categoryName: String,
    onItemClick: (Int) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // TODO: Implement CategoryScreen
    // - Scaffold với TopAppBar
    // - Column với 4 OutlinedButton items
    Box {}
}

@Composable
private fun ItemDetailScreen(
    itemId: Int,
    categoryName: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // TODO: Implement ItemDetailScreen
    // - Scaffold với TopAppBar
    // - Column với Text about back stack depth
    Box {}
}

// ─── Profile Tab Screens ──────────────────────────────────────────────────────

@Composable
private fun UserProfileScreen(
    onEditProfile: () -> Unit,
    onSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // TODO: Implement UserProfileScreen
    // - Column(fillMaxSize, padding=16.dp, CenterHorizontally)
    // - Avatar Surface + Spacer + Text name + Text email
    // - Spacer + Button "Edit Profile" + OutlinedButton "Settings"
    Box {}
}

@Composable
private fun EditProfileScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    // TODO: Implement EditProfileScreen
    Box {}
}

@Composable
private fun UserSettingsScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    // TODO: Implement UserSettingsScreen
    Box {}
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true, name = "Tab App Nav - Light")
@Composable
private fun TabAppNav3Preview() {
    AppTheme {
        TabAppNav3Screen()
    }
}

@Preview(
    showBackground = true,
    name = "Tab App Nav - Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun TabAppNav3DarkPreview() {
    AppTheme(darkTheme = true) {
        TabAppNav3Screen()
    }
}
