package com.apero.composetraining.session6.exercises

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apero.composetraining.common.AppTheme

/**
 * ⭐ BÀI TẬP 1: Animated Like Button (Easy)
 *
 * Yêu cầu:
 * - Icon heart: click toggle filled/outlined
 * - animateColorAsState: Gray → Red
 * - animateFloatAsState: scale 1.0 → 1.3 → 1.0 (graphicsLayer)
 * - Dùng Spring animation, KHÔNG tween
 */

@Composable
fun AnimatedLikeButton() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Animated Like Button", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))

        // TODO: [Session 6] Bài tập 1 - Tạo state isLiked
        // var isLiked by remember { mutableStateOf(false) }

        // TODO: [Session 6] Bài tập 1 - animateColorAsState: Gray → Red
        // val color by animateColorAsState(
        //     targetValue = if (isLiked) Color.Red else Color.Gray,
        //     animationSpec = spring(stiffness = Spring.StiffnessLow)
        // )

        // TODO: [Session 6] Bài tập 1 - animateFloatAsState: scale 1.0 → 1.3
        // val scale by animateFloatAsState(
        //     targetValue = if (isLiked) 1.3f else 1f,
        //     animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
        // )

        // TODO: [Session 6] Bài tập 1 - Icon với graphicsLayer { scaleX = scale; scaleY = scale }
        // Icon(
        //     imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
        //     tint = color,
        //     modifier = Modifier.size(64.dp).graphicsLayer { scaleX = scale; scaleY = scale }
        //         .clickable { isLiked = !isLiked }
        // )

        Text("Implement animated heart ở trên!", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Preview(showBackground = true)
@Composable
private fun AnimatedLikeButtonPreview() {
    AppTheme { AnimatedLikeButton() }
}
