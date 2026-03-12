package com.apero.composetraining.session3.exercises

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apero.composetraining.common.AppTheme
import com.apero.composetraining.common.SampleData

/**
 * ⭐⭐⭐ BÀI TẬP 3: Search & Filter Contacts (Challenge)
 *
 * Yêu cầu:
 * - TextField search bar (state hoisted lên SearchFilterScreen)
 * - Switch "Chỉ hiện active contacts" (dùng Contact.isFavorite làm active flag)
 * - LazyColumn: danh sách contacts đã filter (dùng derivedStateOf)
 * - Empty state: hiển thị "Không tìm thấy liên hệ nào" khi list trống
 * - rememberSaveable cho search query (survive xoay màn hình)
 * - snapshotFlow để debounce search 300ms (tránh filter mỗi keystroke)
 *
 * Tiêu chí:
 * - derivedStateOf đúng cách cho filter logic
 * - snapshotFlow + debounce(300ms) + distinctUntilChanged đúng cách
 * - UDF pattern: state xuống, events lên
 * - rememberSaveable cho query và toggle
 *
 * Gợi ý snapshotFlow:
 * LaunchedEffect(Unit) {
 *     snapshotFlow { searchQuery }     // chuyển Compose State → Flow
 *         .debounce(300L)              // đợi 300ms user ngừng gõ
 *         .distinctUntilChanged()      // bỏ qua nếu giá trị không đổi
 *         .collect { debouncedQuery = it }
 * }
 */

@Composable
fun SearchFilterScreen() {
    val contacts = SampleData.contacts

    // TODO: [Session 3] Bài tập 3 - Tạo state cho search query (rememberSaveable)
    // var searchQuery by rememberSaveable { mutableStateOf("") }

    // TODO: [Session 3] Bài tập 3 - Tạo state cho debounced query (dùng với snapshotFlow)
    // var debouncedQuery by remember { mutableStateOf("") }

    // TODO: [Session 3] Bài tập 3 - Tạo state cho toggle active contacts
    // var showActiveOnly by rememberSaveable { mutableStateOf(false) }

    // TODO: [Session 3] Bài tập 3 - snapshotFlow debounce 300ms
    // LaunchedEffect(Unit) {
    //     snapshotFlow { searchQuery }
    //         .debounce(300L)
    //         .distinctUntilChanged()
    //         .collect { debouncedQuery = it }
    // }

    // TODO: [Session 3] Bài tập 3 - Filter contacts bằng derivedStateOf
    // val filteredContacts by remember {
    //     derivedStateOf {
    //         contacts.filter { contact ->
    //             val matchesQuery = contact.name.contains(debouncedQuery, ignoreCase = true)
    //             val matchesActive = if (showActiveOnly) contact.isFavorite else true
    //             matchesQuery && matchesActive
    //         }
    //     }
    // }

    Column(modifier = Modifier.fillMaxSize()) {
        // TODO: [Session 3] Bài tập 3 - OutlinedTextField cho search
        // value = searchQuery, onValueChange = { searchQuery = it }
        // label = { Text("Tìm kiếm...") }, modifier = fillMaxWidth + padding(16.dp)

        // TODO: [Session 3] Bài tập 3 - Row chứa Text + Switch
        // Text("Chỉ hiện active contacts"), Switch(checked = showActiveOnly, ...)

        // TODO: [Session 3] Bài tập 3 - if (filteredContacts.isEmpty()) → empty state
        // else → LazyColumn { items(filteredContacts) { ContactItem(it) } }

        // Placeholder — xóa khi bắt đầu implement
        Text("Bắt đầu code Search & Filter ở đây!", modifier = Modifier.padding(16.dp))
        LazyColumn {
            items(contacts.take(3)) { contact ->
                ListItem(
                    headlineContent = { Text(contact.name) },
                    supportingContent = { Text(contact.phone) }
                )
                HorizontalDivider()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchFilterScreenPreview() {
    AppTheme { SearchFilterScreen() }
}
