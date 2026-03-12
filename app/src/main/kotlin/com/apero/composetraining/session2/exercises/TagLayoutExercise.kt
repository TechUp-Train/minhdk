package com.apero.composetraining.session2.exercises

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apero.composetraining.common.AppTheme

/**
 * ⭐⭐⭐⭐ BÀI TẬP 4: Custom Tag Layout (Nâng cao — 45 phút)
 *
 * Yêu cầu:
 * - Dùng Layout composable (custom layout) để wrap tags sang hàng mới khi hết width
 * - KHÔNG được dùng FlowRow có sẵn của Compose
 * - Tags wraps tự nhiên như text flow
 * - 8dp spacing giữa các tag, 8dp spacing giữa các hàng
 *
 * Tiêu chí:
 * - Implement đúng Layout { measurables, constraints -> ... }
 * - MeasurePolicy tự tính x, y của từng tag theo available width
 * - Tags không bị cắt ở cuối dòng
 * - Spacing đúng (8dp horizontal, 8dp vertical)
 *
 * Gợi ý implementation:
 * Layout(content = content) { measurables, constraints ->
 *     val placeables = measurables.map { it.measure(constraints.copy(minWidth = 0)) }
 *     val gap = 8.dp.roundToPx()
 *     var x = 0; var y = 0; var rowHeight = 0
 *
 *     for (placeable in placeables) {
 *         if (x + placeable.width > constraints.maxWidth && x > 0) {
 *             x = 0; y += rowHeight + gap; rowHeight = 0  // xuống hàng mới
 *         }
 *         rowHeight = maxOf(rowHeight, placeable.height)
 *         x += placeable.width + gap
 *     }
 *
 *     layout(width = constraints.maxWidth, height = y + rowHeight) {
 *         // place mỗi placeable theo vị trí đã tính
 *     }
 * }
 *
 * Lưu ý: Phải tính lại x, y 2 lần — lần 1 để biết tổng height, lần 2 để place.
 */

// TODO: [Session 2] Bài tập 4 - Implement TagsLayout composable dùng Layout
// @Composable
// fun TagsLayout(
//     tags: List<String>,
//     modifier: Modifier = Modifier
// ) {
//     Layout(
//         content = {
//             tags.forEach { tag ->
//                 // Render từng tag thành AssistChip hoặc SuggestionChip
//                 SuggestionChip(onClick = {}, label = { Text(tag) })
//             }
//         },
//         modifier = modifier
//     ) { measurables, constraints ->
//         // TODO: implement MeasurePolicy
//         // 1. Measure tất cả placeables
//         // 2. Tính vị trí (x, y) của từng placeable (wrap khi hết width)
//         // 3. Tính tổng height
//         // 4. layout(maxWidth, totalHeight) { place mỗi placeable }
//     }
// }

@Composable
fun TagLayoutScreen() {
    val tags = listOf(
        "Android", "Jetpack Compose", "Kotlin", "UI Design",
        "Material3", "Jetpack", "Mobile Dev", "Coroutines",
        "Flow", "MVVM", "Clean Architecture", "Hilt"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Custom Tag Layout", style = MaterialTheme.typography.headlineSmall)
        Text(
            "Implement TagsLayout dùng Layout composable (không dùng FlowRow)",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        HorizontalDivider()

        // TODO: Thay placeholder bằng TagsLayout(tags = tags)
        // Placeholder: hiển thị tags theo hàng ngang cố định
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Placeholder (chưa wrap đúng cách):",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelMedium)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.horizontalScroll(rememberScrollState())
            ) {
                tags.forEach { tag ->
                    SuggestionChip(onClick = {}, label = { Text(tag) })
                }
            }
        }

        HorizontalDivider()
        Text(
            "Sau khi implement TagsLayout, tags sẽ tự wrap xuống hàng mới khi hết width ↑",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TagLayoutScreenPreview() {
    AppTheme { TagLayoutScreen() }
}
