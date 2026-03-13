package com.apero.composetraining.session4.demos

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apero.composetraining.common.AppTheme

// ============================================================
// DEMO 1: MaterialTheme — dùng color/typography từ theme
// KHÔNG hardcode color! Luôn dùng MaterialTheme.colorScheme.xxx
// ============================================================

@Composable
fun MaterialThemeDemo() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("MaterialTheme Colors", fontWeight = FontWeight.Bold)

        // Dùng color từ theme — tự động đổi khi switch dark/light
        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
            Text(
                "Primary Container",
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
            Text(
                "Secondary Container",
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }

        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)) {
            Text(
                "Tertiary Container",
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
        }

        // Typography
        Text("Typography Styles:", fontWeight = FontWeight.Bold)
        Text("headlineMedium", style = MaterialTheme.typography.headlineMedium)
        Text("titleLarge", style = MaterialTheme.typography.titleLarge)
        Text("bodyLarge", style = MaterialTheme.typography.bodyLarge)
        Text("labelLarge", style = MaterialTheme.typography.labelLarge)
    }
}

@Preview(showBackground = true)
@Composable
private fun MaterialThemeDemoPreview() {
    AppTheme { MaterialThemeDemo() }
}

// ============================================================
// DEMO 2: Dark Mode Toggle
// ============================================================

@Composable
fun DarkModeToggleDemo() {
    var isDark by remember { mutableStateOf(false) }

    // Wrap content trong theme với darkTheme parameter
    AppTheme(darkTheme = isDark, dynamicColor = false) {
        Surface(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Dark Mode", fontWeight = FontWeight.Bold)
                    Switch(checked = isDark, onCheckedChange = { isDark = it })
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Card tự đổi màu theo theme
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Hà Nội, 32°C",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            "Sunny ☀️",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Xem chi tiết")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DarkModeToggleDemoPreview() {
    AppTheme { DarkModeToggleDemo() }
}

// ============================================================
// DEMO 3: CompositionLocal — "biến global nhưng scoped"
// Truyền data xuống tree KHÔNG cần pass qua parameter
// ============================================================

// Tạo CompositionLocal cho custom spacing
data class AppSpacing(
    val small: Int = 4,
    val medium: Int = 8,
    val large: Int = 16,
    val extraLarge: Int = 24
)

val LocalAppSpacing = staticCompositionLocalOf { AppSpacing() }

@Composable
fun CompositionLocalDemo() {
    // Cung cấp custom spacing cho toàn bộ subtree
    CompositionLocalProvider(LocalAppSpacing provides AppSpacing(small = 8, medium = 16, large = 24, extraLarge = 32)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("CompositionLocal Demo", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            // Component con dùng spacing KHÔNG cần nhận qua parameter
            SpacedContent()
        }
    }
}

@Composable
private fun SpacedContent() {
    // Đọc giá trị từ CompositionLocal — không cần pass qua params!
    val spacing = LocalAppSpacing.current

    Column(verticalArrangement = Arrangement.spacedBy(spacing.medium.dp)) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Text("spacing.small = ${spacing.small}dp", modifier = Modifier.padding(spacing.small.dp))
        }
        Card(modifier = Modifier.fillMaxWidth()) {
            Text("spacing.medium = ${spacing.medium}dp", modifier = Modifier.padding(spacing.medium.dp))
        }
        Card(modifier = Modifier.fillMaxWidth()) {
            Text("spacing.large = ${spacing.large}dp", modifier = Modifier.padding(spacing.large.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CompositionLocalDemoPreview() {
    AppTheme { CompositionLocalDemo() }
}
