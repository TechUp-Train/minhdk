package com.apero.composetraining

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

// Thông tin 7 sessions
private val sessions = listOf(
    SessionInfo(1, "Compose Fundamentals", "Text, Image, Button, Modifier, Column/Row/Box", "🧱"),
    SessionInfo(2, "Layouts & Lazy Components", "LazyColumn, LazyRow, LazyGrid, Scaffold", "📐"),
    SessionInfo(3, "State & Recomposition", "remember, State hoisting, derivedStateOf", "🔄"),
    SessionInfo(4, "Theming & Styling", "MaterialTheme, Custom colors, Dark mode", "🎨"),
    SessionInfo(5, "Navigation & Side Effects", "NavHost, Arguments, Bottom nav, Deep links", "🧭"),
    SessionInfo(6, "Animation & Gesture", "animate*AsState, AnimatedVisibility, Gestures", "✨"),
    SessionInfo(7, "Testing & Performance", "ComposeTestRule, @Stable, Performance audit", "🧪"),
    SessionInfo(8, "Testing & Performance", "ComposeTestRule, @Stable, Performance audit", "🧪"),
    SessionInfo(9, "Testing & Performance", "ComposeTestRule, @Stable, Performance audit", "🧪"),
    SessionInfo(10, "Testing & Performance", "ComposeTestRule, @Stable, Performance audit", "🧪"),
    SessionInfo(16, "Testing & Performance", "ComposeTestRule, @Stable, Performance audit", "🧪"),
    SessionInfo(15, "Testing & Performance", "ComposeTestRule, @Stable, Performance audit", "🧪"),
    SessionInfo(14, "Testing & Performance", "ComposeTestRule, @Stable, Performance audit", "🧪"),
    SessionInfo(13, "Testing & Performance", "ComposeTestRule, @Stable, Performance audit", "🧪"),
    SessionInfo(12, "Testing & Performance", "ComposeTestRule, @Stable, Performance audit", "🧪"),
    SessionInfo(11, "Testing & Performance", "ComposeTestRule, @Stable, Performance audit", "🧪"),
)

private data class SessionInfo(
    val number: Int,
    val title: String,
    val description: String,
    val emoji: String
)

@Composable
fun MainNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = SessionList) {
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
fun SessionListScreen(onSessionClick: (Int) -> Unit = {}) {
    var enableDialog = remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🎓 Compose Training", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = androidx.compose.ui.graphics.Color.Transparent
                )
            )
        },
        bottomBar = {
            TopAppBar(
                title = { Text("🎓 Compose Training", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = androidx.compose.ui.graphics.Color.Transparent
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(sessions, key = { _, s -> s.number }) { _, session ->
                SessionCard(session = session, onClick = {
                    onSessionClick(session.number)
                    enableDialog.value = true
                })
            }
        }
    }
    if (enableDialog.value) {
        Dialog(
            onDismissRequest = { enableDialog.value = false},
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight(0.6f)
                    .padding(20.dp),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(sessions, key = { _, s -> s.number }) { _, session ->
                    SessionCard(session = session, onClick = { onSessionClick(session.number) })
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
fun SessionDetailScreen(sessionNumber: Int, onBack: () -> Unit = {}) {
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
        }
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
