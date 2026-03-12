package com.apero.composetraining.session1.exercises

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apero.composetraining.common.AppTheme

/**
 * ⭐⭐⭐ BÀI TẬP 3: Mini Profile Screen (Challenge)
 *
 * Yêu cầu:
 * - Header: Avatar + Name + "Edit Profile" button
 * - Stats Row: 3 cột (Posts | Followers | Following) dùng Row + weight
 * - Bio section: Text nhiều dòng
 * - Action buttons: "Message" + "Follow" ngang hàng
 * - Bonus: Thêm Spacer hợp lý, đúng alignment
 */

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // === HEADER ===
        // TODO: [Session 1] Bài tập 3 - Tạo header với Avatar (80dp, CircleShape) + Column(Name, "Edit Profile" button)
        // Gợi ý: Row(verticalAlignment = Alignment.CenterVertically)

        Spacer(modifier = Modifier.height(24.dp))

        // === STATS ROW ===
        // TODO: [Session 1] Bài tập 3 - Tạo Row với 3 cột: Posts (128) | Followers (1.2K) | Following (380)
        // Gợi ý: Mỗi cột là Column(horizontalAlignment = CenterHorizontally, modifier = Modifier.weight(1f))
        // Số to + bold, label nhỏ + màu nhạt

        Spacer(modifier = Modifier.height(16.dp))

        // === BIO ===
        // TODO: [Session 1] Bài tập 3 - Thêm Text bio nhiều dòng
        // Ví dụ: "Android Developer 📱\nLover of clean code ✨\nHà Nội, Việt Nam 🇻🇳"

        Spacer(modifier = Modifier.height(24.dp))

        // === ACTION BUTTONS ===
        // TODO: [Session 1] Bài tập 3 - Tạo Row chứa 2 button ngang hàng: "Message" (Outlined) + "Follow" (Filled)
        // Gợi ý: Dùng Modifier.weight(1f) cho mỗi button, spacedBy(8.dp) cho Row
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    AppTheme { ProfileScreen() }
}
