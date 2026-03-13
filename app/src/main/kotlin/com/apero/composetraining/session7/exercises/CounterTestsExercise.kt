package com.apero.composetraining.session7.exercises

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apero.composetraining.common.AppTheme

/**
 * ⭐ BÀI TẬP 1: Counter Tests (Easy)
 *
 * Viết 5 UI tests cho Counter app (từ Session 3):
 * 1. Initial state = 0
 * 2. Click "+" → count = 1
 * 3. Click "-" from 0 → count vẫn = 0
 * 4. Click "+" 3 lần → count = 3
 * 5. Click "Reset" → count = 0
 *
 * File test nằm ở: app/src/androidTest/kotlin/.../CounterTests.kt
 *
 * Dùng: composeTestRule, onNodeWithText, performClick, assertIsDisplayed
 */

// Component cần test — dùng lại TestableCounter từ demo
// Xem session7/demos/TestingDemo.kt

@Composable
fun CounterTestsInfo() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Counter Tests", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Mở file test tại:")
        Text(
            "app/src/androidTest/.../session7/CounterTests.kt",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Viết 5 tests:", style = MaterialTheme.typography.titleMedium)
        Text("1. Initial state = 0")
        Text("2. Click '+' → count = 1")
        Text("3. Click '-' from 0 → count vẫn = 0")
        Text("4. Click '+' 3 lần → count = 3")
        Text("5. Click 'Reset' → count = 0")
    }
}

@Preview(showBackground = true)
@Composable
private fun CounterTestsInfoPreview() {
    AppTheme { CounterTestsInfo() }
}
