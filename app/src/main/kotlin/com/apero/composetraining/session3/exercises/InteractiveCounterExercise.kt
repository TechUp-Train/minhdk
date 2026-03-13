package com.apero.composetraining.session3.exercises

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apero.composetraining.common.AppTheme

/**
 * ⭐ BÀI TẬP 1: Interactive Counter (Easy)
 *
 * Yêu cầu:
 * - Text hiển thị count (fontSize 48sp)
 * - Row: Button "−" (disabled khi count = 0) | Button "+" | Button "Reset"
 * - rememberSaveable: xoay màn hình count vẫn giữ
 * - State Hoisting: tách thành Counter (stateless) + CounterScreen (stateful)
 *
 * Tiêu chí:
 * - Compile pass
 * - Xoay màn hình → state giữ nguyên
 * - Counter composable chỉ nhận count + callbacks (không có state bên trong)
 *
 * Gợi ý:
 * - fun Counter(count: Int, onIncrement: () -> Unit, onDecrement: () -> Unit, onReset: () -> Unit)
 * - fun CounterScreen() { var count by rememberSaveable { ... }; Counter(count, ...) }
 */

// TODO: [Session 3] Bài tập 1 - Tạo Counter composable STATELESS
// Params: count: Int, onIncrement: () -> Unit, onDecrement: () -> Unit, onReset: () -> Unit
// Layout:
//   - Text(count.toString(), fontSize = 48.sp) ở giữa
//   - Row chứa 3 Button: "−" (enabled = count > 0), "+", "Reset"

// TODO: [Session 3] Bài tập 1 - Tạo CounterScreen composable STATEFUL
// - var count by rememberSaveable { mutableIntStateOf(0) }
// - Gọi Counter(...) và truyền state + callbacks

@Composable
fun InteractiveCounterScreen() {
    // TODO: Xóa placeholder này và gọi CounterScreen() đã implement ở trên
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Interactive Counter", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))
        Text("0", fontSize = 48.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {}, enabled = false) { Text("−") }
            Button(onClick = {}) { Text("+") }
            OutlinedButton(onClick = {}) { Text("Reset") }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun InteractiveCounterScreenPreview() {
    AppTheme { InteractiveCounterScreen() }
}
