package com.apero.composetraining.session4.exercises

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apero.composetraining.common.AppTheme
import kotlinx.coroutines.delay
import kotlin.math.abs

/**
 * ⭐⭐⭐⭐⭐ BÀI TẬP NÂNG CAO BUỔI 4: Pinterest-style Staggered Gallery
 *
 * Mô tả: Gallery với chiều cao item khác nhau (staggered) + category filtering
 *
 * ┌─────────────────────────────────────────────────────┐
 * │ 📸 Gallery                                       🔍 │  ← TopAppBar
 * ├─────────────────────────────────────────────────────┤
 * │ [All] [Nature] [City] [People] [Food] [Travel]      │  ← FilterChip LazyRow
 * ├──────────────────────┬──────────────────────────────┤
 * │ ┌──────────────────┐ │ ┌──────────────────────────┐ │
 * │ │  Short photo     │ │ │   Tall photo              │ │  ← Staggered grid
 * │ │  (Nature)        │ │ │   (City)                  │ │
 * │ └──────────────────┘ │ │                           │ │
 * │ ┌──────────────────┐ │ │                           │ │
 * │ │  Very tall photo │ │ └──────────────────────────┘ │
 * │ │  (People)        │ │ ┌──────────────────────────┐ │
 * │ │                  │ │ │  Short photo (Food)       │ │
 * │ └──────────────────┘ │ └──────────────────────────┘ │
 * └──────────────────────┴──────────────────────────────┘
 *
 * Key concepts:
 * - LazyVerticalStaggeredGrid: render grid với các item chiều cao khác nhau
 * - StaggeredGridCells.Adaptive(150.dp): số cột tự động theo width (responsive)
 * - StaggeredGridCells.Fixed(2): luôn 2 cột (không responsive)
 * - PullToRefreshBox: Material3 pull-to-refresh (experimental)
 */

// ─── Data Model ──────────────────────────────────────────────────────────────

data class Photo(
    val id: Int,
    val title: String,
    val category: String,
    val color: Color, // Thay thế cho ảnh thật
)

// Categories
private val categories = listOf("All", "Nature", "City", "People", "Food", "Travel")

// 24 photos giả với màu sắc đẹp
private val samplePhotos = listOf(
    Photo(1, "Mountain Sunrise", "Nature", Color(0xFF4CAF50)),
    Photo(2, "City at Night", "City", Color(0xFF3F51B5)),
    Photo(3, "Portrait", "People", Color(0xFFE91E63)),
    Photo(4, "Street Food", "Food", Color(0xFFFF9800)),
    Photo(5, "Beach Sunset", "Travel", Color(0xFFFF5722)),
    Photo(6, "Forest Path", "Nature", Color(0xFF8BC34A)),
    Photo(7, "Skyscrapers", "City", Color(0xFF607D8B)),
    Photo(8, "Coffee Art", "Food", Color(0xFF795548)),
    Photo(9, "Desert Dunes", "Travel", Color(0xFFFFEB3B)),
    Photo(10, "Waterfall", "Nature", Color(0xFF00BCD4)),
    Photo(11, "Night Market", "City", Color(0xFF9C27B0)),
    Photo(12, "Family Photo", "People", Color(0xFFFF4081)),
    Photo(13, "Sushi", "Food", Color(0xFFF06292)),
    Photo(14, "Eiffel Tower", "Travel", Color(0xFF5C6BC0)),
    Photo(15, "Cherry Blossoms", "Nature", Color(0xFFEC407A)),
    Photo(16, "Subway Station", "City", Color(0xFF42A5F5)),
    Photo(17, "Graduation", "People", Color(0xFF26A69A)),
    Photo(18, "Pizza", "Food", Color(0xFFEF5350)),
    Photo(19, "Bali Temple", "Travel", Color(0xFFAB47BC)),
    Photo(20, "Ocean Waves", "Nature", Color(0xFF29B6F6)),
    Photo(21, "Bridge View", "City", Color(0xFF66BB6A)),
    Photo(22, "Street Performer", "People", Color(0xFFFF7043)),
    Photo(23, "Ramen Bowl", "Food", Color(0xFFDCE775)),
    Photo(24, "Tokyo Streets", "Travel", Color(0xFF26C6DA)),
)

// ─── Main Screen ──────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaggeredGalleryScreen(modifier: Modifier = Modifier) {
    // State cho selected category
    var selectedCategory by remember { mutableStateOf("All") }

    // State cho pull-to-refresh
    var isRefreshing by remember { mutableStateOf(false) }

    // Photos sau khi filter
    val filteredPhotos by remember(selectedCategory) {
        derivedStateOf {
            if (selectedCategory == "All") samplePhotos
            else samplePhotos.filter { it.category == selectedCategory }
        }
    }

    // Simulate refresh
    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            delay(1500L) // Giả lập network request
            isRefreshing = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "📸 Gallery",
                        fontWeight = FontWeight.Bold,
                    )
                },
                actions = {
                    IconButton(onClick = { /* Search */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                    IconButton(onClick = { isRefreshing = true }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                },
            )
        },
        modifier = modifier,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            // Category filter tabs
            CategoryFilterRow(
                categories = categories,
                selectedCategory = selectedCategory,
                onCategorySelected = { selectedCategory = it },
                modifier = Modifier.padding(vertical = 8.dp),
            )

            // Grid info text
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "${filteredPhotos.size} photos",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.GridOn,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Adaptive(150dp)",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }

            // PullToRefreshBox: wrap content với pull-to-refresh behavior
            // experimental trong Material3, dùng @OptIn(ExperimentalMaterial3Api::class)
            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = { isRefreshing = true },
                modifier = Modifier.fillMaxSize(),
            ) {
                if (filteredPhotos.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "No photos in \"$selectedCategory\"",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                } else {
                    // LazyVerticalStaggeredGrid — điểm chính của bài tập
                    StaggeredPhotoGrid(photos = filteredPhotos)
                }
            }
        }
    }
}

// ─── Staggered Grid ───────────────────────────────────────────────────────────

@Composable
private fun StaggeredPhotoGrid(
    photos: List<Photo>,
    modifier: Modifier = Modifier,
) {
    // LazyVerticalStaggeredGrid: giống LazyVerticalGrid nhưng cho phép height khác nhau
    //
    // StaggeredGridCells.Adaptive(150.dp): số cột tự tính để mỗi cột >= 150dp
    //   → Ưu: Responsive (tablet sẽ có nhiều cột hơn phone)
    //   → Nhược: Số cột thay đổi khi xoay màn hình
    //
    // StaggeredGridCells.Fixed(2): luôn 2 cột
    //   → Ưu: Predictable layout
    //   → Nhược: Không responsive (tablet = 2 cột to → xấu)
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(150.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalItemSpacing = 8.dp,
        modifier = modifier.fillMaxSize(),
    ) {
        items(
            items = photos,
            key = { it.id }, // Key quan trọng cho animation và identity
        ) { photo ->
            PhotoCard(
                photo = photo,
                // Chiều cao deterministic dựa vào title.hashCode()
                // → Mỗi lần render, cùng photo = cùng height (không random)
                height = calculateDeterministicHeight(photo.title),
            )
        }
    }
}

/**
 * Tính height deterministic từ title (không random)
 *
 * Tại sao không dùng Random?
 * → Compose có thể recompose bất kỳ lúc nào
 * → Random() mỗi lần recompose → height thay đổi → layout jump
 * → Dùng hashCode() đảm bảo cùng input = cùng output
 */
private fun calculateDeterministicHeight(title: String): Dp {
    val hash = abs(title.hashCode())
    val minHeight = 120
    val maxHeight = 280
    return (minHeight + hash % (maxHeight - minHeight)).dp
}

// ─── Photo Card ───────────────────────────────────────────────────────────────

@Composable
private fun PhotoCard(
    photo: Photo,
    height: Dp,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(MaterialTheme.shapes.medium),
    ) {
        // Nền màu thay thế ảnh thật
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(photo.color),
        )

        // Gradient overlay ở cuối card — phổ biến trong gallery apps
        // Tạo cảm giác depth và làm text dễ đọc hơn trên nền tối/sáng
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .align(Alignment.BottomCenter)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.7f),
                        ),
                    ),
                ),
        )

        // Title và category chip ở cuối card
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(8.dp),
        ) {
            Text(
                text = photo.title,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Category chip
            Surface(
                shape = MaterialTheme.shapes.extraSmall,
                color = Color.White.copy(alpha = 0.25f),
            ) {
                Text(
                    text = photo.category,
                    color = Color.White,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                )
            }
        }
    }
}

// ─── Category Filter Row ──────────────────────────────────────────────────────

@Composable
private fun CategoryFilterRow(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    // LazyRow cho category chips — horizontal scroll nếu nhiều categories
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(
            items = categories,
            key = { it }, // Category name là unique key
        ) { category ->
            FilterChip(
                selected = category == selectedCategory,
                onClick = { onCategorySelected(category) },
                label = { Text(category) },
            )
        }
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true, name = "Staggered Gallery - Light")
@Composable
private fun StaggeredGalleryPreview() {
    AppTheme {
        StaggeredGalleryScreen()
    }
}

@Preview(
    showBackground = true,
    name = "Staggered Gallery - Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun StaggeredGalleryDarkPreview() {
    AppTheme(darkTheme = true) {
        StaggeredGalleryScreen()
    }
}

@Preview(showBackground = true, name = "Photo Card Preview")
@Composable
private fun PhotoCardPreview() {
    AppTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(8.dp)) {
            PhotoCard(photo = samplePhotos[0], height = 180.dp, modifier = Modifier.weight(1f))
            PhotoCard(photo = samplePhotos[1], height = 240.dp, modifier = Modifier.weight(1f))
        }
    }
}
