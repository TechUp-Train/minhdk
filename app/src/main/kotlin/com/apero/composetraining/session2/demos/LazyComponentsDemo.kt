package com.apero.composetraining.session2.demos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apero.composetraining.common.AppTheme
import com.apero.composetraining.common.SampleData

// ============================================================
// DEMO 1: Column vs LazyColumn
// Column render TẤT CẢ items → lag nếu nhiều items
// LazyColumn chỉ render items trên màn hình → mượt
// ============================================================

@Composable
fun ColumnVsLazyColumnDemo() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("LazyColumn — chỉ render items nhìn thấy", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        // LazyColumn tương tự RecyclerView — chỉ compose items đang hiển thị
        LazyColumn(
            modifier = Modifier.height(300.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(
                items = SampleData.todos,
                key = { it.id } // KEY quan trọng! Giúp Compose track item khi list thay đổi
            ) { todo ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(checked = todo.isDone, onCheckedChange = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(todo.title)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ColumnVsLazyColumnDemoPreview() {
    AppTheme { ColumnVsLazyColumnDemo() }
}

// ============================================================
// DEMO 2: LazyRow — scroll ngang (ví dụ: trending movies)
// ============================================================

@Composable
fun LazyRowDemo() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("🔥 Trending Movies", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(SampleData.movies.take(8), key = { it.id }) { movie ->
                Card(
                    modifier = Modifier.width(140.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column {
                        // Placeholder cho poster
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🎬", fontSize = 40.sp)
                        }
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(
                                movie.title,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                maxLines = 1
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color(0xFFFFD700),
                                    modifier = Modifier.size(14.dp)
                                )
                                Text("${movie.rating}", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LazyRowDemoPreview() {
    AppTheme { LazyRowDemo() }
}

// ============================================================
// DEMO 3: LazyVerticalGrid — lưới responsive
// ============================================================

@Composable
fun LazyGridDemo() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Photo Grid — Adaptive(150.dp)", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Adaptive(150.dp), // Tự tính số cột dựa trên screen width
            modifier = Modifier.height(400.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(SampleData.photos, key = { it.id }) { photo ->
                Card(shape = RoundedCornerShape(8.dp)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .background(MaterialTheme.colorScheme.secondaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("📷", fontSize = 24.sp)
                            Text(
                                photo.title,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LazyGridDemoPreview() {
    AppTheme { LazyGridDemo() }
}

// ============================================================
// DEMO 4: Scaffold — layout chuẩn với TopBar, FAB, BottomBar
// ============================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldDemo() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🎬 Movies") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { }) {
                Text("+", fontSize = 24.sp)
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(SampleData.movies.take(5), key = { it.id }) { movie ->
                ListItem(
                    headlineContent = { Text(movie.title, fontWeight = FontWeight.SemiBold) },
                    supportingContent = { Text("${movie.year} • ${movie.genre}") },
                    trailingContent = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, null, tint = Color(0xFFFFD700), modifier = Modifier.size(16.dp))
                            Text("${movie.rating}")
                        }
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ScaffoldDemoPreview() {
    AppTheme { ScaffoldDemo() }
}
