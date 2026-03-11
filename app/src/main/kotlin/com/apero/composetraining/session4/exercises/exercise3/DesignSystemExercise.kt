package com.apero.composetraining.session4.exercises.exercise3

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
 data class AppColors(
     val success: Color = Color(0xFF4CAF50),
     val warning: Color = Color(0xFFFFC107),
     val error: Color = Color(0xFFF44336),
     val aiPrimary: Color = Color(0xFF7C4DFF),
     val trending: Color = Color(0xFFFF6D00),
     val gradientStart: Color = Color(0xFF1A237E),
     val gradientEnd: Color = Color(0xFF7C4DFF)
 )

// TODO: [Session 4] Bài tập 3 - Tạo CompositionLocal
 val LocalAppColors = staticCompositionLocalOf { AppColors() }

// TODO: [Session 4] Bài tập 3 - Tạo AppDesignTheme object
 object AppDesignTheme {
     val colors: AppColors @Composable get() = LocalAppColors.current
 }

// TODO: [Session 4] Bài tập 3 - Tạo AppDesignThemeWrapper composable
 @Composable
 fun AppDesignThemeWrapper(content: @Composable () -> Unit) {
     CompositionLocalProvider(LocalAppColors provides AppColors()) {
         AppTheme { content() }
     }
 }

@Composable
fun DesignSystemProductCard() {
    AppDesignThemeWrapper {

        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Box(
                    modifier = Modifier
                        .background(
                            AppDesignTheme.colors.aiPrimary,
                            RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        "AI RECOMMENDED",
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Image")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Smart AI Speaker",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "Voice assistant with smart home integration.",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .background(
                            AppDesignTheme.colors.trending,
                            RoundedCornerShape(50)
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        "TRENDING",
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "$199",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DesignSystemProductCardPreview() {
    DesignSystemProductCard()
}
