package com.apero.composetraining.session1.exercises

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apero.composetraining.common.AppTheme

/**
 * ⭐ BÀI TẬP 1: Greeting Card (Easy)
 *
 * Yêu cầu:
 * - Tạo 1 Card chứa: Icon + Text "Hello, [tên mình]!" + Button "Say Hi"
 * - Click button → đổi text thành "Hi back!"
 * - Dùng Column layout
 * - Modifier: padding 16dp, fillMaxWidth
 */

@Composable
fun GreetingCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // TODO: [Session 1] Bài tập 1 - Thêm Icon từ Material Icons (ví dụ: Icons.Default.Favorite)

            // TODO: [Session 1] Bài tập 1 - Thêm Text "Hello, [tên bạn]!" với fontSize 24sp, FontWeight.Bold
            // Gợi ý: Dùng remember + mutableStateOf để lưu trạng thái text

            // TODO: [Session 1] Bài tập 1 - Thêm Button "Say Hi"
            // Khi click → đổi text thành "Hi back!"
            // Gợi ý: var greeting by remember { mutableStateOf("Hello, ...!") }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GreetingCardPreview() {
    AppTheme { GreetingCard() }
}
