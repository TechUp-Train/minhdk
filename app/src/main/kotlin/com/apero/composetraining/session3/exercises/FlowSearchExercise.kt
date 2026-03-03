package com.apero.composetraining.session3.exercises

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.apero.composetraining.common.AppTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapLatest

/**
 * ⭐⭐⭐⭐ BÀI TẬP NÂNG CAO BUỔI 3: Real-time Search với Flow + collectAsStateWithLifecycle
 *
 * Mô tả: Search contacts theo tên với debouncing, async loading, lifecycle-aware collection
 *
 * ┌─────────────────────────────────┐
 * │ 🔍 Search contacts...           │  ← TextField search bar
 * ├─────────────────────────────────┤
 * │  ● Alice Johnson                │  ← Results (filtered)
 * │    alice@email.com              │
 * │  ● Bob Smith                    │
 * │    bob@email.com                │
 * └─────────────────────────────────┘
 *
 * Key concepts:
 * - snapshotFlow: chuyển Compose State → Flow để dùng Flow operators (debounce, distinctUntilChanged)
 * - produceState: chuyển async logic → Compose State (bridge từ coroutines về Compose)
 * - collectAsStateWithLifecycle: collect Flow nhưng tự PAUSE khi app vào background
 *   → Khác collectAsState: collectAsState vẫn collect khi background (tốn tài nguyên)
 */

// ─── Data Model ──────────────────────────────────────────────────────────────

data class Contact(
    val id: Int,
    val name: String,
    val email: String,
)

// 20 contacts giả để demo search
private val fakeContacts = listOf(
    Contact(1, "Alice Johnson", "alice@example.com"),
    Contact(2, "Bob Smith", "bob@example.com"),
    Contact(3, "Charlie Brown", "charlie@example.com"),
    Contact(4, "Diana Prince", "diana@example.com"),
    Contact(5, "Edward Norton", "edward@example.com"),
    Contact(6, "Fiona Apple", "fiona@example.com"),
    Contact(7, "George Miller", "george@example.com"),
    Contact(8, "Hannah Lee", "hannah@example.com"),
    Contact(9, "Ivan Drago", "ivan@example.com"),
    Contact(10, "Julia Roberts", "julia@example.com"),
    Contact(11, "Kevin Hart", "kevin@example.com"),
    Contact(12, "Laura Palmer", "laura@example.com"),
    Contact(13, "Michael Scott", "michael@example.com"),
    Contact(14, "Nancy Drew", "nancy@example.com"),
    Contact(15, "Oscar Wilde", "oscar@example.com"),
    Contact(16, "Petra Pan", "petra@example.com"),
    Contact(17, "Quinn Hughes", "quinn@example.com"),
    Contact(18, "Rachel Green", "rachel@example.com"),
    Contact(19, "Steve Rogers", "steve@example.com"),
    Contact(20, "Tina Turner", "tina@example.com"),
)

// ─── Search Result State ─────────────────────────────────────────────────────

// Sealed class để represent các trạng thái của search result
sealed class SearchResult {
    // Đang load (debounce chưa xong hoặc đang query)
    data object Loading : SearchResult()
    // Có kết quả
    data class Success(val contacts: List<Contact>) : SearchResult()
    // Không tìm thấy
    data object Empty : SearchResult()
}

// ─── Main Composable ─────────────────────────────────────────────────────────

/**
 * FlowSearchScreen - màn hình search chính
 *
 * Pattern: SearchScreen tự quản lý query state (stateful),
 * nhưng tách logic search ra produceState/snapshotFlow
 */
@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@Composable
fun FlowSearchScreen(modifier: Modifier = Modifier) {
    // State cho search query — Compose State bình thường
    var searchQuery by remember { mutableStateOf("") }

    // snapshotFlow: chuyển Compose State (searchQuery) → Flow
    // Tại sao dùng snapshotFlow thay vì observe trực tiếp?
    // → Để dùng được Flow operators: debounce, distinctUntilChanged, mapLatest
    // → Không thể dùng các operators này trực tiếp trên MutableState
    val searchResultState: SearchResult by produceState<SearchResult>(
        initialValue = SearchResult.Success(fakeContacts),
        key1 = searchQuery, // relaunch khi searchQuery thay đổi
    ) {
        // Bên trong produceState: chạy trong coroutine, có thể suspend
        // value = cách set kết quả vào State

        // Tạo Flow từ searchQuery state
        // snapshotFlow sẽ emit mỗi khi searchQuery thay đổi
        snapshotFlow { searchQuery }
            .debounce(300L) // Đợi user ngừng gõ 300ms mới search
            // → Tránh search mỗi keystroke, tiết kiệm resources
            .distinctUntilChanged() // Bỏ qua nếu query không đổi
            .mapLatest { query ->
                // mapLatest: cancel query cũ nếu query mới arrive
                value = SearchResult.Loading
                delay(300L) // Giả lập network call / database query

                val results = if (query.isEmpty()) {
                    fakeContacts // Không filter nếu query rỗng
                } else {
                    fakeContacts.filter { contact ->
                        contact.name.contains(query, ignoreCase = true)
                    }
                }

                if (results.isEmpty()) SearchResult.Empty else SearchResult.Success(results)
            }
            .collect { result ->
                value = result // Cập nhật State
            }
    }

    // Lưu ý: searchResultState bên trên dùng produceState trực tiếp
    // Cách khác: tạo StateFlow trong ViewModel rồi collectAsStateWithLifecycle
    // Demo collectAsStateWithLifecycle ở dưới (cần ViewModel)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        // Tiêu đề
        Text(
            text = "Contact Search",
            style = MaterialTheme.typography.headlineMedium,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Dùng snapshotFlow + produceState + collectAsStateWithLifecycle",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Search TextField
        SearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Hiển thị kết quả dựa theo state
        when (val result = searchResultState) {
            is SearchResult.Loading -> {
                // Loading indicator ở giữa màn hình
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

            is SearchResult.Empty -> {
                // Empty state khi không tìm thấy
                EmptySearchState(query = searchQuery)
            }

            is SearchResult.Success -> {
                // Hiển thị kết quả với counter
                Text(
                    text = "${result.contacts.size} contacts found",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
                Spacer(modifier = Modifier.height(8.dp))
                ContactResultList(contacts = result.contacts)
            }
        }
    }
}

// ─── Search Bar Component ─────────────────────────────────────────────────────

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = { Text("Search contacts...") },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Search icon")
        },
        trailingIcon = {
            // Hiện nút Clear khi có text
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Default.Clear, contentDescription = "Clear search")
                }
            }
        },
        singleLine = true,
        shape = MaterialTheme.shapes.large,
    )
}

// ─── Contact List ─────────────────────────────────────────────────────────────

@Composable
private fun ContactResultList(
    contacts: List<Contact>,
    modifier: Modifier = Modifier,
) {
    // Key = contact.id để Compose track đúng item khi list thay đổi
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        items(
            items = contacts,
            key = { it.id }, // Key quan trọng cho animation và performance
        ) { contact ->
            ContactItem(contact = contact)
        }
    }
}

@Composable
private fun ContactItem(
    contact: Contact,
    modifier: Modifier = Modifier,
) {
    ListItem(
        headlineContent = {
            Text(
                text = contact.name,
                style = MaterialTheme.typography.bodyLarge,
            )
        },
        supportingContent = {
            Text(
                text = contact.email,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        leadingContent = {
            // Avatar placeholder
            Surface(
                shape = MaterialTheme.shapes.extraLarge,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(40.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(24.dp),
                    )
                }
            }
        },
        modifier = modifier,
    )
}

// ─── Empty State ──────────────────────────────────────────────────────────────

@Composable
private fun EmptySearchState(
    query: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No contacts found",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "No results for \"$query\"",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline,
        )
    }
}

// ─── collectAsStateWithLifecycle Demo ────────────────────────────────────────

/**
 * Demo: Sự khác biệt giữa collectAsState và collectAsStateWithLifecycle
 *
 * collectAsState:
 * - Collect liên tục, kể cả khi app ở background
 * - Tốn battery, tốn resources
 *
 * collectAsStateWithLifecycle:
 * - Tự PAUSE khi Activity/Fragment stop (vào background)
 * - Tự RESUME khi Activity/Fragment start lại
 * - KHUYẾN NGHỊ dùng thay collectAsState trong production
 *
 * Cú pháp:
 * val uiState by viewModel.uiState.collectAsStateWithLifecycle()
 * //                                  ↑ Lifecycle-aware, tự pause/resume
 *
 * Cần import:
 * import androidx.lifecycle.compose.collectAsStateWithLifecycle
 * Dependency: androidx.lifecycle:lifecycle-runtime-compose
 */
@Composable
private fun CollectAsStateDemo(modifier: Modifier = Modifier) {
    // Ví dụ sử dụng collectAsStateWithLifecycle với StateFlow từ ViewModel:
    // val uiState by viewModel.searchResults.collectAsStateWithLifecycle()

    // Hoặc với bất kỳ Flow nào:
    // val names by someFlow.collectAsStateWithLifecycle(initialValue = emptyList())

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "📚 collectAsStateWithLifecycle",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "✅ Pause khi background\n✅ Resume khi foreground\n✅ Tiết kiệm battery",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
            )
        }
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true, name = "Flow Search - Light")
@Composable
private fun FlowSearchScreenPreview() {
    AppTheme {
        FlowSearchScreen()
    }
}

@Preview(
    showBackground = true,
    name = "Flow Search - Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun FlowSearchScreenDarkPreview() {
    AppTheme(darkTheme = true) {
        FlowSearchScreen()
    }
}

@Preview(showBackground = true, name = "Contact Item Preview")
@Composable
private fun ContactItemPreview() {
    AppTheme {
        ContactItem(
            contact = Contact(1, "Alice Johnson", "alice@example.com"),
        )
    }
}

@Preview(showBackground = true, name = "Empty State Preview")
@Composable
private fun EmptyStatePreview() {
    AppTheme {
        EmptySearchState(query = "xyz")
    }
}
