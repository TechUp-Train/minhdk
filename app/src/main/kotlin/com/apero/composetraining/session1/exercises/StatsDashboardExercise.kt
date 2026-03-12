package com.apero.composetraining.session1.exercises

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apero.composetraining.common.AppTheme

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

/**
 * Card hiển thị 1 stat với Slot API cho trend
 *
 * TODO: [Nâng cao] Tại sao Slot API tốt hơn truyền thẳng TrendIndicator vào?
 * → Caller có thể truyền bất kỳ UI nào vào (TrendIndicator, Chart, Badge...)
 */
@Composable
fun StatCard(
    label: String,
    value: String,
    emoji: String,
    modifier: Modifier = Modifier,
    // Slot API cho trend indicator — caller quyết định UI
    trend: @Composable () -> Unit = {}
) {
    // TODO: Implement StatCard layout
    // - Card với RoundedCornerShape(12.dp) và elevation
    // - Column bên trong với fillMaxHeight() + padding(16.dp)
    // - verticalArrangement = SpaceBetween
    // - Row header: emoji text + label text (onSurfaceVariant)
    // - Text value lớn (headlineMedium, Bold)
    // - Gọi trend() slot ở dưới
    // GỢI Ý: fillMaxHeight() phối hợp với IntrinsicSize.Max ở Row cha
    Box {}
}

/**
 * Trend indicator: icon mũi tên + percentage + màu
 *
 * TODO: [Nâng cao - Compose Phase] So sánh 2 cách rotate icon:
 *
 * // ❌ Cách 1: Modifier.rotate() — trigger cả 3 phases (Composition + Layout + Drawing)
 * Icon(modifier = Modifier.rotate(if (isPositive) 0f else 180f))
 *
 * // ✅ Cách 2: graphicsLayer — skip Composition + Layout, chỉ Drawing
 * Icon(modifier = Modifier.graphicsLayer { rotationZ = if (isPositive) 0f else 180f })
 *
 * Với static UI cả 2 cho kết quả giống nhau, nhưng trong animation thì graphicsLayer
 * hiệu quả hơn nhiều vì không trigger layout pass.
 */
@Composable
fun TrendIndicator(
    percentage: String,
    isPositive: Boolean,
    modifier: Modifier = Modifier
) {
    // TODO: Implement TrendIndicator
    // - Xác định color: tertiary nếu isPositive, error nếu ngược lại
    // - Row với verticalAlignment = CenterVertically, spacedBy(4.dp)
    // - Icon TrendingUp/TrendingDown size(16.dp) với tint = color
    // - GỢI Ý: Thay vì Modifier.rotate(), dùng Modifier.graphicsLayer { rotationZ = ... }
    //   → graphicsLayer chỉ trigger Drawing phase, tiết kiệm hơn cho animation
    // - Text percentage với color và fontWeight Medium
    Box {}
}

/**
 * Dashboard chứa 4 StatCards trong 2x2 grid
 *
 * TODO: [Nâng cao] Key implementation — IntrinsicSize.Max cho equal height
 *
 * Vấn đề: 2 card trong Row có content khác nhau → height khác nhau → layout không đều
 *
 * Solution:
 * Row(modifier = Modifier.height(IntrinsicSize.Max)) {
 *     StatCard(modifier = Modifier.weight(1f).fillMaxHeight())
 *     StatCard(modifier = Modifier.weight(1f).fillMaxHeight())
 * }
 *
 * Cơ chế: IntrinsicSize.Max đo height của card cao nhất, rồi set cho tất cả
 */
@Composable
fun StatsDashboard(
    stats: List<StatData>,
    modifier: Modifier = Modifier
) {
    // TODO: Implement StatsDashboard 2x2 grid
    // - Card ngoài với fillMaxWidth + padding(16.dp), elevation
    // - Column bên trong với padding(16.dp)
    // - Header Row: "📊 Dashboard" text (titleLarge, Bold) + "Today" text (bodySmall)
    // - Spacer(16.dp)
    // - Row 1 (stats[0] và stats[1]):
    //   → PHẢI dùng Modifier.height(IntrinsicSize.Max) trên Row
    //   → Mỗi StatCard: Modifier.weight(1f).fillMaxHeight()
    //   → Truyền TrendIndicator vào slot trend
    // - Spacer(12.dp) rồi Row 2 tương tự với stats[2] và stats[3]
    // GỢI Ý: Tại sao cần IntrinsicSize.Max?
    // → Compose đo "intrinsic height" của mỗi child, lấy max, constraint tất cả về height đó
    Box {}
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

@Preview(showBackground = true, name = "Dashboard — All Positive")
@Composable
private fun DashboardAllPositivePreview() {
    AppTheme {
        StatsDashboard(stats = allPositiveStats)
    }
}

@Preview(showBackground = true, name = "Dashboard — Mixed Trends")
@Composable
private fun DashboardMixedPreview() {
    AppTheme {
        StatsDashboard(stats = mixedStats)
    }
}

@Preview(
    showBackground = true,
    name = "Dashboard — Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun DashboardDarkPreview() {
    AppTheme {
        StatsDashboard(stats = mixedStats)
    }
}

// ─── Câu Hỏi Thảo Luận ───────────────────────────────────────────────────────
/*
 * Sau khi hoàn thành, thảo luận với nhóm:
 *
 * Q1: Tại sao IntrinsicSize.Max lại work? Compose tính height như thế nào?
 * Q2: Nếu không dùng IntrinsicSize.Max thì UI trông thế nào?
 * Q3: graphicsLayer vs Modifier.rotate() — khi nào dùng cái nào?
 * Q4: Slot API ở StatCard có lợi gì so với truyền thẳng TrendIndicator?
 */
