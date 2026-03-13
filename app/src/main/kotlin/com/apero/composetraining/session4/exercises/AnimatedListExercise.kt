package com.apero.composetraining.session4.exercises

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apero.composetraining.common.AppTheme
import kotlinx.coroutines.launch

/**
 * ⭐⭐⭐⭐ BÀI TẬP NÂNG CAO BUỔI 4: Animated Todo List
 *
 * Mô tả: LazyColumn với animations khi thêm/xóa/reorder items
 *
 * ┌───────────────────────────────────────┐
 * │ Animated Todo List                    │
 * ├───────────────────────────────────────┤
 * │ ── Active Tasks ─────────────────── │  ← stickyHeader
 * │ □ Design mockup          [↑][↓][🗑] │
 * │ □ Code review            [↑][↓][🗑] │
 * ├───────────────────────────────────────┤
 * │ ── Completed Tasks ─────────────── │  ← stickyHeader
 * │ ✓ Write tests            [↑][↓][🗑] │
 * └───────────────────────────────────────┘
 *                                  [+ FAB]  ← ẩn khi scroll xuống
 *
 * Key concepts:
 * - animateItem(): Compose 1.7+ API, tự động animate placement khi list thay đổi
 * - stickyHeader {}: Header dính trên cùng khi scroll qua section
 * - rememberLazyListState() + derivedStateOf: track scroll direction → FAB show/hide
 */

// ─── Data Model ──────────────────────────────────────────────────────────────

data class TodoItem(
    val id: Int,
    val title: String,
    val isDone: Boolean = false,
)

// ─── Main Screen ──────────────────────────────────────────────────────────────

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AnimatedListScreen(modifier: Modifier = Modifier) {
    // TODO: Implement AnimatedListScreen
    // 1. State setup:
    //    - var todos by remember { mutableStateOf(listOf(...initial items...)) }
    //    - var newTodoText by remember { mutableStateOf("") }
    //    - val listState = rememberLazyListState()
    //    - val coroutineScope = rememberCoroutineScope()
    //    - var nextId by remember { mutableStateOf(todos.size + 1) }
    //
    // 2. derivedStateOf cho FAB visibility:
    //    val showFab by remember {
    //        derivedStateOf { listState.firstVisibleItemIndex == 0 }
    //    }
    //    GỢI Ý: Tại sao dùng derivedStateOf thay vì đọc trực tiếp?
    //    → Đọc trực tiếp → recompose MỖI PIXEL scroll → performance issue
    //    → derivedStateOf chỉ recalculate khi firstVisibleItemIndex thay đổi
    //
    // 3. Tách active và completed:
    //    val activeTodos = todos.filter { !it.isDone }
    //    val completedTodos = todos.filter { it.isDone }
    //
    // 4. Scaffold với FAB:
    //    floatingActionButton = {
    //        AnimatedVisibility(
    //            visible = showFab,
    //            enter = fadeIn() + slideInVertically { it },
    //            exit = fadeOut() + slideOutVertically { it }
    //        ) { ExtendedFloatingActionButton(...) }
    //    }
    //
    // 5. Column bên trong: Header + AddTodoInput + LazyColumn
    //
    // 6. LazyColumn với stickyHeader + items(key = { it.id }):
    //    - stickyHeader(key = "active_header") { SectionHeader(...) }
    //    - items(activeTodos, key = { it.id }) { todo →
    //        TodoRow(modifier = Modifier.animateItem())  ← MAGIC LINE!
    //      }
    //    - Tương tự cho completed section
    //
    // GỢI Ý: animateItem() tự động animate:
    // - slide in khi add
    // - slide out khi remove
    // - move khi reorder
    // KEY BẮT BUỘC để animateItem hoạt động đúng!
    Box {}
}

// ─── Section Header (Sticky) ──────────────────────────────────────────────────

@Composable
private fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
) {
    // TODO: Implement SectionHeader
    // - Surface(fillMaxWidth, surfaceVariant color)
    // - Text title (labelLarge, Bold, primary, padding horizontal=16 vertical=8)
    Box {}
}

// ─── Todo Row ─────────────────────────────────────────────────────────────────

@Composable
private fun TodoRow(
    todo: TodoItem,
    onToggle: () -> Unit,
    onDelete: () -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // TODO: Implement TodoRow
    // 1. animateFloatAsState cho alpha khi completed:
    //    val alpha by animateFloatAsState(
    //        targetValue = if (todo.isDone) 0.5f else 1f,
    //        label = "todo_alpha"
    //    )
    //
    // 2. ListItem với:
    //    - headlineContent: Text todo.title với textDecoration = LineThrough nếu isDone
    //    - leadingContent: Checkbox(checked = isDone, onCheckedChange = { onToggle() })
    //    - trailingContent: Row với IconButton(ArrowBack/Up), IconButton(ArrowForward/Down), IconButton(Delete, error tint)
    //    - modifier với background (surfaceVariant.alpha(0.5f) nếu done, surface nếu không)
    //
    // 3. HorizontalDivider ở cuối
    Box {}
}

// ─── Add Todo Input ───────────────────────────────────────────────────────────

@Composable
private fun AddTodoInput(
    value: String,
    onValueChange: (String) -> Unit,
    onAdd: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // TODO: Implement AddTodoInput
    // - Row(fillMaxWidth, CenterVertically, spacedBy=8.dp)
    // - OutlinedTextField(weight(1f), singleLine, placeholder = "Add new task...")
    // - IconButton(onClick = onAdd, enabled = value.isNotBlank())
    //   Icon = Check nếu có text, Add nếu không; tint = primary nếu enabled, outline nếu không
    Box {}
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true, name = "Animated List - Light")
@Composable
private fun AnimatedListScreenPreview() {
    AppTheme {
        AnimatedListScreen()
    }
}

@Preview(
    showBackground = true,
    name = "Animated List - Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun AnimatedListScreenDarkPreview() {
    AppTheme(darkTheme = true) {
        AnimatedListScreen()
    }
}

@Preview(showBackground = true, name = "Todo Row Preview")
@Composable
private fun TodoRowPreview() {
    AppTheme {
        Column {
            TodoRow(
                todo = TodoItem(1, "Design mockup", isDone = false),
                onToggle = {},
                onDelete = {},
                onMoveUp = {},
                onMoveDown = {},
            )
            TodoRow(
                todo = TodoItem(2, "Write tests", isDone = true),
                onToggle = {},
                onDelete = {},
                onMoveUp = {},
                onMoveDown = {},
            )
        }
    }
}
