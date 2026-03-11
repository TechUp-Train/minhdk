package com.apero.composetraining.session4.exercises.exercise2

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apero.composetraining.common.AppTheme

/**
 * ⭐⭐ BÀI TẬP 2: Custom Theme (Medium)
 *
 * Yêu cầu:
 * - Định nghĩa lightColorScheme + darkColorScheme riêng
 * - Custom Typography (có thể dùng system font)
 * - Tạo wrapper MyAppTheme { content }
 * - Áp dụng lên 2 screens: Home + Detail
 * - Consistent color usage (không hardcode hex trong composable)
 * - isSystemInDarkTheme() tự switch
 */


@Composable
fun CustomThemeHomeScreen() {
    Lesson2AppTheme() {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = "Home Screen",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "This is the home screen using custom theme.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
fun CustomThemeDetailScreen() {
    Lesson2AppTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Text(
                text = "Detail Screen",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Detail screen with custom theme.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomThemeHomeScreenPreview() {
    CustomThemeHomeScreen()
}
