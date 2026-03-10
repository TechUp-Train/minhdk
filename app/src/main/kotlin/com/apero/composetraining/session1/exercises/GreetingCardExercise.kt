package com.apero.composetraining.session1.exercises

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    var content by remember { mutableStateOf("Hello, Doan Khac Minh") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
            )

            Text(
                text = content,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            )


            Button(
                onClick = {
                    content = "Hi back"
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = OceanBlue
                ),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(10.dp),
                modifier = Modifier.wrapContentSize()
            ) {
                Text(
                    text = "Say hi",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GreetingCardPreview() {
    AppTheme { GreetingCard() }
}
