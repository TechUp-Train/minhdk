package com.apero.composetraining.session1.demos

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apero.composetraining.common.AppTheme

// ============================================================
// DEMO 1: Basic Composables — Text, Image, Button
// Giới thiệu 3 composable cơ bản nhất
// ============================================================


@Composable
fun BasicComposablesDemo() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Text — composable cơ bản nhất, hiển thị văn bản
        Text(
            text = "Hello Compose! 👋",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        // Text với nhiều style
        Text(
            text = "Đây là text thường",
            style = MaterialTheme.typography.bodyLarge
        )

        // Icon — hiển thị icon từ Material Icons
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = "Star icon",
            tint = Color(0xFFFFD700), // Vàng gold
            modifier = Modifier.size(48.dp)
        )

        // Button — nút bấm với onClick callback
        var clickCount by remember { mutableIntStateOf(0) }
        Button(onClick = { clickCount++ }) {
            Text("Đã click $clickCount lần")
        }

        // OutlinedButton — button viền
        OutlinedButton(onClick = { }) {
            Text("Outlined Button")
        }

        // TextButton — button chỉ có text
        TextButton(onClick = { }) {
            Text("Text Button")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BasicComposablesDemoPreview() {
    AppTheme { BasicComposablesDemo() }
}

// ============================================================
// DEMO 2: Modifier Chains
// Modifier là cách "trang trí" composable — padding, size, background...
// THỨ TỰ MODIFIER RẤT QUAN TRỌNG!
// ============================================================

@Composable
fun ModifierChainsDemo() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Modifier Order Demo", fontWeight = FontWeight.Bold, fontSize = 20.sp)

        // padding TRƯỚC background → background KHÔNG bao gồm padding
        Text(
            text = "padding → background",
            modifier = Modifier
                .padding(16.dp)         // padding bên ngoài (trước background)
                .background(Color(0xFFE3F2FD))  // background chỉ bao phủ text
                .padding(8.dp)          // padding bên trong
        )

        // background TRƯỚC padding → background BAO GỒM padding
        Text(
            text = "background → padding",
            modifier = Modifier
                .background(Color(0xFFFCE4EC))  // background bao phủ cả padding
                .padding(16.dp)         // padding bên trong background
        )

        // Chain nhiều modifier
        Text(
            text = "Modifier chain dài",
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ModifierChainsDemoPreview() {
    AppTheme { ModifierChainsDemo() }
}

// ============================================================
// DEMO 3: Column, Row, Box — 3 layout cơ bản
// Column = xếp dọc, Row = xếp ngang, Box = xếp chồng
// ============================================================

@Composable
fun LayoutBasicsDemo() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Layout Basics", fontWeight = FontWeight.Bold, fontSize = 20.sp)

        // Column — xếp dọc
        Text("Column (dọc):", fontWeight = FontWeight.SemiBold)
        Column(
            modifier = Modifier
                .background(Color(0xFFE8F5E9))
                .padding(8.dp)
        ) {
            Text("Item 1")
            Text("Item 2")
            Text("Item 3")
        }

        // Row — xếp ngang
        Text("Row (ngang):", fontWeight = FontWeight.SemiBold)
        Row(
            modifier = Modifier
                .background(Color(0xFFFFF3E0))
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("A")
            Text("B")
            Text("C")
        }

        // Box — xếp chồng
        Text("Box (chồng):", fontWeight = FontWeight.SemiBold)
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color(0xFFE3F2FD))
        ) {
            Text(
                "Center",
                modifier = Modifier.align(Alignment.Center)
            )
            Text(
                "↗",
                modifier = Modifier.align(Alignment.TopEnd),
                fontSize = 20.sp
            )
        }

        // Row với weight — chia đều không gian
        Text("Row + Weight:", fontWeight = FontWeight.SemiBold)
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(Color(0xFFFFCDD2))
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) { Text("1/3") }
            Box(
                modifier = Modifier
                    .weight(2f)
                    .background(Color(0xFFC8E6C9))
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) { Text("2/3") }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LayoutBasicsDemoPreview() {
    AppTheme { LayoutBasicsDemo() }
}

// ============================================================
// DEMO 4: Profile Card — kết hợp tất cả concepts
// ============================================================

@Composable
fun ProfileCardDemo() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(12.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Nguyễn Văn An",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = "Android Developer tại Apero",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp
                )
            }
        }

        // Follow button
        Button(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
        ) {
            Text("Follow")
        }
    }
}

@Composable
fun JoeCard(
    modifier: Modifier = Modifier,
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.Green),
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                imageVector = Icons.Default.Person,
                contentDescription = "Avatar",
                modifier = Modifier
                    .padding(16.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            )
            Column {
                Text(text = "Jane Doe")
                Text(text = "Android Developer at Apero")
            }
        }
    }

}

@Preview
@Composable
private fun Preview() {
    JoeCard()
}

@Preview(showBackground = true)
@Composable
private fun ProfileCardDemoPreview() {
    AppTheme { ProfileCardDemo() }
}
