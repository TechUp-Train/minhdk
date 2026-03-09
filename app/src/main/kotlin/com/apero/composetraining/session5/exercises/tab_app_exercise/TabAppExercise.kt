package com.apero.composetraining.session5.exercises.tab_app_exercise

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.apero.composetraining.common.AppTheme
import com.apero.composetraining.session5.exercises.tab_app_exercise.home.ArticleDetailScreen
import com.apero.composetraining.session5.exercises.tab_app_exercise.home.HomeScreen
import kotlinx.serialization.Serializable

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


// pre-defines
private val primaryColor = Color(0XFF0062A3)
private val containerColor = primaryColor.copy(alpha = 0.2f)

//----------------------------------------Entries----------------------------------------
// Home flows

@Serializable
private data object Homee

@Serializable
private data class ArticleDetail(
    val id: Int
)

@Serializable
private data object Explore

@Serializable
private data object SearchResult

@Serializable
private data object Profile

@Serializable
private data object EditProfile

private data class NavbarItem(
    val position: Int,
    val label: String,
    val icon: ImageVector
)

private val navbarItems = listOf(
    NavbarItem(0, "Home", Icons.Default.Home),
    NavbarItem(1, "Explore", Icons.Default.Explore),
    NavbarItem(2, "Profile", Icons.Default.AccountCircle)
)

@Composable
private fun ExploreScreen() {

}

@Composable
private fun ResultScreen() {

}

@Composable
private fun ProfileScreen() {

}

@Composable
private fun EditProfileScreen() {

}

@Composable
private fun RowScope.BottomBarItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    NavigationBarItem(
        selected = isSelected,
        onClick = onClick,
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(25.dp)
            )
        },
        label = { Text(label) },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = primaryColor,
            selectedTextColor = primaryColor,
            unselectedIconColor = containerColor,
            unselectedTextColor = containerColor,
            indicatorColor = Color.Transparent
        ),
        modifier = Modifier
            .padding(horizontal = 10.dp, 5.dp)
            .background(
                color = if (isSelected) containerColor else Color.Transparent,
                shape = RoundedCornerShape(15.dp)
            )
    )
}

@Composable
private fun BottomBar(
    modifier: Modifier = Modifier,
    isSelected: (Int) -> Boolean,
    onItemSelected: (Int) -> Unit
) {
    NavigationBar(
        modifier = modifier
    ) {
        navbarItems.forEach { item ->
            BottomBarItem(
                icon = item.icon,
                label = item.label,
                isSelected = isSelected(item.position)
            ) {
                onItemSelected(item.position)
            }
        }

    }
}

@Composable
private fun SetupNavBar(
    contentPadding: PaddingValues = PaddingValues(0.dp),
    curBackStack: SnapshotStateList<Any>,
    onPressBack: () -> Unit
) {

    BackHandler {
        onPressBack()
    }

    NavDisplay(
        backStack = curBackStack,
        entryProvider = entryProvider {

            entry<Homee> {
                HomeScreen(contentPadding) {
                    curBackStack.add(ArticleDetail(it))
                }
            }

            entry<ArticleDetail> {
                curBackStack.lastOrNull()?.let { curEntry ->
                    (curEntry as? ArticleDetail)?.let {
                        ArticleDetailScreen(contentPadding, it.id) {
                            curBackStack.removeLastOrNull()
                        }
                    }
                }
            }

            entry<Explore> {
                ExploreScreen()
            }

            entry<SearchResult> {
                ResultScreen()
            }

            entry<Profile> {
                ProfileScreen()
            }

            entry<EditProfile> {
                EditProfileScreen()
            }
        }
    )
}

@SuppressLint("MutableCollectionMutableState")
@Composable
fun TabAppScreen() {

    val homeEntries = remember { mutableStateListOf<Any>(Homee) }
    val exploreEntries = remember { mutableStateListOf<Any>(Explore) }
    val profileEntries = remember { mutableStateListOf<Any>(Profile) }

    var curBackStack by remember { mutableStateOf(homeEntries) }

    val context = LocalContext.current

    Scaffold(
        bottomBar = {
            BottomBar(
                isSelected = checkSelected@{ pos ->
                    val firstEntry = when (pos) {
                        0 -> Homee
                        1 -> Explore
                        2 -> Profile
                        else -> throw Exception()
                    }
                    return@checkSelected curBackStack.contains(firstEntry)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(containerColor.copy(alpha = 0.5f))
            ) { pos ->
                curBackStack = when (pos) {
                    0 -> homeEntries
                    1 -> exploreEntries
                    2 -> profileEntries
                    else -> throw Exception()
                }
            }
        }
    ) { contentPadding ->

        SetupNavBar(contentPadding = contentPadding, curBackStack = curBackStack) pressBack@{
            // navigate up in normal case
            if (curBackStack.size > 1) {
                curBackStack.removeLastOrNull()
                return@pressBack
            }
            // back to home flow if down to first screen of the current
            // stack which is not homeEntries
            if (curBackStack != homeEntries) {
                curBackStack = homeEntries
                return@pressBack
            }
            // take user to home if th cur stack is home
            context.startActivity(Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_HOME)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            })

        }


    }
}

@Preview(
    name = "Light Theme",
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
private fun TabAppScreenPreview() {
    AppTheme(darkTheme = false) { TabAppScreen() }
}
