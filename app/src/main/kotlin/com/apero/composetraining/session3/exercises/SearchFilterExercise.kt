package com.apero.composetraining.session3.exercises

import com.apero.composetraining.R
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Preview
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apero.composetraining.common.AppTheme
import com.apero.composetraining.common.SampleData
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

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

private data class Contact(
    val id: Int,
    val name: String,
    val phone: String,
    val avatarUrl: String = "https://i.pravatar.cc/150?u=$id",
    val isActive: Boolean = false
)

private val contacts = SampleData.contacts.map { contact ->
    Contact(
        contact.id,
        contact.name,
        contact.phone,
        contact.avatarUrl,
        contact.name.contains("M")
    )
}

@OptIn(FlowPreview::class)
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    query: String = "",
    onQueryChange: (String) -> Unit = {}
) {

    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
            )
        },
        placeholder = {
            Text("Search…")
        },
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        shape = RoundedCornerShape(8.dp)
    )
}

@Composable
private fun ActiveFilter(
    modifier: Modifier = Modifier,
    enableFilter: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = "Just show active contacts",
            color = Color.Black,
            fontSize = 16.sp,
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Normal,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f)
                .padding(end = 12.dp)
        )

        Switch(
            checked = enableFilter,
            onCheckedChange = onCheckedChange
        )

    }

}

@Preview
@Composable
fun ContactItem(
    modifier: Modifier = Modifier,
    name: String = "fehwf",
    phone: String = "fjewf",
    imageUrl: Boolean = true,
    isActive: Boolean = true
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {

        Box(
            modifier = Modifier
        ) {

            Box(
                modifier = Modifier.clip(CircleShape)
            ) {
                Image(
                    imageVector = Icons.Default.Preview,
                    contentDescription = null,
                    modifier = Modifier
                        .background(color = Color.LightGray)
                        .padding(10.dp)
                )
            }

            Box(
                modifier = Modifier
                    .padding(end = 5.dp)
                    .align(Alignment.BottomEnd)
            ) {
                Image(
                    painter = ColorPainter(color = if (isActive) Color.Green else Color.Red),
                    contentDescription = null,
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .wrapContentHeight()
                .padding(start = 12.dp)
        ) {
            Text(
                text = name,
                color = Color.Black,
                fontSize = 18.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Text(
                text = phone,
                color = Color.Black,
                fontSize = 14.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Normal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
            )

        }

    }
}

@OptIn(FlowPreview::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun SearchFilterScreen() {

    var query by rememberSaveable {
        mutableStateOf("")
    }

    var searchInput by rememberSaveable {
        mutableStateOf("")
    }

    var enableFilter by rememberSaveable {
        mutableStateOf(false)
    }

    val filteredContacts by remember {
        derivedStateOf {
            contacts.filter {
                val active = if (!enableFilter) true else it.isActive
                val isSearched = if (searchInput.isEmpty()) true else it.name.contains(searchInput)
                active && isSearched
            }
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { query }
            .debounce(300)
            .distinctUntilChanged()
            .collect { newQuery ->
                searchInput = newQuery
            }
    }

    Scaffold { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {

            SearchBar(
                query = query,
                onQueryChange = { newQuery ->
                    query = newQuery
                }
            )

            ActiveFilter(
                enableFilter = enableFilter,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            ) {
                enableFilter = it
            }

            if (filteredContacts.isEmpty()) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "No contact founded!",
                        color = Color.Black.copy(alpha = 0.5f),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    items(
                        items = filteredContacts,
                        key = { contact -> contact.id }
                    ) { contact ->
                        ContactItem(
                            name = contact.name,
                            phone = contact.phone,
                            isActive = contact.isActive,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .background(color = Color.White, shape = RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        )
                    }
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchFilterScreenPreview() {
    AppTheme { SearchFilterScreen() }
}
