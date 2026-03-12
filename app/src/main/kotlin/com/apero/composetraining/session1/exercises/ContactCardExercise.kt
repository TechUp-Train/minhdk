package com.apero.composetraining.session1.exercises

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apero.composetraining.common.AppTheme

/**
 * ⭐⭐ BÀI TẬP 2: Contact Card (Medium)
 *
 * Yêu cầu:
 * - Row chứa: Image (CircleShape, 60dp) + Column (Name bold + Bio regular)
 * - Button "Follow" ở dưới, fillMaxWidth
 * - Card có elevation, rounded corner 12dp
 * - Modifier chain ít nhất 3 modifier
 * - Dùng cả Column, Row, Box
 * - @Preview với showBackground = true
 */

@Composable
fun ContactCard(
    name: String = "Nguyễn Văn An",
    bio: String = "Android Developer tại Apero"
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row {
                // TODO: [Session 1] Bài tập 2 - Thêm avatar (Icon hoặc Image)
                // Gợi ý: Icon với modifier .size(60.dp).clip(CircleShape).background(...)

                // TODO: [Session 1] Bài tập 2 - Thêm Column chứa Name (Bold) và Bio (Regular)
                // Gợi ý: Column(modifier = Modifier.weight(1f).padding(start = 12.dp))
            }

            Spacer(modifier = Modifier.height(12.dp))

            // TODO: [Session 1] Bài tập 2 - Thêm Button "Follow" với fillMaxWidth
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ContactCardPreview() {
    AppTheme { ContactCard() }
}
