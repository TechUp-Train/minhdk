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
    // State cho danh sách todo
    var todos by remember {
        mutableStateOf(
            listOf(
                TodoItem(1, "Design mockup"),
                TodoItem(2, "Code review"),
                TodoItem(3, "Write tests", isDone = true),
                TodoItem(4, "Deploy to staging"),
                TodoItem(5, "Update docs", isDone = true),
            ),
        )
    }

    // State cho input thêm todo mới
    var newTodoText by remember { mutableStateOf("") }

    // rememberLazyListState: track scroll position để show/hide FAB
    // Đây là cách đúng để đọc scroll state — KHÔNG dùng onScroll callback
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // derivedStateOf: chỉ recalculate khi firstVisibleItemIndex thay đổi
    // KHÔNG dùng listState.firstVisibleItemIndex trực tiếp trong composable
    // vì nó sẽ trigger recompose MỖI PIXEL scroll → performance issue
    val showFab by remember {
        derivedStateOf {
            // FAB visible khi ở đầu list (firstVisibleItemIndex = 0)
            // FAB ẩn khi scroll xuống
            listState.firstVisibleItemIndex == 0
        }
    }

    // Tách active và completed tasks
    val activeTodos = todos.filter { !it.isDone }
    val completedTodos = todos.filter { it.isDone }

    // Counter cho ID mới
    var nextId by remember { mutableStateOf(todos.size + 1) }

    Scaffold(
        floatingActionButton = {
            // FAB với AnimatedVisibility — ẩn khi scroll xuống
            AnimatedVisibility(
                visible = showFab,
                enter = fadeIn() + slideInVertically { it },
                exit = fadeOut() + slideOutVertically { it },
            ) {
                ExtendedFloatingActionButton(
                    onClick = {
                        if (newTodoText.isNotBlank()) {
                            todos = todos + TodoItem(
                                id = nextId++,
                                title = newTodoText.trim(),
                            )
                            newTodoText = ""
                        }
                    },
                    icon = { Icon(Icons.Default.Add, contentDescription = "Add") },
                    text = { Text("Add Task") },
                )
            }
        },
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            // Tiêu đề
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                Text(
                    text = "Animated Todo List",
                    style = MaterialTheme.typography.headlineMedium,
                )
                Text(
                    text = "animateItem() + stickyHeader + rememberLazyListState",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            // Input thêm todo
            AddTodoInput(
                value = newTodoText,
                onValueChange = { newTodoText = it },
                onAdd = {
                    if (newTodoText.isNotBlank()) {
                        // Thêm item — animateItem() sẽ tự animate slide in
                        todos = todos + TodoItem(id = nextId++, title = newTodoText.trim())
                        newTodoText = ""
                        // Scroll lên đầu để thấy item mới
                        coroutineScope.launch { listState.animateScrollToItem(0) }
                    }
                },
                modifier = Modifier.padding(horizontal = 16.dp),
            )

            Spacer(modifier = Modifier.height(8.dp))

            // LazyColumn với sticky headers
            LazyColumn(
                state = listState, // Gắn listState để track scroll
                contentPadding = PaddingValues(bottom = 80.dp), // Space cho FAB
                modifier = Modifier.fillMaxSize(),
            ) {
                // ─── Section: Active Tasks ──────────────────────────────────

                // stickyHeader: Header này sẽ "dính" khi scroll qua nó
                // Tại sao sticky? → Giúp user luôn biết mình đang ở section nào
                stickyHeader(key = "active_header") {
                    SectionHeader(title = "Active Tasks (${activeTodos.size})")
                }

                // animateItem() trong Compose 1.7+
                // Tự động animate: slide in khi add, slide out khi remove, move khi reorder
                // Không cần config gì thêm — Compose tự handle physics
                items(
                    items = activeTodos,
                    key = { it.id }, // Key BẮT BUỘC phải có để animateItem hoạt động đúng
                ) { todo ->
                    TodoRow(
                        todo = todo,
                        onToggle = {
                            todos = todos.map { if (it.id == todo.id) it.copy(isDone = !it.isDone) else it }
                        },
                        onDelete = {
                            todos = todos.filter { it.id != todo.id }
                        },
                        onMoveUp = {
                            val index = todos.indexOfFirst { it.id == todo.id }
                            if (index > 0) {
                                todos = todos.toMutableList().apply {
                                    val item = removeAt(index)
                                    add(index - 1, item)
                                }
                            }
                        },
                        onMoveDown = {
                            val index = todos.indexOfFirst { it.id == todo.id }
                            if (index < todos.size - 1) {
                                todos = todos.toMutableList().apply {
                                    val item = removeAt(index)
                                    add(index + 1, item)
                                }
                            }
                        },
                        // animateItem() — magic line!
                        // Compose tự calculate position change và animate từ vị trí cũ → mới
                        modifier = Modifier.animateItem(),
                    )
                }

                // ─── Section: Completed Tasks ───────────────────────────────

                if (completedTodos.isNotEmpty()) {
                    stickyHeader(key = "completed_header") {
                        SectionHeader(title = "Completed Tasks (${completedTodos.size})")
                    }

                    items(
                        items = completedTodos,
                        key = { it.id },
                    ) { todo ->
                        TodoRow(
                            todo = todo,
                            onToggle = {
                                todos = todos.map { if (it.id == todo.id) it.copy(isDone = !it.isDone) else it }
                            },
                            onDelete = {
                                todos = todos.filter { it.id != todo.id }
                            },
                            onMoveUp = { /* Completed items không reorder */ },
                            onMoveDown = {},
                            modifier = Modifier.animateItem(), // animateItem cho completed items
                        )
                    }
                }

                // Empty state
                if (todos.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(48.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = "No tasks yet!\nAdd one above 👆",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }
        }
    }
}

// ─── Section Header (Sticky) ──────────────────────────────────────────────────

@Composable
private fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )
    }
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
    // animateFloatAsState cho alpha khi item completed
    val alpha by animateFloatAsState(
        targetValue = if (todo.isDone) 0.5f else 1f,
        label = "todo_alpha",
    )

    ListItem(
        headlineContent = {
            Text(
                text = todo.title,
                textDecoration = if (todo.isDone) TextDecoration.LineThrough else null,
                modifier = Modifier.alpha(alpha),
            )
        },
        leadingContent = {
            // Checkbox để toggle done/undone
            Checkbox(
                checked = todo.isDone,
                onCheckedChange = { onToggle() },
            )
        },
        trailingContent = {
            Row {
                // Nút move up
                if (!todo.isDone) {
                    IconButton(onClick = onMoveUp, modifier = Modifier.size(36.dp)) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Move up",
                            modifier = Modifier.size(16.dp),
                        )
                    }
                    // Nút move down
                    IconButton(onClick = onMoveDown, modifier = Modifier.size(36.dp)) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Move down",
                            modifier = Modifier.size(16.dp),
                        )
                    }
                }
                // Nút delete
                IconButton(onClick = onDelete, modifier = Modifier.size(36.dp)) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }
        },
        modifier = modifier.background(
            if (todo.isDone) {
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            } else {
                MaterialTheme.colorScheme.surface
            },
        ),
    )
    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
}

// ─── Add Todo Input ───────────────────────────────────────────────────────────

@Composable
private fun AddTodoInput(
    value: String,
    onValueChange: (String) -> Unit,
    onAdd: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text("Add new task...") },
            modifier = Modifier.weight(1f),
            singleLine = true,
            shape = MaterialTheme.shapes.medium,
        )
        IconButton(
            onClick = onAdd,
            enabled = value.isNotBlank(),
        ) {
            Icon(
                imageVector = if (value.isNotBlank()) Icons.Default.Check else Icons.Default.Add,
                contentDescription = "Add task",
                tint = if (value.isNotBlank()) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.outline,
            )
        }
    }
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
