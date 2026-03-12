package com.apero.composetraining

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.compose.runtime.Immutable
import com.apero.composetraining.common.AppTheme
import com.apero.composetraining.common.SessionDetail
import com.apero.composetraining.common.SessionList

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                MainNavigation()
            }
        }
    }
}

// ✅ @Immutable — Compose biết object này không bao giờ thay đổi
// → SessionCard skip recompose kể cả khi parent recompose (vd: dialog mở/đóng)
@Immutable
private data class SessionInfo(
    val number: Int,
    val title: String,
    val description: String,
    val emoji: String
)

// Thông tin 7 sessions (chỉ 7, không duplicate)
private val sessions = listOf(
    SessionInfo(1, "Compose Fundamentals", "Text, Image, Button, Modifier, Column/Row/Box", "🧱"),
    SessionInfo(2, "Layouts", "LazyColumn, LazyRow, BoxWithConstraints, Custom Layout", "📐"),
    SessionInfo(3, "State & Recomposition", "remember, State hoisting, derivedStateOf, snapshotFlow", "🔄"),
    SessionInfo(4, "Lazy Layouts", "LazyColumn, LazyGrid, keys, animateItem", "⚡"),
    SessionInfo(5, "Theming & Styling", "MaterialTheme, Custom colors, Dark mode, Typography", "🎨"),
    SessionInfo(6, "Navigation", "NavHost, Navigation 3, Bottom nav, Deep links", "🧭"),
    SessionInfo(7, "Animation & Testing", "animate*AsState, AnimatedVisibility, ComposeTestRule", "✨"),
)

@Composable
fun MainNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = SessionList, modifier = modifier) {
        composable<SessionList> {
            SessionListScreen(onSessionClick = { number ->
                navController.navigate(SessionDetail(number))
            })
        }
        composable<SessionDetail> { backStackEntry ->
            val detail = backStackEntry.toRoute<SessionDetail>()
            SessionDetailScreen(
                sessionNumber = detail.sessionNumber,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionListScreen(modifier: Modifier = Modifier, onSessionClick: (Int) -> Unit = {}) {
    // ✅ Cheese pattern: selectedSession thay vì Boolean flag
    // → null = không có dialog, non-null = hiện dialog session đó
    var selectedSession by remember { mutableStateOf<SessionInfo?>(null) }

    // ✅ Stable lambda — capture setter (MutableState), không capture value
    // → reference không đổi qua các recompose → SessionCard không recompose vì lambda
    val onCardClick = remember<(SessionInfo) -> Unit> {
        { session -> selectedSession = session }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🎓 Compose Training", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = androidx.compose.ui.graphics.Color.Transparent
                )
            )
        },
        modifier = modifier
    ) { padding ->
        // ✅ Tách LazyColumn vào composable riêng — scope recompose độc lập
        // → selectedSession thay đổi → SessionListScreen recompose
        //   NHƯNG SessionList nhận cùng params → skip recompose luôn
        SessionList(
            sessions = sessions,
            onSessionClick = onCardClick,
            modifier = Modifier.padding(padding)
        )

        // ✅ Dialog nằm ngoài SessionList scope — chỉ Dialog recompose khi selectedSession thay đổi
        // → LazyColumn bên trong SessionList KHÔNG bao giờ recompose vì dialog mở/đóng
        selectedSession?.let { session ->
            SessionPreviewDialog(
                session = session,
                onNavigate = {
                    onSessionClick(session.number)
                    selectedSession = null
                },
                onDismiss = { selectedSession = null }
            )
        }
    }
}

// ✅ Composable riêng → scope recompose độc lập với dialog state
@Composable
private fun SessionList(
    sessions: List<SessionInfo>,
    onSessionClick: (SessionInfo) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // ✅ items thay vì itemsIndexed (không cần index)
        // ✅ key = id ổn định → chỉ recompose item thực sự thay đổi
        items(sessions, key = { it.number }) { session ->
            SessionCard(session = session, onClick = { onSessionClick(session) })
        }
    }
}

// ✅ Dialog tách biệt hoàn toàn — không liên quan đến LazyColumn scope
@Composable
private fun SessionPreviewDialog(
    session: SessionInfo,
    onNavigate: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = MaterialTheme.shapes.extraLarge,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = session.emoji,
                    style = MaterialTheme.typography.displaySmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Session ${session.number}: ${session.title}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = session.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Đóng")
                    }
                    Button(
                        onClick = onNavigate,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Vào học →")
                    }
                }
            }
        }
    }
}

@Composable
private fun SessionCard(session: SessionInfo, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = session.emoji,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(end = 16.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Session ${session.number}",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = session.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = session.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Go to session",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionDetailScreen(
    sessionNumber: Int,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {}
) {
    val session = sessions.find { it.number == sessionNumber } ?: return

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${session.emoji} Session $sessionNumber") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Back",
                            // Flip for back arrow
                            modifier = Modifier.padding(0.dp)
                        )
                    }
                }
            )
        },
        modifier = modifier
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = session.title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = session.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Text(
                    text = "📖 Demos",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Mở file demos/ trong package session$sessionNumber để xem code demo.\nChạy @Preview để xem kết quả.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "✏️ Exercises",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Mở file exercises/ trong package session$sessionNumber.\nTìm các TODO comment và hoàn thành bài tập.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("💡 Tip", fontWeight = FontWeight.Bold)
                        Text(
                            "Dùng Android Studio Preview để xem kết quả ngay. " +
                                    "Không cần build app mỗi lần thay đổi code!",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SessionListScreenPreview() {
    AppTheme {
        SessionListScreen()
    }
}

@Preview(showBackground = true)
@Composable
private fun SessionDetailScreenPreview() {
    AppTheme {
        SessionDetailScreen(sessionNumber = 1)
    }
}
