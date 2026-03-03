package com.apero.composetraining.session5.exercises

import android.content.res.Configuration
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apero.composetraining.common.AppTheme

/**
 * ⭐⭐⭐⭐ BÀI TẬP NÂNG CAO BUỔI 5: Dynamic Color + Font Showcase
 *
 * Mô tả: Demo dynamic color (Android 12+) và Material 3 color roles/typography
 *
 * ┌──────────────────────────────────────────┐
 * │ Dynamic Color Showcase                   │
 * │ [Dynamic Color: ON ●] [Dark Mode: OFF ●] │
 * │                                          │
 * │ ⚠️ Requires Android 12+                  │
 * │                                          │
 * │ Color Roles (25 swatches)                │
 * │ ┌──────┬──────┬──────┬──────┬──────┐    │
 * │ │ Prim │ OnP  │ PrC  │ OnPC │ Sec  │    │
 * │ └──────┴──────┴──────┴──────┴──────┘    │
 * │ Typography Scale                         │
 * │ displayLarge: Aa                         │
 * │ headlineMedium: Aa                       │
 * └──────────────────────────────────────────┘
 *
 * Key concepts:
 * - dynamicLightColorScheme(context): lấy màu từ wallpaper Android 12+
 * - Build.VERSION.SDK_INT >= Build.VERSION_CODES.S: check API level
 * - MaterialTheme.colorScheme: tất cả 25 color roles của Material 3
 * - Typography scale: displayLarge → labelSmall (15 styles)
 */

// ─── Main Screen ──────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DynamicColorScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val systemInDark = isSystemInDarkTheme()

    // State: toggle dynamic color vs custom scheme
    var useDynamicColor by remember { mutableStateOf(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) }

    // State: toggle dark mode
    var isDarkMode by remember { mutableStateOf(systemInDark) }

    // Tính color scheme dựa theo toggles
    // dynamicLightColorScheme: lấy màu từ hình nền điện thoại (Android 12+ Monet)
    // Tại sao cần SDK_INT check?
    //   → dynamicLightColorScheme() chỉ có trên Android 12 (API 31 = S)
    //   → Gọi trên API thấp hơn → crash
    val selectedColorScheme = when {
        useDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (isDarkMode) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }
        isDarkMode -> darkColorScheme()
        else -> lightColorScheme()
    }

    // Áp dụng color scheme cho preview
    MaterialTheme(colorScheme = selectedColorScheme) {
        Surface(modifier = modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                // Header
                Text(
                    text = "Dynamic Color Showcase",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                )

                // Toggle controls
                ToggleControls(
                    useDynamicColor = useDynamicColor,
                    isDarkMode = isDarkMode,
                    onDynamicToggle = { useDynamicColor = it },
                    onDarkModeToggle = { isDarkMode = it },
                )

                // Warning nếu SDK < 12
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                    ApiWarningCard()
                } else if (useDynamicColor) {
                    // Info card khi dynamic color đang bật
                    DynamicColorInfoCard()
                }

                HorizontalDivider()

                // Color palette grid — all 25 M3 color roles
                Text(
                    text = "Material 3 Color Roles (25 roles)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                ColorPaletteGrid()

                HorizontalDivider()

                // Typography showcase
                Text(
                    text = "Typography Scale",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                TypographyShowcase()
            }
        }
    }
}

// ─── Toggle Controls ──────────────────────────────────────────────────────────

@Composable
private fun ToggleControls(
    useDynamicColor: Boolean,
    isDarkMode: Boolean,
    onDynamicToggle: (Boolean) -> Unit,
    onDarkModeToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text(
                        text = "Dynamic Color",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                    )
                    Text(
                        text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            "Android 12+ ✅"
                        } else {
                            "Requires Android 12 ❌"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.error
                        },
                    )
                }
                Switch(
                    checked = useDynamicColor,
                    onCheckedChange = onDynamicToggle,
                    // Disable nếu không support dynamic color
                    enabled = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S,
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Dark Mode",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                )
                Switch(
                    checked = isDarkMode,
                    onCheckedChange = onDarkModeToggle,
                )
            }
        }
    }
}

// ─── Warning Cards ────────────────────────────────────────────────────────────

@Composable
private fun ApiWarningCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
        ),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = "⚠️", style = MaterialTheme.typography.titleLarge)
            Column {
                Text(
                    text = "Dynamic Color not supported",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Your device runs Android ${Build.VERSION.SDK_INT}. Dynamic Color requires API 31+ (Android 12). Custom scheme is used instead.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                )
            }
        }
    }
}

@Composable
private fun DynamicColorInfoCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(text = "✨", style = MaterialTheme.typography.titleLarge)
            Column {
                Text(
                    text = "Dynamic Color Active",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Colors extracted from your wallpaper (Monet engine). Change wallpaper to see different palettes.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }
    }
}

// ─── Color Palette Grid ───────────────────────────────────────────────────────

/**
 * Hiển thị tất cả 25 Material 3 color roles dạng grid
 *
 * Material 3 có 5 key colors × 5 roles = 25 color roles:
 * Primary, Secondary, Tertiary, Error, Neutral × (color, onColor, container, onContainer, ...)
 */
@Composable
private fun ColorPaletteGrid(modifier: Modifier = Modifier) {
    // Định nghĩa tất cả 25 color roles
    val colorRoles = listOf(
        // Primary group
        ColorRole("primary", colorScheme.primary, colorScheme.onPrimary),
        ColorRole("onPrimary", colorScheme.onPrimary, colorScheme.primary),
        ColorRole("primaryContainer", colorScheme.primaryContainer, colorScheme.onPrimaryContainer),
        ColorRole("onPrimaryContainer", colorScheme.onPrimaryContainer, colorScheme.primaryContainer),
        ColorRole("inversePrimary", colorScheme.inversePrimary, colorScheme.primary),

        // Secondary group
        ColorRole("secondary", colorScheme.secondary, colorScheme.onSecondary),
        ColorRole("onSecondary", colorScheme.onSecondary, colorScheme.secondary),
        ColorRole("secondaryContainer", colorScheme.secondaryContainer, colorScheme.onSecondaryContainer),
        ColorRole("onSecondaryContainer", colorScheme.onSecondaryContainer, colorScheme.secondaryContainer),

        // Tertiary group
        ColorRole("tertiary", colorScheme.tertiary, colorScheme.onTertiary),
        ColorRole("onTertiary", colorScheme.onTertiary, colorScheme.tertiary),
        ColorRole("tertiaryContainer", colorScheme.tertiaryContainer, colorScheme.onTertiaryContainer),
        ColorRole("onTertiaryContainer", colorScheme.onTertiaryContainer, colorScheme.tertiaryContainer),

        // Error group
        ColorRole("error", colorScheme.error, colorScheme.onError),
        ColorRole("onError", colorScheme.onError, colorScheme.error),
        ColorRole("errorContainer", colorScheme.errorContainer, colorScheme.onErrorContainer),
        ColorRole("onErrorContainer", colorScheme.onErrorContainer, colorScheme.errorContainer),

        // Surface group
        ColorRole("surface", colorScheme.surface, colorScheme.onSurface),
        ColorRole("onSurface", colorScheme.onSurface, colorScheme.surface),
        ColorRole("surfaceVariant", colorScheme.surfaceVariant, colorScheme.onSurfaceVariant),
        ColorRole("onSurfaceVariant", colorScheme.onSurfaceVariant, colorScheme.surfaceVariant),
        ColorRole("outline", colorScheme.outline, colorScheme.surface),
        ColorRole("outlineVariant", colorScheme.outlineVariant, colorScheme.onSurface),
        ColorRole("inverseSurface", colorScheme.inverseSurface, colorScheme.inverseOnSurface),
        ColorRole("background", colorScheme.background, colorScheme.onBackground),
    )

    // Grid cố định 3 cột cho color swatches
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = modifier.height(420.dp), // Cần fixed height cho grid trong scroll
        userScrollEnabled = false, // Disable grid scroll vì đã có outer scroll
    ) {
        items(colorRoles) { role ->
            ColorSwatch(role = role)
        }
    }
}

data class ColorRole(
    val name: String,
    val color: Color,
    val textColor: Color,
)

@Composable
private fun ColorSwatch(
    role: ColorRole,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .height(60.dp)
            .clip(MaterialTheme.shapes.small)
            .background(role.color)
            .padding(4.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = role.name.take(12), // Truncate dài
            color = role.textColor,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 2,
        )
    }
}

// ─── Typography Showcase ──────────────────────────────────────────────────────

@Composable
private fun TypographyShowcase(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        // Material 3 có 5 groups × 3 sizes = 15 type styles
        TypographyItem("displayLarge", "The quick brown fox", MaterialTheme.typography.displayLarge)
        TypographyItem("displayMedium", "The quick brown fox", MaterialTheme.typography.displayMedium)
        TypographyItem("displaySmall", "The quick brown fox", MaterialTheme.typography.displaySmall)

        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

        TypographyItem("headlineLarge", "The quick brown fox", MaterialTheme.typography.headlineLarge)
        TypographyItem("headlineMedium", "The quick brown fox", MaterialTheme.typography.headlineMedium)
        TypographyItem("headlineSmall", "The quick brown fox jumps", MaterialTheme.typography.headlineSmall)

        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

        TypographyItem("titleLarge", "The quick brown fox jumps", MaterialTheme.typography.titleLarge)
        TypographyItem("titleMedium", "The quick brown fox jumps over", MaterialTheme.typography.titleMedium)
        TypographyItem("titleSmall", "The quick brown fox jumps over the lazy dog", MaterialTheme.typography.titleSmall)

        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

        TypographyItem("bodyLarge", "The quick brown fox jumps over the lazy dog", MaterialTheme.typography.bodyLarge)
        TypographyItem("bodyMedium", "The quick brown fox jumps over the lazy dog", MaterialTheme.typography.bodyMedium)
        TypographyItem("bodySmall", "The quick brown fox jumps over the lazy dog", MaterialTheme.typography.bodySmall)

        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

        TypographyItem("labelLarge", "LABEL LARGE", MaterialTheme.typography.labelLarge)
        TypographyItem("labelMedium", "LABEL MEDIUM", MaterialTheme.typography.labelMedium)
        TypographyItem("labelSmall", "LABEL SMALL", MaterialTheme.typography.labelSmall)
    }
}

@Composable
private fun TypographyItem(
    name: String,
    sample: String,
    textStyle: androidx.compose.ui.text.TextStyle,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.width(120.dp),
        )
        Text(
            text = sample,
            style = textStyle,
            maxLines = 1,
            modifier = Modifier.weight(1f),
        )
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true, name = "Dynamic Color - Light")
@Composable
private fun DynamicColorScreenPreview() {
    AppTheme {
        DynamicColorScreen()
    }
}

@Preview(
    showBackground = true,
    name = "Dynamic Color - Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun DynamicColorScreenDarkPreview() {
    AppTheme(darkTheme = true) {
        DynamicColorScreen()
    }
}
