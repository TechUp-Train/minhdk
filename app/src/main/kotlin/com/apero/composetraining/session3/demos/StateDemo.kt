package com.apero.composetraining.session3.demos

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.Immutable
import com.apero.composetraining.common.AppTheme
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

// ============================================================
// DEMO 1: Counter KHÔNG có remember — BỊ LỖI!
// Mỗi lần recomposition, biến bị reset về 0
// ============================================================

@Composable
fun BrokenCounterDemo() {
    // ⚠️ BUG: Không dùng remember → mỗi lần recompose, count reset về 0
    var count = 0 // KHÔNG BAO GIỜ THAY ĐỔI trên UI!

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("❌ Counter BROKEN (không remember)", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
        Spacer(modifier = Modifier.height(8.dp))
        Text("$count", fontSize = 48.sp)
        Button(onClick = { count++ }) {
            Text("Click me (không hoạt động!)")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BrokenCounterDemoPreview() {
    AppTheme { BrokenCounterDemo() }
}

// ============================================================
// DEMO 2: Counter CÓ remember — HOẠT ĐỘNG!
// remember giữ giá trị qua các lần recomposition
// ============================================================

@Composable
fun WorkingCounterDemo() {
    // ✅ remember giữ state qua recomposition
    var count by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("✅ Counter WORKING (có remember)", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(8.dp))
        Text("$count", fontSize = 48.sp)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { if (count > 0) count-- }) { Text("-") }
            Button(onClick = { count++ }) { Text("+") }
            OutlinedButton(onClick = { count = 0 }) { Text("Reset") }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WorkingCounterDemoPreview() {
    AppTheme { WorkingCounterDemo() }
}

// ============================================================
// DEMO 3: State Hoisting — tách state ra khỏi composable
// Component CON stateless → dễ test, dễ reuse
// ============================================================

@Composable
fun StateHoistingDemo() {
    // State nằm ở PARENT (hoisted)
    var count by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("State Hoisting Demo", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        // Component con KHÔNG giữ state — chỉ nhận value + callbacks
        StatelessCounter(
            count = count,
            onIncrement = { count++ },
            onDecrement = { if (count > 0) count-- },
            onReset = { count = 0 }
        )
    }
}

// Stateless composable — dễ test, dễ reuse, dễ preview
@Composable
fun StatelessCounter(
    count: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onReset: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("$count", fontSize = 48.sp, fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = onDecrement) { Text("-") }
            Button(onClick = onIncrement) { Text("+") }
            OutlinedButton(onClick = onReset) { Text("Reset") }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StateHoistingDemoPreview() {
    AppTheme { StateHoistingDemo() }
}

// ============================================================
// DEMO 4: rememberSaveable — giữ state qua configuration change
// ============================================================

@Composable
fun RememberSaveableDemo() {
    // rememberSaveable: state survive rotation, process death
    var count by rememberSaveable { mutableIntStateOf(0) }

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("rememberSaveable Demo", fontWeight = FontWeight.Bold)
        Text("Xoay màn hình — count vẫn giữ!", style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(8.dp))
        Text("$count", fontSize = 48.sp)
        Button(onClick = { count++ }) { Text("Tăng count") }
    }
}

@Preview(showBackground = true)
@Composable
private fun RememberSaveableDemoPreview() {
    AppTheme { RememberSaveableDemo() }
}

// ============================================================
// DEMO 5: derivedStateOf — tính toán derived value hiệu quả
// Chỉ recompose khi KẾT QUẢ thay đổi, không phải khi INPUT thay đổi
// ============================================================

@Composable
fun DerivedStateDemo() {
    var items by remember { mutableStateOf(listOf("Apple", "Banana", "Cherry", "Date", "Elderberry")) }
    var query by remember { mutableStateOf("") }

    // derivedStateOf: chỉ recompute khi query hoặc items thay đổi
    val filteredItems by remember {
        derivedStateOf {
            if (query.isBlank()) items
            else items.filter { it.contains(query, ignoreCase = true) }
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("derivedStateOf Demo", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Search") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text("Kết quả: ${filteredItems.size} items")

        filteredItems.forEach { item ->
            Text("• $item", modifier = Modifier.padding(vertical = 2.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DerivedStateDemoPreview() {
    AppTheme { DerivedStateDemo() }
}

// ============================================================
// DEMO 6: snapshotFlow — chuyển Compose State thành Flow
// Use case phổ biến: debounce search query
// ============================================================

@OptIn(kotlinx.coroutines.FlowPreview::class)
@Composable
fun SnapshotFlowDemo() {
    var searchQuery by remember { mutableStateOf("") }
    var debouncedQuery by remember { mutableStateOf("") }

    // snapshotFlow đọc Compose State bên trong block, emit khi state thay đổi
    // → Từ đó dùng được các Flow operators: debounce, distinctUntilChanged, mapLatest...
    LaunchedEffect(Unit) {
        snapshotFlow { searchQuery }          // emit mỗi khi searchQuery thay đổi
            .debounce(500L)                   // đợi 500ms user ngừng gõ
            .distinctUntilChanged()            // bỏ qua nếu value không thay đổi
            .collect { query: String ->
                debouncedQuery = query         // chỉ update sau khi debounce
            }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("snapshotFlow + debounce Demo", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Gõ để search...") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text("Giá trị NGAY LÚC GÕ: \"$searchQuery\"",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error)
        Text("Giá trị SAU DEBOUNCE (500ms): \"$debouncedQuery\"",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary)
        Text(
            "→ API call chỉ xảy ra với giá trị debounced",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SnapshotFlowDemoPreview() {
    AppTheme { SnapshotFlowDemo() }
}

// ============================================================
// DEMO 7: @Stable / @Immutable — tối ưu recomposition
// ============================================================

// ❌ KHÔNG có annotation: Compose coi List<> là unstable → không skip recompose
data class UnstableUiState(
    val title: String,
    val items: List<String>  // List<T> mặc định UNSTABLE theo Compose compiler
)

// ✅ CÓ @Immutable: Compose biết class này không đổi sau creation → CÓ THỂ skip recompose
@Immutable
data class StableUiState(
    val title: String,
    val items: List<String>  // Cùng là List nhưng đã được "bảo lãnh" bởi @Immutable
)

@Composable
fun StableAnnotationDemo() {
    // Demo giải thích @Stable vs @Immutable
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("@Stable / @Immutable Demo", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "❌ List<T> trong data class → Compose KHÔNG thể skip recompose\n" +
                "   vì List không đảm bảo equals() đúng với mọi implementation",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "✅ @Immutable trên data class → Compose TIN rằng\n" +
                "   • Tất cả public properties đều val\n" +
                "   • Giá trị không thay đổi sau construction\n" +
                "   → CÓ THỂ skip recompose khi tham chiếu không đổi",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "@Stable vs @Immutable:\n" +
                "• @Immutable: properties KHÔNG ĐỔI sau creation (mạnh hơn)\n" +
                "• @Stable: properties có thể đổi NHƯNG đúng equals() (linh hoạt hơn)",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StableAnnotationDemoPreview() {
    AppTheme { StableAnnotationDemo() }
}
