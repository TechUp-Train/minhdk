package com.apero.composetraining.session4.exercises

import androidx.compose.foundation.layout.*
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

// TODO: [Session 4] Bài tập 2 - Định nghĩa custom light color scheme
// private val MyLightColors = lightColorScheme(
//     primary = Color(0xFF...),
//     secondary = Color(0xFF...),
//     ...
// )

// TODO: [Session 4] Bài tập 2 - Định nghĩa custom dark color scheme
// private val MyDarkColors = darkColorScheme(...)

// TODO: [Session 4] Bài tập 2 - Tạo custom Typography
// private val MyTypography = Typography(
//     headlineMedium = TextStyle(...),
//     bodyLarge = TextStyle(...),
//     ...
// )

// TODO: [Session 4] Bài tập 2 - Tạo MyAppTheme wrapper composable
// @Composable
// fun MyAppTheme(
//     darkTheme: Boolean = isSystemInDarkTheme(),
//     content: @Composable () -> Unit
// ) {
//     MaterialTheme(
//         colorScheme = if (darkTheme) MyDarkColors else MyLightColors,
//         typography = MyTypography,
//         content = content
//     )
// }

@Composable
fun CustomThemeHomeScreen() {
    // TODO: [Session 4] Bài tập 2 - Wrap trong MyAppTheme, tạo Home screen đơn giản
    // Card + Text dùng MaterialTheme.colorScheme + typography
    Text("Bắt đầu code Custom Theme ở đây!", modifier = Modifier.padding(16.dp))
}

@Composable
fun CustomThemeDetailScreen() {
    // TODO: [Session 4] Bài tập 2 - Detail screen cũng dùng MyAppTheme
    Text("Detail screen với Custom Theme", modifier = Modifier.padding(16.dp))
}

@Preview(showBackground = true)
@Composable
private fun CustomThemeHomeScreenPreview() {
    AppTheme { CustomThemeHomeScreen() }
}
