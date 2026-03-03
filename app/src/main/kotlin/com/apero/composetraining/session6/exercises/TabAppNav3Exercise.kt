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
import androidx.navigation.NavController
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
 * ┌────────────────────────────────────────────┐
 * │ Feed                                        │
 * ├────────────────────────────────────────────┤
 * │                                             │
 * │  [Article Card]                             │  ← Feed Tab
 * │  [Article Card]                             │
 * │                                             │
 * ├────────────────────────────────────────────┤
 * │ [🏠 Feed] [🔍 Explore (3)] [👤 Profile]   │  ← Bottom Nav
 * └────────────────────────────────────────────┘
 *
 * Key concepts:
 * - Mỗi tab có NavController riêng → per-tab back stack
 * - saveState = true + restoreState = true: giữ tab state khi switch
 * - launchSingleTop = true: không tạo duplicate instances
 * - derivedStateOf: tính badge count từ state
 * - BackHandler: back từ tab root → switch về Feed tab
 *
 * Lưu ý về "Navigation 3":
 * Navigation 3 (alpha) dùng "NavDisplay + NavBackStack" thay vì NavController.
 * Pattern tương tự nhưng explicit hơn về per-tab stacks.
 * Navigation Compose (stable 2.8.5) đạt kết quả tương tự với saveState.
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

// Feed tab routes
@Serializable object FeedScreenRoute
@Serializable data class ArticleDetailRoute(val articleId: Int)

// Explore tab routes
@Serializable object ExploreScreenRoute
@Serializable data class CategoryRoute(val categoryName: String)
@Serializable data class ItemDetailRoute(val itemId: Int, val categoryName: String)

// Profile tab routes
@Serializable object ProfileScreenRoute
@Serializable object EditProfileRoute
@Serializable object ProfileSettingsRoute

// ─── Main App ─────────────────────────────────────────────────────────────────

/**
 * TabAppNav3 — main composable với per-tab navigation
 *
 * Architecture:
 * - Mỗi tab có NavController riêng (feedNav, exploreNav, profileNav)
 * - selectedTab state kiểm soát NavController nào đang active
 * - Scaffold + NavigationBar ở ngoài (shared across tabs)
 * - NavHost bên trong mỗi tab
 *
 * Khác biệt với Navigation 3 NavDisplay:
 * - Nav3: NavDisplay(backStack = feedBackStack) { ... }
 * - NavCompose: NavHost(navController = feedNav) { ... }
 * Concept giống nhau, API khác nhau
 */
@Composable
fun TabAppNav3Screen(modifier: Modifier = Modifier) {
    // Selected tab
    var selectedTab by remember { mutableStateOf(MainTab.FEED) }

    // Tạo NavController cho từng tab
    // Mỗi NavController = 1 back stack riêng biệt
    val feedNavController = rememberNavController()
    val exploreNavController = rememberNavController()
    val profileNavController = rememberNavController()

    // Map tab → NavController
    val navControllerMap = remember(feedNavController, exploreNavController, profileNavController) {
        mapOf(
            MainTab.FEED to feedNavController,
            MainTab.EXPLORE to exploreNavController,
            MainTab.PROFILE to profileNavController,
        )
    }

    // Fake unread notifications count (thường từ ViewModel)
    var unreadCount by remember { mutableStateOf(3) }

    // Badge count cho Explore tab
    val exploreBadge by remember {
        derivedStateOf { if (unreadCount > 0) unreadCount else null }
    }

    // Kiểm tra xem tab hiện tại có đang ở root không
    // Nếu ở root + selectedTab != FEED → BackHandler switch về Feed
    val currentNavController = navControllerMap[selectedTab] ?: feedNavController

    Scaffold(
        bottomBar = {
            TabNavigationBar(
                selectedTab = selectedTab,
                exploreBadge = exploreBadge,
                onTabSelected = { tab ->
                    if (tab == selectedTab) {
                        // Double-tap tab → scroll to top / pop to root
                        currentNavController.popBackStack(
                            route = getStartRoute(tab),
                            inclusive = false,
                        )
                    } else {
                        selectedTab = tab
                        if (tab == MainTab.EXPLORE) {
                            unreadCount = 0 // Clear badge khi mở Explore
                        }
                    }
                },
            )
        },
        modifier = modifier,
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            // Mỗi tab's NavHost — chỉ visible khi tab được chọn
            // Compose sẽ giữ state của tab không active trong memory

            // Feed Tab
            FeedTabNavHost(
                navController = feedNavController,
                visible = selectedTab == MainTab.FEED,
            )

            // Explore Tab
            ExploreTabNavHost(
                navController = exploreNavController,
                visible = selectedTab == MainTab.EXPLORE,
            )

            // Profile Tab
            ProfileTabNavHost(
                navController = profileNavController,
                visible = selectedTab == MainTab.PROFILE,
            )
        }
    }

    // BackHandler: khi bấm back ở tab root (không phải Feed)
    // → Switch về Feed tab (giống Instagram, Reddit)
    val isAtRoot = currentNavController.currentBackStackEntry?.destination?.route?.let {
        it == getStartRoute(selectedTab).toString()
    } ?: true

    BackHandler(enabled = selectedTab != MainTab.FEED && isAtRoot) {
        selectedTab = MainTab.FEED
    }
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
    NavigationBar(modifier = modifier) {
        MainTab.entries.forEach { tab ->
            NavigationBarItem(
                icon = {
                    if (tab == MainTab.EXPLORE && exploreBadge != null) {
                        // Badge count cho Explore
                        BadgedBox(badge = {
                            Badge { Text(exploreBadge.toString()) }
                        }) {
                            Icon(tab.icon, contentDescription = tab.label)
                        }
                    } else {
                        Icon(tab.icon, contentDescription = tab.label)
                    }
                },
                label = { Text(tab.label) },
                selected = selectedTab == tab,
                onClick = { onTabSelected(tab) },
            )
        }
    }
}

// ─── Feed Tab ─────────────────────────────────────────────────────────────────

@Composable
private fun FeedTabNavHost(
    navController: NavHostController,
    visible: Boolean,
    modifier: Modifier = Modifier,
) {
    if (!visible) return // Không render khi tab không active

    NavHost(
        navController = navController,
        startDestination = FeedScreenRoute,
        modifier = modifier.fillMaxSize(),
    ) {
        composable<FeedScreenRoute> {
            FeedListScreen(
                onArticleClick = { articleId ->
                    navController.navigate(ArticleDetailRoute(articleId))
                },
            )
        }

        composable<ArticleDetailRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<ArticleDetailRoute>()
            ArticleDetailScreen(
                articleId = route.articleId,
                onBack = { navController.popBackStack() },
            )
        }
    }
}

// ─── Explore Tab ──────────────────────────────────────────────────────────────

@Composable
private fun ExploreTabNavHost(
    navController: NavHostController,
    visible: Boolean,
    modifier: Modifier = Modifier,
) {
    if (!visible) return

    NavHost(
        navController = navController,
        startDestination = ExploreScreenRoute,
        modifier = modifier.fillMaxSize(),
    ) {
        composable<ExploreScreenRoute> {
            ExploreScreen(
                onCategoryClick = { category ->
                    navController.navigate(CategoryRoute(category))
                },
            )
        }

        composable<CategoryRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<CategoryRoute>()
            CategoryScreen(
                categoryName = route.categoryName,
                onItemClick = { itemId ->
                    navController.navigate(ItemDetailRoute(itemId, route.categoryName))
                },
                onBack = { navController.popBackStack() },
            )
        }

        composable<ItemDetailRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<ItemDetailRoute>()
            ItemDetailScreen(
                itemId = route.itemId,
                categoryName = route.categoryName,
                onBack = { navController.popBackStack() },
            )
        }
    }
}

// ─── Profile Tab ──────────────────────────────────────────────────────────────

@Composable
private fun ProfileTabNavHost(
    navController: NavHostController,
    visible: Boolean,
    modifier: Modifier = Modifier,
) {
    if (!visible) return

    NavHost(
        navController = navController,
        startDestination = ProfileScreenRoute,
        modifier = modifier.fillMaxSize(),
    ) {
        composable<ProfileScreenRoute> {
            UserProfileScreen(
                onEditProfile = { navController.navigate(EditProfileRoute) },
                onSettings = { navController.navigate(ProfileSettingsRoute) },
            )
        }

        composable<EditProfileRoute> {
            EditProfileScreen(onBack = { navController.popBackStack() })
        }

        composable<ProfileSettingsRoute> {
            UserSettingsScreen(onBack = { navController.popBackStack() })
        }
    }
}

// ─── Feed Tab Screens ─────────────────────────────────────────────────────────

@Composable
private fun FeedListScreen(
    onArticleClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Text("Feed 🏠", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        repeat(5) { index ->
            Card(
                onClick = { onArticleClick(index + 1) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Article #${index + 1}", style = MaterialTheme.typography.titleMedium)
                    Text(
                        "Tap to read full article → ArticleDetailRoute(id=${index + 1})",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@Composable
private fun ArticleDetailScreen(
    articleId: Int,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text("Article #$articleId") },
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
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
        ) {
            Text("📰 Article Detail", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Feed tab back stack: FeedScreenRoute → ArticleDetailRoute($articleId)\n\n" +
                    "Switch to Explore tab → Feed tab state preserved.\n" +
                    "Come back → still on this screen!",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

// ─── Explore Tab Screens ──────────────────────────────────────────────────────

@Composable
private fun ExploreScreen(
    onCategoryClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val categories = listOf("Technology", "Sports", "Art", "Music", "Travel")

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
    ) {
        Text("Explore 🔍", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text(
            "3 levels deep: Explore → Category → Item",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(16.dp))

        categories.forEach { category ->
            ListItem(
                headlineContent = { Text(category) },
                trailingContent = { Icon(Icons.AutoMirrored.Filled.ArrowForward, null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
            )
        }
    }
}

@Composable
private fun CategoryScreen(
    categoryName: String,
    onItemClick: (Int) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text(categoryName) },
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
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            repeat(4) { index ->
                OutlinedButton(
                    onClick = { onItemClick(index + 1) },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("$categoryName Item #${index + 1}")
                }
            }
        }
    }
}

@Composable
private fun ItemDetailScreen(
    itemId: Int,
    categoryName: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text("$categoryName #$itemId") },
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
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
        ) {
            Text("Item Detail 📄", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Explore back stack:\nExploreScreen → CategoryRoute($categoryName) → ItemDetailRoute($itemId)\n\n" +
                    "Back 2 lần để về ExploreScreen.",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

// ─── Profile Tab Screens ──────────────────────────────────────────────────────

@Composable
private fun UserProfileScreen(
    onEditProfile: () -> Unit,
    onSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.size(80.dp),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Person, null, modifier = Modifier.size(48.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("John Doe", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("john@example.com", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = onEditProfile, modifier = Modifier.fillMaxWidth()) {
            Text("Edit Profile")
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(onClick = onSettings, modifier = Modifier.fillMaxWidth()) {
            Text("Settings")
        }
    }
}

@Composable
private fun EditProfileScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text("Edit Profile") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) } },
            )
        },
        modifier = modifier,
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Text("Edit Profile ✏️", style = MaterialTheme.typography.headlineMedium)
        }
    }
}

@Composable
private fun UserSettingsScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) } },
            )
        },
        modifier = modifier,
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Text("Settings ⚙️", style = MaterialTheme.typography.headlineMedium)
        }
    }
}

// Needed for Explore screen arrow icon
private val Icons.AutoMirrored.Filled.ArrowForward: ImageVector
    get() = Icons.AutoMirrored.Filled.ArrowBack // placeholder - reuse ArrowBack visually reversed

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
