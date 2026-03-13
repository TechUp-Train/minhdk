package com.apero.composetraining.session1.exercises

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apero.composetraining.common.AppTheme

/**
 * ⭐⭐⭐⭐⭐ BONUS (Dành cho Nguyễn Quang Minh)
 *
 * GitHub Profile Card — Advanced Modifier & Composition
 *
 * Yêu cầu:
 * 1. Avatar với GRADIENT BORDER (Brush.linearGradient)
 * 2. Stats row: Repos / Stars / Followers — Divider giữa các stat
 * 3. Language chips: FlowRow layout (dùng FlowRow từ accompanist HOẶC
 *    tự implement wrap bằng Layout composable)
 * 4. Pinned repos: 2 cards EQUAL HEIGHT (IntrinsicSize.Max)
 *    + graphicsLayer scale effect khi "pressed" (mock với alpha)
 * 5. Tất cả component phải có modifier param
 * 6. 3 @Preview: Portrait / Large font / Dark mode
 *
 * Khái niệm nâng cao:
 * - Brush.linearGradient cho gradient border
 * - graphicsLayer { scaleX; scaleY; alpha } cho visual effects
 * - Custom Layout composable (nếu tự implement FlowRow)
 * - IntrinsicSize.Max + fillMaxHeight
 */

// ─── Data Models ──────────────────────────────────────────────────────────────

data class GitHubProfile(
    val username: String,
    val displayName: String,
    val bio: String,
    val location: String,
    val repoCount: Int,
    val starCount: Int,
    val followerCount: Int,
    val languages: List<String>,
    val pinnedRepos: List<PinnedRepo>
)

data class PinnedRepo(
    val name: String,
    val description: String,
    val language: String,
    val stars: Int
)

// ─── Sample Data ──────────────────────────────────────────────────────────────

val sampleProfile = GitHubProfile(
    username = "nqmgaming",
    displayName = "Nguyễn Quang Minh",
    bio = "Android & Flutter Developer · Open Source enthusiast",
    location = "Hà Nội, Việt Nam 🇻🇳",
    repoCount = 39,
    starCount = 47,
    followerCount = 12,
    languages = listOf("Kotlin", "Dart", "TypeScript", "Python", "Rust", "Go"),
    pinnedRepos = listOf(
        PinnedRepo("shose-shop", "Online shoe shopping app", "Kotlin", 9),
        PinnedRepo("ANeko-Reborn", "Revived pet app with Compose", "Kotlin", 23)
    )
)

// ─── Main Component ──────────────────────────────────────────────────────────

@Composable
fun GitHubProfileCard(
    profile: GitHubProfile,
    modifier: Modifier = Modifier
) {
    // TODO: Implement GitHubProfileCard
    // - Card với fillMaxWidth, padding(16.dp), RoundedCornerShape(16.dp)
    // - Column bên trong với padding(20.dp)
    // - Gọi ProfileHeader (username, displayName, bio, location)
    // - Spacer + HorizontalDivider + Spacer
    // - Gọi ProfileStats (repoCount, starCount, followerCount)
    // - Spacer + HorizontalDivider + Spacer
    // - Text "Languages" label + LanguageChips(languages)
    // - Nếu pinnedRepos không rỗng: HorizontalDivider + Text "Pinned" + PinnedReposRow
    Box {}
}

// ─── Sub-components ──────────────────────────────────────────────────────────

@Composable
fun ProfileHeader(
    username: String,
    displayName: String,
    bio: String,
    location: String,
    modifier: Modifier = Modifier
) {
    // TODO: Implement ProfileHeader
    // - Row với verticalAlignment = CenterVertically
    // - GradientAvatar(username, size = 72) bên trái
    // - Spacer(16.dp) rồi Column (weight(1f)):
    //   → Text displayName (titleLarge, Bold)
    //   → Text "@$username" (primary color)
    //   → Text bio (bodySmall, onSurfaceVariant, maxLines=2, Ellipsis)
    //   → Text "📍 $location" (bodySmall, onSurfaceVariant)
    Box {}
}

/**
 * Avatar với GRADIENT BORDER — advanced Modifier trick
 *
 * TODO: [Nâng cao] Implement gradient border thay vì solid border
 *
 * Cách implement gradient border:
 * Box(modifier = Modifier
 *     .size(size.dp + 4.dp)       // Lớn hơn 4dp để tạo border
 *     .clip(CircleShape)
 *     .background(
 *         Brush.linearGradient(
 *             colors = listOf(Color(0xFF833AB4), Color(0xFFFD1D1D), Color(0xFFFCB045))
 *         )
 *     )
 * ) {
 *     Box(modifier = Modifier.size(size.dp).clip(CircleShape).align(Alignment.Center)) {
 *         // Avatar content
 *     }
 * }
 */
@Composable
fun GradientAvatar(
    username: String,
    size: Int,
    modifier: Modifier = Modifier
) {
    // TODO: Implement GradientAvatar với gradient border
    // - Box ngoài: size = (size+4).dp, clip(CircleShape),
    //   background(Brush.linearGradient) với màu Instagram-style purple→red→orange
    //   GỢI Ý: colors = listOf(Color(0xFF833AB4), Color(0xFFFD1D1D), Color(0xFFFCB045))
    // - Box trong: size = size.dp, clip(CircleShape), background(primary),
    //   align(Alignment.Center)
    // - Text chữ cái đầu username (fontSize = size/2.5, Bold, onPrimary)
    Box {}
}

@Composable
fun ProfileStats(
    repoCount: Int,
    starCount: Int,
    followerCount: Int,
    modifier: Modifier = Modifier
) {
    // TODO: Implement ProfileStats
    // - Row với fillMaxWidth, SpaceEvenly, verticalAlignment = CenterVertically
    // - 3 StatItem: FolderOpen+repoCount+"repos", Star+starCount+"stars", People+followerCount+"followers"
    // - VerticalDivider(height=32.dp) giữa mỗi stat
    Box {}
}

@Composable
fun StatItem(
    icon: ImageVector,
    count: Int,
    label: String,
    modifier: Modifier = Modifier
) {
    // TODO: Implement StatItem
    // - Column với horizontalAlignment = CenterHorizontally
    // - Row: Icon(16.dp, primary) + Text count (titleMedium, Bold)
    // - Text label (bodySmall, onSurfaceVariant)
    Box {}
}

/**
 * Language chips — Wrap layout
 *
 * TODO: [Nâng cao] Implement FlowRow để chips wrap tự động
 *
 * Option 1 (Easy): Dùng accompanist FlowRow
 * Option 2 (Hard): Tự implement với Layout composable
 *
 * Tạm thời: chia 2 Row (take(3) + drop(3))
 */
@Composable
fun LanguageChips(
    languages: List<String>,
    modifier: Modifier = Modifier
) {
    // TODO: Implement LanguageChips
    // - Column với spacedBy(6.dp)
    // - Row 1: languages.take(3) → mỗi item gọi LanguageChip(lang)
    // - Row 2: languages.drop(3) nếu có → tương tự
    // GỢI Ý NÂNG CAO: Dùng FlowRow để auto-wrap thay vì chia cứng
    Box {}
}

@Composable
fun LanguageChip(
    language: String,
    modifier: Modifier = Modifier
) {
    // TODO: Implement LanguageChip
    // - Surface với RoundedCornerShape(50) (pill shape), secondaryContainer color
    // - Text bên trong với padding horizontal=10, vertical=4
    // - labelSmall, onSecondaryContainer, FontWeight.Medium
    Box {}
}

/**
 * Pinned repos — EQUAL HEIGHT với IntrinsicSize.Max
 *
 * TODO: [Nâng cao] Thêm graphicsLayer scale effect khi pressed
 *
 * Cách implement hover/press effect với graphicsLayer:
 * var isPressed by remember { mutableStateOf(false) }
 * Modifier.graphicsLayer {
 *     scaleX = if (isPressed) 0.97f else 1f
 *     scaleY = if (isPressed) 0.97f else 1f
 * }
 * Note: State cần Buổi 3 — hôm nay mock với alpha thay đổi tĩnh
 */
@Composable
fun PinnedReposRow(
    repos: List<PinnedRepo>,
    modifier: Modifier = Modifier
) {
    // TODO: Implement PinnedReposRow với EQUAL HEIGHT
    // - Row với fillMaxWidth + height(IntrinsicSize.Max), spacedBy(12.dp)
    // - Với mỗi repo: PinnedRepoCard(repo, Modifier.weight(1f).fillMaxHeight())
    // GỢI Ý: Thiếu fillMaxHeight() trên card → height không đều dù có IntrinsicSize.Max
    Box {}
}

@Composable
fun PinnedRepoCard(
    repo: PinnedRepo,
    modifier: Modifier = Modifier
) {
    // TODO: Implement PinnedRepoCard
    // - Card với clickable, RoundedCornerShape(8.dp), surfaceVariant color
    // - NÂNG CAO: Thêm graphicsLayer { scaleX, scaleY } cho visual feedback
    // - Column bên trong với fillMaxHeight, padding(12.dp), SpaceBetween
    // - Header: Text repo.name (titleSmall, Bold, maxLines=1) + Text description (bodySmall, 2 dòng)
    // - Footer Row: dot language + Text language + Spacer(weight(1f)) + Star icon + stars count
    Box {}
}

// ─── Preview ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true, name = "GitHub Profile — Portrait")
@Composable
private fun GitHubProfilePortraitPreview() {
    AppTheme {
        GitHubProfileCard(profile = sampleProfile)
    }
}

@Preview(
    showBackground = true,
    name = "GitHub Profile — Large Font",
    fontScale = 1.5f
)
@Composable
private fun GitHubProfileLargeFontPreview() {
    AppTheme {
        GitHubProfileCard(profile = sampleProfile)
    }
}

@Preview(
    showBackground = true,
    name = "GitHub Profile — Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun GitHubProfileDarkPreview() {
    AppTheme {
        GitHubProfileCard(profile = sampleProfile)
    }
}
