package com.apero.composetraining.session7.exercises

import android.content.res.Configuration
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apero.composetraining.common.AppTheme

/**
 * ⭐⭐⭐⭐ BÀI TẬP NÂNG CAO BUỔI 7: Shared Element Transition — Compose 1.7+
 *
 * Mô tả: Movie list với shared element animation khi chuyển sang detail
 *
 * List Screen:                    Detail Screen:
 * ┌─────────────────────┐         ┌─────────────────────────┐
 * │ ┌────┐ Movie Title  │  →→→    │ ┌────────────────────┐  │
 * │ │ 🎬 │ 2024 • 8.5   │  →→→    │ │                    │  │
 * │ └────┘              │  →→→    │ │   🎬 (animated)    │  │
 * │ ┌────┐ Movie Title  │         │ │                    │  │
 * │ │ 🎬 │ 2023 • 7.9   │         │ └────────────────────┘  │
 * │ └────┘              │         │ Movie Title (animated)  │
 * └─────────────────────┘         └─────────────────────────┘
 *
 * Key concepts:
 * - SharedTransitionLayout: container cho shared element transitions
 * - Modifier.sharedElement(): đánh dấu element được chia sẻ giữa screens
 * - rememberSharedContentState(key): state để track shared element
 * - AnimatedContent + SharedTransitionScope: kết hợp để tạo transition
 * - boundsTransform: custom animation curve cho transition
 *
 * Lưu ý: SharedTransitionLayout có trong Compose 1.7+ (BOM 2025.01.00)
 */

// ─── Data Model ──────────────────────────────────────────────────────────────

data class Movie(
    val id: Int,
    val title: String,
    val year: Int,
    val rating: Float,
    val color: Color, // Placeholder cho poster
    val description: String,
)

private val sampleMovies = listOf(
    Movie(1, "The Matrix Reloaded", 2024, 8.5f, Color(0xFF1E88E5), "A computer hacker learns about the true nature of reality."),
    Movie(2, "Inception Dreams", 2023, 8.8f, Color(0xFF43A047), "A thief who steals corporate secrets through dream-sharing."),
    Movie(3, "Interstellar Journey", 2024, 9.0f, Color(0xFF5E35B1), "Explorers travel through a wormhole in space."),
    Movie(4, "The Dark Knight Returns", 2023, 9.2f, Color(0xFF212121), "Batman faces the Joker in Gotham City."),
    Movie(5, "Pulp Fiction Redux", 2024, 8.9f, Color(0xFFFF7043), "Interconnected stories of crime in Los Angeles."),
    Movie(6, "Forrest Gump 2", 2023, 8.7f, Color(0xFF26A69A), "The continuation of an extraordinary life."),
)

// ─── Screen State ─────────────────────────────────────────────────────────────

sealed class ScreenState {
    data object List : ScreenState()
    data class Detail(val movie: Movie) : ScreenState()
}

// ─── Main Screen with SharedTransitionLayout ──────────────────────────────────

/**
 * SharedElementScreen — demo shared element transitions
 *
 * SharedTransitionLayout:
 * - Wrap tất cả content có shared elements
 * - Cung cấp SharedTransitionScope cho children
 * - Tự động animate elements có cùng key giữa các states
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedElementScreen(modifier: Modifier = Modifier) {
    var screenState: ScreenState by remember { mutableStateOf(ScreenState.List) }

    // SharedTransitionLayout: container cho tất cả shared element animations
    // Tất cả composables bên trong có thể dùng Modifier.sharedElement()
    SharedTransitionLayout(modifier = modifier.fillMaxSize()) {
        // AnimatedContent: animate transition giữa List và Detail
        // Kết hợp với SharedTransitionLayout để shared elements animate smoothly
        AnimatedContent(
            targetState = screenState,
            transitionSpec = {
                // Custom transition: fade + slide
                fadeIn(animationSpec = tween(300)) togetherWith
                    fadeOut(animationSpec = tween(300))
            },
            label = "screen_transition",
        ) { state ->
            when (state) {
                is ScreenState.List -> {
                    MovieListScreen(
                        movies = sampleMovies,
                        onMovieClick = { movie ->
                            screenState = ScreenState.Detail(movie)
                        },
                        // Pass AnimatedVisibilityScope để sharedElement hoạt động
                        animatedVisibilityScope = this@AnimatedContent,
                    )
                }

                is ScreenState.Detail -> {
                    MovieDetailScreen(
                        movie = state.movie,
                        onBack = { screenState = ScreenState.List },
                        animatedVisibilityScope = this@AnimatedContent,
                    )
                }
            }
        }
    }
}

// ─── Movie List Screen ────────────────────────────────────────────────────────

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.MovieListScreen(
    movies: List<Movie>,
    onMovieClick: (Movie) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        // Header
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "🎬 Movies",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "Tap a movie to see shared element transition",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(items = movies, key = { it.id }) { movie ->
                MovieListItem(
                    movie = movie,
                    onClick = { onMovieClick(movie) },
                    animatedVisibilityScope = animatedVisibilityScope,
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.MovieListItem(
    movie: Movie,
    onClick: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // Poster — SHARED ELEMENT
            // sharedElement(): đánh dấu element này sẽ animate sang screen khác
            // rememberSharedContentState(key): key phải UNIQUE và GIỐNG nhau ở cả 2 screens
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .sharedElement(
                        state = rememberSharedContentState(key = "poster_${movie.id}"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        // boundsTransform: custom animation curve
                        // Mặc định dùng spring(), có thể đổi sang tween() hoặc custom
                        boundsTransform = { _, _ ->
                            tween(durationMillis = 400)
                        },
                    )
                    .clip(MaterialTheme.shapes.medium)
                    .background(movie.color),
                contentAlignment = Alignment.Center,
            ) {
                Text("🎬", style = MaterialTheme.typography.headlineLarge)
            }

            // Movie info
            Column(modifier = Modifier.weight(1f)) {
                // Title — cũng là shared element
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.sharedElement(
                        state = rememberSharedContentState(key = "title_${movie.id}"),
                        animatedVisibilityScope = animatedVisibilityScope,
                    ),
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${movie.year} • ⭐ ${movie.rating}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

// ─── Movie Detail Screen ──────────────────────────────────────────────────────

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun SharedTransitionScope.MovieDetailScreen(
    movie: Movie,
    onBack: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                ),
            )
        },
        modifier = modifier,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            // Large poster — SHARED ELEMENT (cùng key với list item)
            // Compose sẽ tự animate từ vị trí nhỏ (list) sang vị trí lớn (detail)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .sharedElement(
                        state = rememberSharedContentState(key = "poster_${movie.id}"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = { _, _ ->
                            tween(durationMillis = 400)
                        },
                    )
                    .background(movie.color),
                contentAlignment = Alignment.Center,
            ) {
                Text("🎬", style = MaterialTheme.typography.displayLarge)
            }

            Column(modifier = Modifier.padding(16.dp)) {
                // Title — SHARED ELEMENT
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.sharedElement(
                        state = rememberSharedContentState(key = "title_${movie.id}"),
                        animatedVisibilityScope = animatedVisibilityScope,
                    ),
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Non-shared content — sẽ fade in/out bình thường
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    AssistChip(
                        onClick = { },
                        label = { Text("${movie.year}") },
                    )
                    AssistChip(
                        onClick = { },
                        label = { Text("⭐ ${movie.rating}") },
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Description",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = movie.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Info card
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    ),
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "💡 Shared Element Transition",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Poster và Title được animate từ list → detail.\n" +
                                "Key: \"poster_${movie.id}\" và \"title_${movie.id}\"",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                        )
                    }
                }
            }
        }
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true, name = "Shared Element - Light")
@Composable
private fun SharedElementPreview() {
    AppTheme {
        SharedElementScreen()
    }
}

@Preview(
    showBackground = true,
    name = "Shared Element - Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun SharedElementDarkPreview() {
    AppTheme(darkTheme = true) {
        SharedElementScreen()
    }
}
