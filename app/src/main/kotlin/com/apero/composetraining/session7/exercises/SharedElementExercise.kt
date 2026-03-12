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
    val color: Color,
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
    // TODO: Implement SharedElementScreen
    // 1. var screenState: ScreenState by remember { mutableStateOf(ScreenState.List) }
    //
    // 2. SharedTransitionLayout(modifier = Modifier.fillMaxSize()) {
    //        // SharedTransitionLayout wrap tất cả content có shared elements
    //        // Tất cả composables bên trong có thể dùng Modifier.sharedElement()
    //
    //        AnimatedContent(
    //            targetState = screenState,
    //            transitionSpec = {
    //                fadeIn(tween(300)) togetherWith fadeOut(tween(300))
    //            },
    //            label = "screen_transition"
    //        ) { state →
    //            when (state) {
    //                is ScreenState.List → MovieListScreen(
    //                    movies = sampleMovies,
    //                    onMovieClick = { movie → screenState = ScreenState.Detail(movie) },
    //                    animatedVisibilityScope = this@AnimatedContent
    //                )
    //                is ScreenState.Detail → MovieDetailScreen(
    //                    movie = state.movie,
    //                    onBack = { screenState = ScreenState.List },
    //                    animatedVisibilityScope = this@AnimatedContent
    //                )
    //            }
    //        }
    //    }
    Box {}
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
    // TODO: Implement MovieListScreen
    // - Column(fillMaxSize):
    //   → Header: Text "🎬 Movies" + subtitle
    //   → LazyColumn(contentPadding, spacedBy=12.dp):
    //     items(movies, key = { it.id }) { movie →
    //         MovieListItem(movie, onClick, animatedVisibilityScope)
    //     }
    Box {}
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.MovieListItem(
    movie: Movie,
    onClick: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {
    // TODO: Implement MovieListItem với shared elements
    // - Card(fillMaxWidth, clickable):
    //   Row(padding=12.dp, spacedBy=12.dp):
    //
    //   // Poster — SHARED ELEMENT
    //   Box(
    //       modifier = Modifier
    //           .size(80.dp)
    //           .sharedElement(
    //               state = rememberSharedContentState(key = "poster_${movie.id}"),
    //               animatedVisibilityScope = animatedVisibilityScope,
    //               boundsTransform = { _, _ → tween(durationMillis = 400) }
    //           )
    //           .clip(shapes.medium)
    //           .background(movie.color)
    //   ) { Text "🎬" (headlineLarge, Center) }
    //
    //   // Movie info
    //   Column(weight(1f)):
    //       // Title — also shared element
    //       Text(
    //           text = movie.title,
    //           modifier = Modifier.sharedElement(
    //               state = rememberSharedContentState(key = "title_${movie.id}"),
    //               animatedVisibilityScope = animatedVisibilityScope
    //           )
    //       )
    //       Text "${year} • ⭐ ${rating}"
    //
    // GỢI Ý: sharedElement key phải UNIQUE và GIỐNG nhau ở cả list và detail
    Box {}
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
    // TODO: Implement MovieDetailScreen với shared elements
    // - Scaffold với TopAppBar (transparent, back icon)
    // - Column(fillMaxSize):
    //
    //   // Large poster — SHARED ELEMENT (cùng key với list item)
    //   Box(
    //       modifier = Modifier
    //           .fillMaxWidth()
    //           .height(280.dp)
    //           .sharedElement(
    //               state = rememberSharedContentState(key = "poster_${movie.id}"),
    //               animatedVisibilityScope = animatedVisibilityScope,
    //               boundsTransform = { _, _ → tween(durationMillis = 400) }
    //           )
    //           .background(movie.color)
    //   ) { Text "🎬" (displayLarge, Center) }
    //
    //   Column(padding=16.dp):
    //       // Title — SHARED ELEMENT
    //       Text(movie.title, modifier = Modifier.sharedElement(
    //           state = rememberSharedContentState(key = "title_${movie.id}"), ...
    //       ))
    //
    //       // Non-shared content (sẽ fade in/out bình thường)
    //       Row chips: year + rating
    //       Spacer + Text "Description" + Text movie.description
    //       Card info về shared element transition
    //
    // GỢI Ý: Compose tự animate từ vị trí nhỏ (list) → vị trí lớn (detail)
    // vì key giống nhau
    Box {}
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
