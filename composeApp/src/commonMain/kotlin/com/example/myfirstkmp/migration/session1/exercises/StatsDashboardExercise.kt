package com.example.myfirstkmp.migration.session1.exercises

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * ⭐⭐⭐⭐ BÀI TẬP NÂNG CAO: Stats Dashboard
 *
 * Yêu cầu:
 * 1. StatCard với Slot API cho trend indicator
 * 2. TrendIndicator component (icon + %, màu xanh/đỏ)
 * 3. Dashboard 2x2 grid dùng Row + Column
 * 4. EQUAL HEIGHT cards — bắt buộc dùng IntrinsicSize.Max + fillMaxHeight()
 * 5. Compose Phase optimization — TrendIcon dùng graphicsLayer thay vì rotate()
 * 6. 3 @Preview: All Positive / Mixed / Dark Mode
 *
 * Khái niệm áp dụng từ Buổi 1:
 * - Slot API (trend: @Composable () -> Unit)
 * - IntrinsicSize.Max — equal height trick (học từ slide 6 Modifier)
 * - graphicsLayer { rotationZ } — skip Layout phase (Slide 11!)
 * - Modifier chain & order
 */

// ─── Data Model ───────────────────────────────────────────────────────────────

data class StatData(
    val label: String,
    val value: String,
    val percentage: String,
    val isPositive: Boolean,
    val emoji: String = "📊"
)

// ─── Components ──────────────────────────────────────────────────────────────
@Composable
fun StatCard(
    label: String,
    value: String,
    emoji: String,
    modifier: Modifier = Modifier,
    trend: @Composable ColumnScope.() -> Unit = {}
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .padding(16.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = emoji,
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    modifier = Modifier.wrapContentSize()
                )

                Text(
                    text = label,
                    fontSize = 20.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(start = 10.dp)
                )
            }

            Text(
                text = value,
                fontSize = 30.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            )

            trend()

        }
    }
}

@Composable
fun TrendIndicator(
    percentage: String,
    isPositive: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {

        val positiveColor = if (isPositive) Color.Green else Color.Red

        Icon(
            imageVector = Icons.Default.KeyboardArrowUp,
            contentDescription = null,
            tint = positiveColor,
            modifier = Modifier
                .size(25.dp)
                .graphicsLayer { rotationZ = if (isPositive) 0f else 180f }
        )

        Text(
            text = percentage,
            fontSize = 16.sp,
            color = Color.Black,
            maxLines = 1,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .wrapContentSize()
        )
    }
}

@Composable
private fun DashboardHeader(modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {

        Text(
            text = "\uD83D\uDCCA Dashboard",
            fontSize = 25.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
        )

    }

    Text(
        text = "Today",
        fontSize = 20.sp,
        color = Color.Black,
        fontWeight = FontWeight.Normal,
        maxLines = 1,
        textAlign = TextAlign.Start,
        modifier = Modifier
            .fillMaxWidth()
    )
}

@Composable
fun StatsDashboard(
    modifier: Modifier = Modifier,
    stats: List<StatData>
) {
    var rowCount = stats.size / 2
    if (rowCount % 2 != 0) rowCount++

    Card(
        colors = CardDefaults.cardColors().copy(containerColor = AzureBlue.copy(alpha = 0.2f)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp)
        ) {

            DashboardHeader(modifier = Modifier.fillMaxWidth().wrapContentSize())

            for (i in 0..<rowCount) {

                val rowData = getStatRow(i, stats)

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max)
                ) {

                    rowData.forEach { stat ->
                        stat?.let { absStat ->
                            StatCard(
                                label = absStat.label,
                                value = absStat.value,
                                emoji = absStat.emoji,
                                modifier = Modifier
                                    .weight(1f)
                                    .wrapContentHeight()
                            ) {
                                TrendIndicator(
                                    percentage = absStat.percentage,
                                    isPositive = absStat.isPositive,
                                    modifier = Modifier
                                        .padding(top = 10.dp)
                                )
                            }
                        }
                    }

                }

            }

        }
    }
}

private fun getStatRow(row: Int, list: List<StatData>): Array<StatData?> {
    val firstIdx = row * 2
    val secondIdx = firstIdx + 1
    val stats = arrayOfNulls<StatData>(2)
    if (firstIdx in 0..<list.size) stats[0] = list[firstIdx]
    if (secondIdx in 0..<list.size) stats[1] = list[secondIdx]
    return stats
}

// ─── Sample Data ──────────────────────────────────────────────────────────────

private val allPositiveStats = listOf(
    StatData("Downloads", "12,450", "↑ +23%", true, "📱"),
    StatData("Rating", "4.7 / 5.0", "↑ +0.2", true, "⭐"),
    StatData("Revenue", "\$2,840", "↑ +12%", true, "💰"),
    StatData("Active Users", "8,920", "↑ +5%", true, "👥")
)

private val mixedStats = listOf(
    StatData("Downloads", "12,450", "↑ +23%", true, "📱"),
    StatData("Rating", "4.6 / 5.0", "↓ -0.1", false, "⭐"),
    StatData("Revenue", "\$2,840", "↑ +12%", true, "💰"),
    StatData("Crash Rate", "0.8%", "↑ +0.3%", false, "💥")
)

// ─── Preview ─────────────────────────────────────────────────────────────────

@Preview
@Composable
fun TrendIndicatorPreview() {
    val data = allPositiveStats.first()
    TrendIndicator(
        percentage = data.percentage,
        isPositive = data.isPositive,
        modifier = Modifier.background(Color.Red)
    )
}

@Preview
@Composable
fun StatCardReview() {
    val data = allPositiveStats.first()
    StatCard(
        label = data.label,
        value = data.value,
        emoji = data.emoji,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        TrendIndicator(
            percentage = data.percentage,
            isPositive = data.isPositive,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StatDashboardAllPositivePreview() {
    StatsDashboard(
        modifier = Modifier.fillMaxWidth(),
        allPositiveStats
    )
}

@Preview
@Composable
fun StatDashboardAllMixedPreview() {
    StatsDashboard(
        modifier = Modifier.fillMaxWidth(),
        mixedStats
    )
}

