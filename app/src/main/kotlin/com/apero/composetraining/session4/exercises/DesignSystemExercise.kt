package com.apero.composetraining.session4.exercises

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apero.composetraining.common.AppTheme

/**
 * ⭐⭐⭐ BÀI TẬP 3: Design System (Challenge)
 *
 * Yêu cầu:
 * - data class AppColors (success, warning, error, aiPrimary, trending, gradient)
 * - CompositionLocalProvider
 * - AppDesignTheme wrapper
 * - Product Card với:
 *   - "AI RECOMMENDED" badge (aiPrimary color)
 *   - "TRENDING" pill (trending color)
 *   - Price text (custom typography)
 * - AppDesignTheme.colors.xxx syntax hoạt động
 */

// TODO: [Session 4] Bài tập 3 - Định nghĩa AppColors data class
// data class AppColors(
//     val success: Color = Color(0xFF4CAF50),
//     val warning: Color = Color(0xFFFFC107),
//     val error: Color = Color(0xFFF44336),
//     val aiPrimary: Color = Color(0xFF7C4DFF),
//     val trending: Color = Color(0xFFFF6D00),
//     val gradientStart: Color = Color(0xFF1A237E),
//     val gradientEnd: Color = Color(0xFF7C4DFF),
// )

// TODO: [Session 4] Bài tập 3 - Tạo CompositionLocal
// val LocalAppColors = staticCompositionLocalOf { AppColors() }

// TODO: [Session 4] Bài tập 3 - Tạo AppDesignTheme object
// object AppDesignTheme {
//     val colors: AppColors @Composable get() = LocalAppColors.current
// }

// TODO: [Session 4] Bài tập 3 - Tạo AppDesignThemeWrapper composable
// @Composable
// fun AppDesignThemeWrapper(content: @Composable () -> Unit) {
//     CompositionLocalProvider(LocalAppColors provides AppColors()) {
//         AppTheme { content() }
//     }
// }

@Composable
fun DesignSystemProductCard() {
    // TODO: [Session 4] Bài tập 3 - Wrap trong AppDesignThemeWrapper
    // Tạo Product Card với:
    // 1. "AI RECOMMENDED" badge — dùng AppDesignTheme.colors.aiPrimary
    // 2. Product image placeholder
    // 3. Product name + description
    // 4. "TRENDING" pill — dùng AppDesignTheme.colors.trending
    // 5. Price text — custom style

    Text("Bắt đầu code Design System ở đây!", modifier = Modifier.padding(16.dp))
}

@Preview(showBackground = true)
@Composable
private fun DesignSystemProductCardPreview() {
    AppTheme { DesignSystemProductCard() }
}
