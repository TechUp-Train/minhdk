package com.apero.composetraining.session6.exercises

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apero.composetraining.common.AppTheme
import com.apero.composetraining.common.SampleData

/**
 * ⭐⭐ BÀI TẬP 2: Expandable FAQ List (Medium)
 *
 * Yêu cầu:
 * - LazyColumn: 5 câu hỏi FAQ
 * - Click question → AnimatedVisibility (answer expand/collapse)
 * - Arrow icon rotate 0° → 180° (animateFloatAsState)
 * - animateContentSize trên answer text
 * - Chỉ 1 item expand tại 1 thời điểm
 */

@Composable
fun ExpandableListScreen() {
    val faqItems = SampleData.faqItems

    // TODO: [Session 6] Bài tập 2 - State cho expanded item id
    // var expandedItemId by remember { mutableIntStateOf(-1) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("FAQ", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // TODO: [Session 6] Bài tập 2 - LazyColumn hiển thị FAQ items
        // items(faqItems, key = { it.id }) { faq ->
        //     FaqItemCard(
        //         question = faq.question,
        //         answer = faq.answer,
        //         isExpanded = expandedItemId == faq.id,
        //         onToggle = { expandedItemId = if (expandedItemId == faq.id) -1 else faq.id }
        //     )
        // }

        // Placeholder
        Text("Bắt đầu code Expandable List ở đây!")
    }
}

// TODO: [Session 6] Bài tập 2 - Tạo FaqItemCard composable
// Params: question, answer, isExpanded, onToggle
// - Row: Text question + Icon arrow (rotate animation)
// - AnimatedVisibility cho answer
// - animateContentSize trên Card

@Preview(showBackground = true)
@Composable
private fun ExpandableListScreenPreview() {
    AppTheme { ExpandableListScreen() }
}
