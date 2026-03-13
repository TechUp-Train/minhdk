package com.apero.composetraining.session6.exercises

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apero.composetraining.common.AppTheme
import com.apero.composetraining.common.SampleData

/**
 * ⭐⭐⭐ BÀI TẬP 3: Tinder Swipe Card (Challenge)
 *
 * Yêu cầu:
 * - 5 profile cards stacked (Box)
 * - Drag gesture: detectDragGestures
 * - Swipe left = "Nope" (red overlay), right = "Like" (green overlay)
 * - Card rotation theo drag offset
 * - animateFloatAsState cho snap back khi thả giữa
 * - Card tiếp theo slide lên khi top card bị swipe
 * - Drag amount > threshold → dismiss
 * - Drag amount < threshold → snap back với spring
 */

@Composable
fun SwipeCardScreen() {
    val profiles = SampleData.profileCards

    // TODO: [Session 6] Bài tập 3 - State cho current card index
    // var currentIndex by remember { mutableIntStateOf(0) }

    // TODO: [Session 6] Bài tập 3 - State cho drag offset
    // var offsetX by remember { mutableFloatStateOf(0f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Swipe Cards", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // TODO: [Session 6] Bài tập 3 - Box chứa stacked cards
        // Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
        //     // Render cards từ dưới lên trên
        //     profiles.drop(currentIndex).take(3).reversed().forEachIndexed { index, profile ->
        //         SwipeableProfileCard(
        //             profile = profile,
        //             isTopCard = index == ... ,
        //             offsetX = if (isTopCard) offsetX else 0f,
        //             modifier = Modifier.offset(y = (index * -8).dp) // stack effect
        //         )
        //     }
        // }

        // TODO: [Session 6] Bài tập 3 - Row với Nope + Like buttons ở dưới
        // Row { Button "❌ Nope" + Button "💚 Like" }

        // Placeholder
        Text("Bắt đầu code Swipe Card ở đây!")
    }
}

// TODO: [Session 6] Bài tập 3 - Tạo SwipeableProfileCard composable
// - Card với profile info (name, age, bio)
// - pointerInput { detectDragGestures(...) } cho top card
// - graphicsLayer { rotationZ = offsetX / 20 } cho rotation theo drag
// - Color overlay: red khi swipe left, green khi swipe right
// - Threshold: abs(offsetX) > 300 → dismiss, else snap back

@Preview(showBackground = true)
@Composable
private fun SwipeCardScreenPreview() {
    AppTheme { SwipeCardScreen() }
}
