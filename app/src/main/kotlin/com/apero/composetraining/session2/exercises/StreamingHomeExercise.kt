package com.apero.composetraining.session2.exercises

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apero.composetraining.common.AppTheme

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

private val movieSections = listOf(
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
@Composable
fun StreamingHomeScreen(modifier: Modifier = Modifier) {
    // TODO: Implement StreamingHomeScreen
    // - rememberScrollState() cho outer scroll
    // - var selectedCategory by remember { mutableStateOf("All") }
    // - Column với fillMaxSize + verticalScroll(scrollState) + background(Color(0xFF0D0D0D))
    //   → HeroBanner(featuredMovie)
    //   → Spacer(16.dp)
    //   → CategoryChipRow(categories, selectedCategory, onCategorySelect)
    //   → Spacer(24.dp)
    //   → movieSections.forEach { section → MovieSection(section) + Spacer(24.dp) }
    //   → Spacer(80.dp) ← space cho FAB
    Box {}
}

// ─── Sub-components ──────────────────────────────────────────────────────────

/**
 * Hero Banner — Box với gradient text overlay
 *
 * TODO: [Buổi 2 — Box] Cấu trúc:
 * Box(fillMaxWidth, height=280.dp) {
 *     Box(fillMaxSize, background = movie.color) {   ← Background poster
 *         Text emoji (80.sp, Alignment.Center)
 *     }
 *     Box(fillMaxSize, background = Brush.verticalGradient) ← Dark overlay
 *     Column(Alignment.BottomStart, padding=16.dp) { ← Content
 *         Text title + Text genre + Row buttons
 *     }
 * }
 */
@Composable
fun HeroBanner(
    movie: Movie,
    modifier: Modifier = Modifier
) {
    // TODO: Implement HeroBanner
    // - Box với fillMaxWidth + height(280.dp)
    // - Lớp 1: Box nền với movie.color + emoji ở giữa
    // - Lớp 2: Box overlay với Brush.verticalGradient (Transparent → Black.alpha0.3 → Black.alpha0.9)
    // - Lớp 3: Column (align = BottomStart, padding = 16.dp):
    //   → Text movie.title (headlineMedium, Bold, White)
    //   → Text "${genre} · ⭐ ${rating}" (bodyMedium, White.alpha0.8)
    //   → Spacer(12.dp)
    //   → Row buttons: Button "▶ Watch" (White bg) + OutlinedButton "+ My List"
    Box {}
}

@Composable
fun CategoryChipRow(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // TODO: Implement CategoryChipRow
    // - Row với horizontalScroll(rememberScrollState()) + padding(horizontal=16.dp)
    // - horizontalArrangement = spacedBy(8.dp)
    // - Với mỗi category: FilterChip(selected, onClick, label)
    // GỢI Ý: Row(horizontalScroll) nhẹ hơn LazyRow cho list ngắn (<10 items)
    Box {}
}

@Composable
fun MovieSection(
    section: MovieSection,
    modifier: Modifier = Modifier
) {
    // TODO: Implement MovieSection
    // - Column:
    //   → Text section.title (titleMedium, Bold, White, padding horizontal=16dp vertical=8dp)
    //   → Row với horizontalScroll(rememberScrollState()) + padding(horizontal=16.dp) + spacedBy(12.dp)
    //   → Mỗi movie: MovieCard(movie)
    // GỢI Ý: Tại sao KHÔNG dùng LazyRow ở đây?
    // → Nested Lazy với cùng hướng scroll → crash. Khác hướng thì OK.
    Box {}
}

@Composable
fun MovieCard(
    movie: Movie,
    modifier: Modifier = Modifier
) {
    // TODO: Implement MovieCard
    // - Box với width(120.dp) + height(160.dp) + clip(RoundedCornerShape(8.dp)) + background(movie.color)
    // - Text emoji (40.sp, align = Center)
    // - Box overlay gradient ở BottomCenter (Transparent → Black.alpha0.8)
    //   + Column bên trong: Text title (labelSmall, Bold, White) + Text "⭐ ${rating}" (labelSmall, White.alpha0.7)
    Box {}
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
