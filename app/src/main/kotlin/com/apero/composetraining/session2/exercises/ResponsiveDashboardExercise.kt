package com.apero.composetraining.session2.exercises

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apero.composetraining.common.AppTheme

/**
 * ⭐⭐⭐ BÀI TẬP 3: Responsive Dashboard (Khó — 60 phút)
 *
 * Yêu cầu:
 * - BoxWithConstraints để detect screen width
 *   → Phone (maxWidth < 600.dp): LazyColumn, 1 card per row
 *   → Tablet (maxWidth ≥ 600.dp): LazyVerticalGrid, 2 columns
 * - Stats row: 4 stats cột ngang với VerticalDivider, dùng IntrinsicSize.Min để bằng cao
 * - Premium banner với Modifier.drawBehind (custom gradient background)
 *
 * Tiêu chí:
 * - BoxWithConstraints đúng cách (dùng maxWidth từ constraints)
 * - Modifier.height(IntrinsicSize.Min) + VerticalDivider trên stats row
 * - Modifier.drawBehind { drawRect(brush = gradient) } cho banner
 * - KHÔNG hardcode layout cho device type
 *
 * Gợi ý BoxWithConstraints:
 * BoxWithConstraints {
 *     if (maxWidth < 600.dp) {
 *         PhoneLayout(stats, items, isPremium)
 *     } else {
 *         TabletLayout(stats, items, isPremium)
 *     }
 * }
 *
 * Gợi ý drawBehind:
 * Modifier.drawBehind {
 *     drawRect(
 *         brush = Brush.horizontalGradient(listOf(Color(0xFF6750A4), Color(0xFF9C27B0)))
 *     )
 * }
 */

data class StatItem(
    val label: String,
    val value: String,
    val unit: String = ""
)

data class DashboardItem(
    val id: Int,
    val title: String,
    val description: String,
    val category: String
)

private val sampleStats = listOf(
    StatItem("Apps", "19", "apps"),
    StatItem("Downloads", "2.4M", ""),
    StatItem("Rating", "4.8", "⭐"),
    StatItem("Reviews", "12K", "")
)

private val sampleItems = (1..8).map { i ->
    DashboardItem(i, "App #$i", "Mô tả app $i ngắn gọn", listOf("AI", "Photo", "Video", "Utility")[i % 4])
}

// TODO: [Session 2] Bài tập 3 - Implement DashboardScreen
// Dùng BoxWithConstraints để switch layout:
// @Composable
// fun DashboardScreen(
//     stats: List<StatItem> = sampleStats,
//     items: List<DashboardItem> = sampleItems,
//     isPremium: Boolean = false
// ) {
//     BoxWithConstraints {
//         if (maxWidth < 600.dp) PhoneLayout(stats, items, isPremium)
//         else TabletLayout(stats, items, isPremium)
//     }
// }

// TODO: [Session 2] Bài tập 3 - Implement StatsRow (dùng IntrinsicSize.Min)
// Row(Modifier.height(IntrinsicSize.Min).fillMaxWidth()) {
//     stats.forEachIndexed { index, stat ->
//         StatColumn(stat, Modifier.weight(1f))
//         if (index < stats.size - 1) VerticalDivider()
//     }
// }

// TODO: [Session 2] Bài tập 3 - Implement PremiumBanner (dùng drawBehind)
// Box(
//     modifier = Modifier
//         .fillMaxWidth()
//         .height(80.dp)
//         .drawBehind {
//             drawRect(brush = Brush.horizontalGradient(...))
//         }
// ) { ... }

// TODO: [Session 2] Bài tập 3 - Implement PhoneLayout
// LazyColumn: PremiumBanner + StatsRow + items(DashboardCard) 1 per row

// TODO: [Session 2] Bài tập 3 - Implement TabletLayout
// LazyVerticalGrid(GridCells.Fixed(2)): header + items(DashboardCard) 2 per row

// TODO: [Session 2] Bài tập 3 - Implement DashboardCard (stateless)
// Card: title bold + description + category chip

@Composable
fun ResponsiveDashboardScreen() {
    // TODO: Thay bằng DashboardScreen() đã implement
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Responsive Dashboard", style = MaterialTheme.typography.headlineSmall)
        Text("TODO: dùng BoxWithConstraints để switch Phone/Tablet layout",
            color = MaterialTheme.colorScheme.error)
        Text("TODO: StatsRow với IntrinsicSize.Min + VerticalDivider",
            color = MaterialTheme.colorScheme.error)
        Text("TODO: PremiumBanner với Modifier.drawBehind gradient",
            color = MaterialTheme.colorScheme.error)
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun PhonePreview() {
    AppTheme { ResponsiveDashboardScreen() }
}

@Preview(showBackground = true, widthDp = 720)
@Composable
private fun TabletPreview() {
    AppTheme { ResponsiveDashboardScreen() }
}
