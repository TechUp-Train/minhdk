package ui.view.themes

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = AppColors.Primary,
            background = AppColors.Background,
            surface = AppColors.Surface,
            onPrimary = Color.White,
            onBackground = AppColors.TextPrimary,
            onSurface = AppColors.TextPrimary
        ),
        typography = typography(),
        shapes = AppShapes,
        content = content
    )
}