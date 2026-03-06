package com.apero.composetraining.session2.exercises

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    DashboardItem(
        i,
        "App #$i",
        "Mô tả app $i ngắn gọn",
        listOf("AI", "Photo", "Video", "Utility")[i % 4]
    )
}

private fun Modifier.coloringBackground(
    colors: List<Color>, connerRadius: Dp = 0.dp
): Modifier {
    return this.drawBehind {
        drawRoundRect(
            brush = Brush.horizontalGradient(
                colors = colors
            ),
            cornerRadius = CornerRadius(connerRadius.toPx())
        )
    }
}

// TODO: [Session 2] Bài tập 3 - Implement DashboardScreen
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun DashboardScreen(
    stats: List<StatItem> = sampleStats,
    items: List<DashboardItem> = sampleItems,
    isPremium: Boolean = false
) {
    BoxWithConstraints {
        if (maxWidth < 600.dp) PhoneLayout(stats = stats, items = items, isPremium = isPremium)
        else TabletLayout(stats = stats, listItems = items, isPremium = isPremium)
    }
}

// TODO: [Session 2] Bài tập 3 - Implement StatsRow (dùng IntrinsicSize.Min)
// Row(Modifier.height(IntrinsicSize.Min).fillMaxWidth()) {
//     stats.forEachIndexed { index, stat ->
//         StatColumn(stat, Modifier.weight(1f))
//         if (index < stats.size - 1) VerticalDivider()
//     }
// }

@Preview
@Composable
private fun StatColumn(
    modifier: Modifier = Modifier,
    title: String = "Apps",
    amount: String = "19",
    unit: String = "apps"
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {

        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White,
            maxLines = 1,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp)
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = amount,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 1,
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            Text(
                text = unit,
                fontSize = 14.sp,
                color = Color.White,
                maxLines = 1,
                textAlign = TextAlign.Center
            )
        }

    }
}

@Stable
@Composable
private fun StatsRow(
    stats: List<StatItem>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .coloringBackground(
                colors = listOf(
                    Color(0xFFFF512F),
                    Color(0xFFDD2476)
                ),
                connerRadius = 10.dp
            )
            .padding(vertical = 10.dp)
    ) {
        for (i in 0..<stats.size) {
            StatColumn(
                title = sampleStats[i].label,
                amount = sampleStats[i].value,
                unit = sampleStats[i].unit,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )

            if (i < sampleStats.size - 1) {
                Spacer(
                    modifier = Modifier
                        .width(5.dp)
                        .fillMaxHeight()
                )

                VerticalDivider(
                    thickness = 2.dp,
                    color = Color.LightGray,
                    modifier = Modifier.fillMaxHeight()
                )

                Spacer(
                    modifier = Modifier
                        .width(5.dp)
                        .fillMaxHeight()
                )
            }
        }
    }
}

// TODO: [Session 2] Bài tập 3 - Implement PremiumBanner (dùng drawBehind)
// Box(
//     modifier = Modifier
//         .fillMaxWidth()
//         .height(80.dp)
//         .drawBehind {
//             drawRect(brush = Brush.horizontalGradient(...))
//         }
// ) { ... }

@Preview
@Composable
private fun PremiumBannerPhone() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .coloringBackground(
                colors = listOf(
                    Color(0xFF00C6FF),
                    Color(0xFF0072FF)
                ),
                connerRadius = 24.dp
            )
            .padding(vertical = 25.dp)
    ) {

        Text(
            text = "Premium banner",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Text(
            text = "Enable exclusive features and experiences",
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Button(
            onClick = {},
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Go Premium",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview
@Composable
fun PremiumBannerTablet(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .coloringBackground(
                colors = listOf(
                    Color(0xFF1BA9E1),
                    Color(0xFF1C6FE8)
                ),
                connerRadius = 24.dp
            )
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Premium banner",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "Enable exclusive features and experiences",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 14.sp
            )
        }

        Spacer(Modifier.width(24.dp))

        Button(
            onClick = onClick,
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            )
        ) {
            Text("Go Premium")
        }
    }
}

// TODO: [Session 2] Bài tập 3 - Implement PhoneLayout
// LazyColumn: PremiumBanner + StatsRow + items(DashboardCard) 1 per row

@Composable
fun PhoneLayout(
    modifier: Modifier = Modifier,
    isPremium: Boolean = false,
    stats: List<StatItem>,
    items: List<DashboardItem>
) {

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {

        item {
            if (!isPremium) PremiumBannerPhone()

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            StatsRow(stats)

            Spacer(
                modifier = Modifier.height(20.dp)
            )
        }

        items(items = items, key = { item -> item.id }) {
            DashboardCard(
                title = it.title,
                description = it.description,
                chips = listOf(it.category),
                modifier = Modifier
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )
        }
    }

}

// TODO: [Session 2] Bài tập 3 - Implement TabletLayout
// LazyVerticalGrid(GridCells.Fixed(2)): header + items(DashboardCard) 2 per row

@Composable
private fun TabletLayout(
    modifier: Modifier = Modifier,
    isPremium: Boolean = false,
    stats: List<StatItem>,
    listItems: List<DashboardItem>
) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {

        item(span = { GridItemSpan(2) }) {

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                if (!isPremium) PremiumBannerTablet {}

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                StatsRow(stats)

                Spacer(
                    modifier = Modifier.height(20.dp)
                )
            }
        }

        items(items = listItems, key = { item -> item.id }
        ) {
            DashboardCard(
                title = it.title,
                description = it.description,
                chips = listOf(it.category),
                modifier = Modifier
            )
        }
    }

}


// TODO: [Session 2] Bài tập 3 - Implement DashboardCard (stateless)
// Card: title bold + description + category chip

@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
private fun DashboardCard(
    modifier: Modifier = Modifier,
    title: String = "Title",
    description: String = "My description",
    chips: List<String> = listOf("Chip 1", "Chip 2", "Chip 3", "Chip 4", "Chip 5", "Chip 6")
) {

    Card(
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.LightGray
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {

            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 1,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = description,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White,
                maxLines = 1,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                chips.forEach { chip ->
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .widthIn(50.dp, 150.dp)
                            .background(Color.White, shape = RoundedCornerShape(12.dp))
                            .padding(5.dp)
                    ) {
                        Text(
                            text = chip,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFF512F)
                        )
                    }
                }

            }

        }
    }

}

@Composable
fun ResponsiveDashboardScreen() {
    DashboardScreen()
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