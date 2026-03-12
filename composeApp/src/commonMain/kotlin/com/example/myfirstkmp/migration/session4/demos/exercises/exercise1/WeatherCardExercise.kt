package com.example.myfirstkmp.migration.session4.demos.exercises.exercise1

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myfirstkmp.theme.AppTheme

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

//@Composable
//fun WeatherCardScreen() {
//    // TODO: [Session 4] Bài tập 1 - Tạo state cho dark mode toggle
//    // var isDark by remember { mutableStateOf(false) }
//
//    // TODO: [Session 4] Bài tập 1 - Wrap trong AppTheme(darkTheme = isDark, dynamicColor = false)
//
//    Column(modifier = Modifier.padding(16.dp)) {
//        // TODO: [Session 4] Bài tập 1 - Row với Text "Dark Mode" + Switch
//
//        // TODO: [Session 4] Bài tập 1 - Card với RoundedCornerShape(16.dp) chứa:
//        // - Text "Hà Nội" (titleLarge, colorScheme.primary)
//        // - Text "32°C" (headlineMedium, colorScheme.onSurface)
//        // - Text "Sunny ☀️" (bodyLarge, colorScheme.onSurfaceVariant)
//        // KHÔNG hardcode color — dùng MaterialTheme.colorScheme.xxx
//
//        // Placeholder
//        Text("Bắt đầu code Weather Card ở đây!", modifier = Modifier.padding(16.dp))
//    }
//}

@Composable
fun WeatherCardScreen() {
    var isDark by remember { mutableStateOf(false) }

    AppTheme(
        darkTheme = isDark,
        dynamicColor = false
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Dark Mode",
                    modifier = Modifier.weight(1f)
                )

                Switch(
                    checked = isDark,
                    onCheckedChange = { isDark = it }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = "Hà Nội",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "32°C",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Sunny ☀️",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WeatherCardScreenPreview() {
    AppTheme { WeatherCardScreen() }
}
