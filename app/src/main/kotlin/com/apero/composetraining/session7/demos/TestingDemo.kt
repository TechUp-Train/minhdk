package com.apero.composetraining.session7.demos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apero.composetraining.common.AppTheme

// ============================================================
// DEMO 1: Testable Counter — có testTag cho mỗi element
// ComposeTestRule sẽ tìm elements bằng testTag hoặc text
// ============================================================

@Composable
fun TestableCounter() {
    var count by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .testTag("counter_screen"), // testTag cho screen
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Testable Counter", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        // testTag giúp test tìm được element
        Text(
            text = "$count",
            fontSize = 48.sp,
            modifier = Modifier.testTag("count_text")
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = { if (count > 0) count-- },
                modifier = Modifier.testTag("decrement_button")
            ) {
                Text("-")
            }
            Button(
                onClick = { count++ },
                modifier = Modifier.testTag("increment_button")
            ) {
                Text("+")
            }
            OutlinedButton(
                onClick = { count = 0 },
                modifier = Modifier.testTag("reset_button")
            ) {
                Text("Reset")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TestableCounterPreview() {
    AppTheme { TestableCounter() }
}

// ============================================================
// DEMO 2: @Stable annotation — giảm unnecessary recomposition
// ============================================================

// ❌ UNSTABLE — có var field → Compose không biết khi nào data thay đổi
data class UnstableItem(
    var id: Int,     // var → unstable!
    var title: String
)

// ✅ STABLE — tất cả fields là val → Compose biết data immutable
@androidx.compose.runtime.Stable
data class StableItem(
    val id: Int,     // val → stable
    val title: String
)

@Composable
fun StabilityDemo() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("@Stable Demo", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "var fields → Compose recompose MỌI LÚC\n" +
                "val fields + @Stable → Compose chỉ recompose khi data thực sự đổi",
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Stable items — ít recomposition hơn
        val stableItems = remember {
            (1..5).map { StableItem(it, "Item $it") }
        }

        LazyColumn {
            items(stableItems, key = { it.id }) { item ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
                    Text(item.title, modifier = Modifier.padding(12.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StabilityDemoPreview() {
    AppTheme { StabilityDemo() }
}

// ============================================================
// DEMO 3: Performance — remember heavy computation
// ============================================================

@Composable
fun PerformanceDemo() {
    var query by remember { mutableStateOf("") }

    // ❌ BÀD: heavy computation chạy MỖI LẦN recompose
    // val badResult = (1..10000).filter { it.toString().contains(query) }

    // ✅ GOOD: remember + derivedStateOf — chỉ tính khi query đổi
    val goodResult by remember {
        derivedStateOf {
            if (query.isBlank()) emptyList()
            else (1..1000).filter { it.toString().contains(query) }.take(20)
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Performance Demo", fontWeight = FontWeight.Bold)
        Text("Tìm số chứa query:", style = MaterialTheme.typography.bodySmall)

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Search number") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text("Tìm thấy: ${goodResult.size} kết quả (hiển thị 20)")

        LazyColumn(modifier = Modifier.height(200.dp)) {
            items(goodResult) { number ->
                Text("• $number", modifier = Modifier.padding(vertical = 2.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PerformanceDemoPreview() {
    AppTheme { PerformanceDemo() }
}
