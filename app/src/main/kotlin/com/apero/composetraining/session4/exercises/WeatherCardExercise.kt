package com.apero.composetraining.session4.exercises

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apero.composetraining.common.AppTheme

/**
 * ⭐ BÀI TẬP 1: Weather Card (Easy)
 *
 * Yêu cầu:
 * - Dùng MaterialTheme.colorScheme.primary/surface/onSurface
 * - Typography: headlineMedium cho temperature, bodyLarge cho description
 * - Shape: RoundedCornerShape(16.dp)
 * - Toggle dark mode bằng Switch
 * - Data: hardcode "Hanoi, 32°C, Sunny"
 */

@Composable
fun WeatherCardScreen() {
    // TODO: [Session 4] Bài tập 1 - Tạo state cho dark mode toggle
    // var isDark by remember { mutableStateOf(false) }

    // TODO: [Session 4] Bài tập 1 - Wrap trong AppTheme(darkTheme = isDark, dynamicColor = false)

    Column(modifier = Modifier.padding(16.dp)) {
        // TODO: [Session 4] Bài tập 1 - Row với Text "Dark Mode" + Switch

        // TODO: [Session 4] Bài tập 1 - Card với RoundedCornerShape(16.dp) chứa:
        // - Text "Hà Nội" (titleLarge, colorScheme.primary)
        // - Text "32°C" (headlineMedium, colorScheme.onSurface)
        // - Text "Sunny ☀️" (bodyLarge, colorScheme.onSurfaceVariant)
        // KHÔNG hardcode color — dùng MaterialTheme.colorScheme.xxx

        // Placeholder
        Text("Bắt đầu code Weather Card ở đây!", modifier = Modifier.padding(16.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun WeatherCardScreenPreview() {
    AppTheme { WeatherCardScreen() }
}
