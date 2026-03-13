package com.apero.composetraining.session7.exercises

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apero.composetraining.common.AppTheme

/**
 * ⭐⭐⭐ BÀI TẬP 3: Performance Audit (Challenge)
 *
 * Đây là "bad code" có NHIỀU performance issues.
 * Nhiệm vụ:
 * 1. Identify 5 performance issues
 * 2. Fix từng issue
 * 3. Giải thích tại sao fix đó improve performance
 * 4. Bonus: Thêm @Stable annotation + derivedStateOf
 */

// ❌ ISSUE 1: data class có var fields → UNSTABLE
// TODO: [Session 7] Bài tập 3 - Fix: đổi var thành val, thêm @Stable nếu cần
data class BadItem(
    var id: Int,        // ← var → unstable!
    var title: String,  // ← var → unstable!
    var count: Int      // ← var → unstable!
)

@Composable
fun BadPerformanceScreen() {
    var searchQuery by remember { mutableStateOf("") }

    val items = remember {
        (1..100).map { BadItem(it, "Item $it", 0) }
    }

    // ❌ ISSUE 2: Heavy computation chạy MỖI LẦN recompose, không dùng derivedStateOf
    // TODO: [Session 7] Bài tập 3 - Fix: wrap trong derivedStateOf
    val filteredItems = items.filter {
        it.title.contains(searchQuery, ignoreCase = true)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Performance Audit", style = MaterialTheme.typography.headlineMedium)
        Text("Tìm và fix 5 performance issues!", color = MaterialTheme.colorScheme.error)

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // ❌ ISSUE 3: LazyColumn KHÔNG có key → state mất khi reorder
        // TODO: [Session 7] Bài tập 3 - Fix: thêm key = { it.id }
        LazyColumn {
            items(filteredItems) { item ->
                // ❌ ISSUE 4: Tạo object mới mỗi lần recompose
                // TODO: [Session 7] Bài tập 3 - Fix: dùng remember hoặc lambda reference
                BadItemCard(
                    item = item,
                    onClick = { /* no-op */ }
                )
            }
        }
    }
}

@Composable
fun BadItemCard(item: BadItem, onClick: () -> Unit) {
    // ❌ ISSUE 5: Tạo padding Modifier MỚI mỗi lần — nên extract ra ngoài
    // TODO: [Session 7] Bài tập 3 - Fix: dùng Modifier constant hoặc remember
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            Text(item.title, modifier = Modifier.weight(1f))
            Text("Count: ${item.count}")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BadPerformanceScreenPreview() {
    AppTheme { BadPerformanceScreen() }
}
