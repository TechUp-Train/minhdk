package com.apero.composetraining.session2.exercises

import android.R.color.black
import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apero.composetraining.R
import com.apero.composetraining.common.AppTheme
import okhttp3.internal.immutableListOf
import kotlin.String

/**
 * ⭐⭐⭐⭐⭐ BONUS NÂNG CAO: Streaming App Home (Nested Scroll — Concept 5) (Netflix-style)
 *
 * Mô tả: Build Home screen với nested scrolling đúng cách
 *
 * ┌──────────────────────────────────┐
 * │  🎬 StreamApp                🔍  │  ← TopBar
 * │                                  │
 * │  ┌────────────────────────────┐  │  ← Hero Banner (Box overlay)
 * │  │      FEATURED MOVIE        │  │
 * │  │      Inception             │  │
 * │  │      [▶ Watch] [+ List]   │  │
 * │  └────────────────────────────┘  │
 * │                                  │
 * │  Trending Now                    │  ← Section title
 * │  [  ] [  ] [  ] [  ] →         │  ← Horizontal scroll
 * │                                  │
 * │  Continue Watching               │
 * │  [  ] [  ] [  ] →              │
 * └──────────────────────────────────┘  ↕ Vertical scroll (outer)
 *
 * Yêu cầu — NESTED SCROLLING ĐÚNG CÁCH:
 * ❌ SAI: Column(verticalScroll) + nhiều LazyColumn/LazyRow
 * ✅ ĐÚNG: Column(verticalScroll) + Row(horizontalScroll) bên trong
 *   Hoặc: LazyColumn với item { Row(horizontalScroll) }
 *
 * Yêu cầu kỹ thuật:
 * 1. Hero Banner: Box với overlay gradient (background dark gradient overlay)
 * 2. Category chips: Row với horizontalScroll (nhẹ hơn LazyRow cho < 10 items)
 * 3. Movie row: Row(horizontalScroll) — không phải LazyRow (để tránh nested Lazy crash)
 * 4. Outer scroll: Column(verticalScroll) — chứa tất cả sections
 * 5. Arrangement.spacedBy cho movie row
 * 6. contentPadding bằng padding trên Column, không phải từng item
 *
 * Khái niệm Buổi 2:
 * - Nested scroll giải pháp: Column(verticalScroll) + Row(horizontalScroll)
 * - Box + overlay = gradient trên image
 * - Arrangement.spacedBy vs Arrangement.SpaceBetween
 * - Modifier.horizontalScroll() vs LazyRow
 */

// ─── Data Models ──────────────────────────────────────────────────────────────

data class Movie(
    val title: String,
    val genre: String,
    val emoji: String,
    val rating: String,
    val color: Color = Color(0xFF1E1E2E)
)

data class MovieSection(
    val title: String,
    val movies: List<Movie>
)

// ─── Sample Data ──────────────────────────────────────────────────────────────

private val featuredMovie = Movie(
    title = "Inception",
    genre = "Sci-Fi · Thriller",
    emoji = "🌀",
    rating = "8.8",
    color = Color(0xFF0D1117)
)

val movieSections = listOf(
    MovieSection(
        title = "🔥 Trending Now",
        movies = listOf(
            Movie("The Matrix", "Sci-Fi", "💊", "8.7", Color(0xFF0D2818)),
            Movie("Dune", "Epic", "🏜️", "8.0", Color(0xFF2D1B00)),
            Movie("Interstellar", "Space", "🌌", "8.6", Color(0xFF000D1A)),
            Movie("Blade Runner", "Neo-noir", "🤖", "8.1", Color(0xFF1A0000)),
            Movie("Tenet", "Action", "⏰", "7.4", Color(0xFF001A2D))
        )
    ),
    MovieSection(
        title = "▶ Continue Watching",
        movies = listOf(
            Movie("Pulp Fiction", "Crime", "🎬", "8.9", Color(0xFF1A0D00)),
            Movie("Dark Knight", "Action", "🦇", "9.0", Color(0xFF0D0D0D)),
            Movie("Parasite", "Thriller", "🏠", "8.5", Color(0xFF0D1A0D))
        )
    ),
    MovieSection(
        title = "🎭 Because you watched Inception",
        movies = listOf(
            Movie("Shutter Island", "Mystery", "🏝️", "8.1", Color(0xFF0D1A2D)),
            Movie("Prestige", "Drama", "🎩", "8.5", Color(0xFF1A1A0D)),
            Movie("Memento", "Thriller", "📸", "8.4", Color(0xFF2D0D0D)),
            Movie("Fight Club", "Drama", "🥊", "8.8", Color(0xFF1A0D1A))
        )
    )
)

private val categories = listOf("All", "Movies", "Series", "Anime", "Documentary", "Kids")

private data class NavBarItem(
    val label: String,
    val icon: ImageVector
)

private val navBarItems = listOf(
    NavBarItem("Home", Icons.Default.Home),
    NavBarItem("Search", Icons.Default.Search),
    NavBarItem("Library", Icons.Default.LocalLibrary),
    NavBarItem("Download", Icons.Default.Download),
    NavBarItem("Profile", Icons.Default.AccountCircle)
)

// ─── Main Screen ──────────────────────────────────────────────────────────────

/**
 * Streaming Home Screen
 *
 * TODO: [Buổi 2 — Nested Scroll] Cấu trúc chính xác:
 *
 * Column(                                     ← Outer vertical scroll
 *     modifier = Modifier.verticalScroll(...)
 * ) {
 *     HeroBanner(...)                         ← Box với gradient overlay
 *     CategoryChipRow(...)                    ← Row với horizontalScroll
 *
 *     movieSections.forEach { section ->
 *         SectionTitle(...)
 *         MovieRow(section.movies)            ← Row với horizontalScroll (KHÔNG phải LazyRow!)
 *     }
 * }
 *
 * ⚠️ WARNING: Đừng dùng LazyRow bên trong Column(verticalScroll)!
 * → LazyRow cần unbounded width constraint
 * → Column(verticalScroll) gives unbounded height
 * → Nested unbounded = CRASH
 *
 * Dùng Row(horizontalScroll) thay thế cho danh sách nhỏ (< 20 items)
 */

val TextHeader34 = TextStyle(
    fontSize = 34.sp,
    fontWeight = FontWeight.Bold,
    letterSpacing = 1.5.sp
)

val TextHeader20 = TextStyle(
    fontSize = 20.sp,
    fontWeight = FontWeight.Bold,
    letterSpacing = 1.5.sp
)

val TextNormal16 = TextStyle(
    fontSize = 16.sp,
    fontWeight = FontWeight.Normal
)

val TextNormal14 = TextStyle(
    fontSize = 16.sp,
    fontWeight = FontWeight.Normal
)

@Composable
fun StreamingHomeScreen(modifier: Modifier = Modifier) {

    var selectedCat by remember { mutableStateOf(categories.first()) }

    Scaffold(
        bottomBar = {
            BottomBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            )
        }
    ) { contentPadding ->
        LazyColumn(
            contentPadding = contentPadding,
            modifier = modifier
                .fillMaxSize()
                .background(color = Color.Black)
        ) {

            item {
                HeroBanner(
                    movie = featuredMovie,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                )
            }

            item {
                CategoryChipRow(
                    categories = categories,
                    selectedCategory = selectedCat
                ) { selectedCat = it }
            }

            item {
                movieSections.forEach {
                    MovieSection(
                        section = it,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp)
                    )
                }
            }

        }
    }
}

// ─── Sub-components ──────────────────────────────────────────────────────────

@Composable
private fun BannerBackground(
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(R.drawable.banner),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
    )
}

@Preview
@Composable
private fun BannerAction(
    modifier: Modifier = Modifier
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {

        Button(
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors()
                .copy(containerColor = Color.White),
            onClick = {},
            modifier = Modifier.wrapContentSize()
        ) {
            Text(
                text = "Play",
                color = Color.Black,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = "My List",
            style = TextHeader20,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

    }

}

@Preview(showBackground = true)
@Composable
fun BannerInfo(
    modifier: Modifier = Modifier,
    name: String = "Doan Khac Minh",
    category: String = "Sci-fi - Thriller",
    time: String = "02:30:01",
) {
    Column(
        modifier = modifier
    ) {

        Text(
            text = name,
            style = TextHeader34,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = category,
                style = TextNormal16,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.width(20.dp))

            Text(
                text = "Duration: $time",
                style = TextNormal16,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        BannerAction()
    }
}

@Composable
fun HeroBanner(
    movie: Movie,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        BannerBackground(modifier = Modifier.fillMaxSize())

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black
                        )
                    )
                )
        )

        BannerInfo(
            name = movie.title,
            category = movie.genre,
            time = "02:30:01",
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(12.dp)
        )
    }
}

@Composable
fun CategoryChipRow(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelect: (String) -> Unit
) {

    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        categories.forEach { category ->

            val isSelected = category == selectedCategory

            FilterChip(
                selected = isSelected,
                onClick = { onCategorySelect(category) },
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(2.dp, Color.White),
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = Color.Transparent,
                    selectedContainerColor = Color.White
                ),
                modifier = Modifier.padding(5.dp),
                label = {
                    Text(
                        text = category,
                        style = TextNormal14,
                        color = if (!isSelected) Color.White else Color.Black
                    )
                }
            )
        }
    }
}

@Preview
@Composable
fun MovieSection(
    modifier: Modifier = Modifier,
    section: MovieSection = movieSections.first()
) {
    Column(modifier = modifier) {

        Text(
            text = section.title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            section.movies.forEach { movie ->
                MovieCard(movie = movie)
            }
        }
    }
}

@Preview
@Composable
fun MovieCard(
    modifier: Modifier = Modifier,
    movie: Movie = featuredMovie
) {
    Box(
        modifier = modifier
            .width(120.dp)
            .height(160.dp)
            .clip(RoundedCornerShape(8.dp))
    ) {

        Image(
            painter = painterResource(id = R.drawable.banner),
            contentDescription = movie.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.8f)
                        )
                    )
                )
        ) {

            Column(
                modifier = Modifier.padding(8.dp)
            ) {

                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "⭐ ${movie.rating}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun BottomBar(modifier: Modifier = Modifier) {
    NavigationBar(
        containerColor = Color.Black.copy(alpha = 0.7f),
        modifier = modifier
    ) {
        navBarItems.forEach {
            BottomBarItem(
                image = it.icon,
                label = it.label,
                isSelected = true
            )
        }
    }
}

@Composable
private fun RowScope.BottomBarItem(
    image: ImageVector,
    label: String,
    isSelected: Boolean
) {
    NavigationBarItem(
        selected = isSelected,
        onClick = {},
        icon = {
            Icon(
                imageVector = image,
                contentDescription = null,
                modifier = Modifier.size(25.dp)
            )
        },
        label = { Text(label) },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = Color.White,
            selectedTextColor = Color.White,
            unselectedIconColor = Color.White.copy(alpha = 0.5f),
            unselectedTextColor = Color.White.copy(alpha = 0.5f),
            indicatorColor = Color.Transparent
        )
    )
}


// ─── Preview ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true, name = "Streaming Home — Dark")
@Composable
private fun StreamingHomePreview() {
    AppTheme {
        StreamingHomeScreen()
    }
}

@Preview(
    showBackground = true,
    name = "Hero Banner Only",
    heightDp = 320
)
@Composable
private fun HeroBannerPreview() {
    AppTheme {
        HeroBanner(movie = featuredMovie)
    }
}

// ─── Câu Hỏi Thảo Luận ───────────────────────────────────────────────────────
/*
 * Q1: Tại sao Row(horizontalScroll) thay vì LazyRow bên trong Column(verticalScroll)?
 * Q2: Khi nào dùng Row(horizontalScroll) vs LazyRow?
 * Q3: Gradient overlay trên HeroBanner — tại sao cần 2 Box thay vì 1?
 * Q4: contentPadding vs padding — khác gì?
 */
