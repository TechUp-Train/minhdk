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
 * ⭐⭐⭐⭐ BÀI TẬP NÂNG CAO BUỔI 2: Streaming App Home (Netflix-style)
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
 *     CategoryChips(...)                      ← Row với horizontalScroll
 *
 *     movieSections.forEach { section ->
 *         SectionTitle(...)
 *         MovieRow(section.movies)            ← Row với horizontalScroll (KHÔNG phải LazyRow!)
 *     }
 * }
 *
 * ⚠️ WARNING: Đừng dùng LazyRow bên trong Column(verticalScroll)!
 * → LazyRow needs unbounded width constraint
 * → Column(verticalScroll) gives unbounded height
 * → Nested unbounded = CRASH
 *
 * Dùng Row(horizontalScroll) thay thế cho danh sách nhỏ (< 20 items)
 */
@Composable
fun StreamingHomeScreen(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    var selectedCategory by remember { mutableStateOf("All") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)  // ← Outer scroll
            .background(Color(0xFF0D0D0D))
    ) {
        // ── Hero Banner ──────────────────────────────────────────────
        HeroBanner(movie = featuredMovie)

        Spacer(modifier = Modifier.height(16.dp))

        // ── Category Chips ───────────────────────────────────────────
        // Row(horizontalScroll) — OK bên trong Column(verticalScroll)
        // Khác hướng scroll → không có conflict
        CategoryChipRow(
            categories = categories,
            selectedCategory = selectedCategory,
            onCategorySelect = { selectedCategory = it }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // ── Movie Sections ───────────────────────────────────────────
        movieSections.forEach { section ->
            MovieSection(section = section)
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Bottom spacing
        Spacer(modifier = Modifier.height(80.dp))
    }
}

// ─── Sub-components ──────────────────────────────────────────────────────────

/**
 * Hero Banner — Box với gradient text overlay
 *
 * TODO: [Buổi 2 — Box] Cấu trúc:
 * Box {
 *     // Background (movie poster placeholder)
 *     Box(fillMaxSize, background gradient)
 *
 *     // Overlay gradient (dark → transparent → dark)
 *     Box(fillMaxSize, background Brush.verticalGradient)
 *
 *     // Content (align Bottom)
 *     Column(Alignment.BottomStart) {
 *         Title + Genre + Action buttons
 *     }
 * }
 */
@Composable
fun HeroBanner(
    movie: Movie,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(280.dp)
    ) {
        // Background — movie poster placeholder
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(movie.color)
        ) {
            // Emoji làm placeholder poster
            Text(
                text = movie.emoji,
                fontSize = 80.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Dark gradient overlay — bottom heavy
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.3f),
                            Color.Black.copy(alpha = 0.9f)
                        )
                    )
                )
        )

        // Content — align Bottom
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Text(
                text = movie.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "${movie.genre} · ⭐ ${movie.rating}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Action buttons
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    Text("▶ Watch", fontWeight = FontWeight.Bold)
                }
                OutlinedButton(
                    onClick = {},
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text("+ My List")
                }
            }
        }
    }
}

@Composable
fun CategoryChipRow(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Row(horizontalScroll) — nhẹ hơn LazyRow cho list ngắn
    Row(
        modifier = modifier
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEach { category ->
            FilterChip(
                selected = category == selectedCategory,
                onClick = { onCategorySelect(category) },
                label = { Text(category) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    }
}

@Composable
fun MovieSection(
    section: MovieSection,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Section title
        Text(
            text = section.title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        // Movie row — Row(horizontalScroll) KHÔNG phải LazyRow
        // TODO: [Buổi 2] Giải thích tại sao KHÔNG dùng LazyRow ở đây
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            section.movies.forEach { movie ->
                MovieCard(movie = movie)
            }
        }
    }
}

@Composable
fun MovieCard(
    movie: Movie,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(120.dp)
            .height(160.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(movie.color)
    ) {
        // Poster emoji placeholder
        Text(
            text = movie.emoji,
            fontSize = 40.sp,
            modifier = Modifier.align(Alignment.Center)
        )

        // Bottom info overlay
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .background(
                    androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
                    )
                )
                .padding(8.dp)
        ) {
            Column {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1
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
 *     → Column(verticalScroll) cho unlimited height constraint
 *     → LazyRow cần width constraint để lazy-load đúng
 *     → Kết hợp 2 cái → "Nesting scrollable in the same direction" → crash hoặc không render
 *     → GIẢI PHÁP: Row(horizontalScroll) là non-lazy → không có constraint conflict
 *     → Hoặc: Dùng LazyColumn bên ngoài + item { Row(horizontalScroll) } bên trong
 *
 * Q2: Khi nào dùng Row(horizontalScroll) vs LazyRow?
 *     → < 20 items: Row(horizontalScroll) — đơn giản hơn, không overhead
 *     → > 20 items hoặc cần recycling: LazyRow — nhưng phải đặt trong LazyColumn parent
 *
 * Q3: Gradient overlay trên HeroBanner — tại sao cần 2 Box thay vì 1?
 *     → Box 1: Background/poster
 *     → Box 2: Overlay gradient (separate layer để control opacity riêng)
 *     → Nếu gộp vào 1: không thể animate overlay opacity độc lập
 *
 * Q4: contentPadding vs padding — khác gì?
 *     → padding(16.dp) trên Column → space bên ngoài, items bị clip khi scroll
 *     → contentPadding (LazyColumn) → items scroll đến tận edge, chỉ có padding ở đầu/cuối
 *     → Bài này dùng padding trên từng Row vì đơn giản hơn
 */
