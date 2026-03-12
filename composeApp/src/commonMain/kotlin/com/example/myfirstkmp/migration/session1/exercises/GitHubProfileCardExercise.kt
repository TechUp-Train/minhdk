package com.example.myfirstkmp.migration.session1.exercises

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myfirstkmp.theme.AppTheme
import myfirstkmp.composeapp.generated.resources.Res
import myfirstkmp.composeapp.generated.resources.banner
import org.jetbrains.compose.resources.painterResource

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
    username = "Benjamin",
    displayName = "Doan Khac Minh",
    bio = "Too lazy to code, but crazy in problem solving",
    location = "Hà Nội, Việt Nam 🇻🇳",
    repoCount = 23,
    starCount = 46,
    followerCount = 69,
    languages = listOf("Kotlin", "Jav"),
    pinnedRepos = listOf(
        PinnedRepo("HaHaHaHaHa", "Online shoe shopping app", "Kotlin", 9),
        PinnedRepo("HiHiHiHiHi", "Revived pet app with Compose", "Kotlin", 23)
    )
)

// ─── Main Component ──────────────────────────────────────────────────────────

@Composable
fun GitHubProfileCard(
    profile: GitHubProfile,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        ProfileHeader(
            username = profile.username,
            displayName = profile.displayName,
            bio = profile.bio,
            location = profile.location
        )

        Spacer(Modifier.height(24.dp))

        ProfileStats(
            repoCount = profile.repoCount,
            starCount = profile.starCount,
            followerCount = profile.followerCount
        )

        Spacer(Modifier.height(32.dp))

        Text(
            "Skills",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFFBDBDBD)
        )

        Spacer(Modifier.height(12.dp))

        LanguageChips(profile.languages)

        Spacer(Modifier.height(32.dp))

        Text(
            "Pinned Projects",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFFBDBDBD)
        )

        Spacer(Modifier.height(16.dp))

        PinnedReposRow(profile.pinnedRepos)
    }
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
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {

        GradientAvatar(username, size = 110)

        Spacer(Modifier.height(12.dp))

        Text(
            displayName,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Text(
            "@${username.lowercase()}",
            fontSize = 16.sp,
            color = Color(0xFF7C4DFF)
        )

        Spacer(Modifier.height(12.dp))

        Text(
            bio,
            color = Color(0xFFB0B0B0),
            textAlign = TextAlign.Center,
            fontSize = 15.sp,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
    }
}

@Composable
fun GradientAvatar(
    username: String,
    size: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size.dp)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF7C4DFF),
                        Color(0xFF651FFF),
                        Color(0xFF00E5FF)
                    )
                ),
                shape = CircleShape
            )
            .padding(4.dp)
    ) {
        Image(
            painter = painterResource(Res.drawable.banner),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun ProfileStats(
    repoCount: Int,
    starCount: Int,
    followerCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF2A2342), RoundedCornerShape(16.dp))
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatItem(Icons.Default.Folder, repoCount, "Repos")
        StatItem(Icons.Default.Star, starCount, "Stars")
        StatItem(Icons.Default.Person, followerCount, "Followers")
    }
}

@Composable
fun StatItem(
    icon: ImageVector,
    count: Int,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            count.toString(),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            label.uppercase(),
            color = Color(0xFFBDBDBD),
            fontSize = 12.sp
        )
    }
}

@Composable
fun LanguageChips(
    languages: List<String>,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        languages.forEach { lang ->
            LanguageChip(lang)
        }
    }
}

@Composable
fun LanguageChip(
    language: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(Color(0xFF2C2C3A), RoundedCornerShape(20.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(language, color = Color(0xFFBDBDBD))
    }
}

@Composable
fun PinnedReposRow(
    repos: List<PinnedRepo>,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        repos.forEach {
            PinnedRepoCard(it, Modifier.weight(1f))
        }
    }
}
@Composable
fun PinnedRepoCard(
    repo: PinnedRepo,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(Color(0xFF2A2342), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text(repo.name, color = Color.White, fontWeight = FontWeight.SemiBold)

        Spacer(Modifier.height(8.dp))

        Text(
            repo.description,
            color = Color(0xFFBBBBBB),
            fontSize = 14.sp
        )

        Spacer(Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(repo.language, color = Color(0xFFFFB74D), fontSize = 13.sp)
            Text("★ ${repo.stars}", color = Color(0xFFBDBDBD))
        }
    }
}

// ─── Preview ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0XFF171022, name = "GitHub Profile — Portrait")
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
    name = "GitHub Profile — Dark Mode"
)
@Composable
private fun GitHubProfileDarkPreview() {
    AppTheme {
        GitHubProfileCard(profile = sampleProfile)
    }
}
