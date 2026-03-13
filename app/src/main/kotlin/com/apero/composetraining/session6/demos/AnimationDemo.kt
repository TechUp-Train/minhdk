package com.apero.composetraining.session6.demos

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apero.composetraining.common.AppTheme
import kotlin.math.roundToInt

// ============================================================
// DEMO 1: animate*AsState — animation đơn giản nhất
// ============================================================

@Composable
fun AnimateAsStateDemo() {
    var isLiked by remember { mutableStateOf(false) }

    // animateColorAsState: màu chuyển mượt từ Gray → Red
    val iconColor by animateColorAsState(
        targetValue = if (isLiked) Color.Red else Color.Gray,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "likeColor"
    )

    // animateFloatAsState: scale khi liked
    val scale by animateFloatAsState(
        targetValue = if (isLiked) 1.3f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "likeScale"
    )

    Column(modifier = Modifier.padding(16.dp)) {
        Text("animate*AsState Demo", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { isLiked = !isLiked }
        ) {
            Icon(
                imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Like",
                tint = iconColor,
                modifier = Modifier
                    .size(48.dp)
                    .scale(scale) // graphicsLayer alternative
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(if (isLiked) "Liked! ❤️" else "Tap to like")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AnimateAsStateDemoPreview() {
    AppTheme { AnimateAsStateDemo() }
}

// ============================================================
// DEMO 2: AnimatedVisibility — show/hide với animation
// ============================================================

@Composable
fun AnimatedVisibilityDemo() {
    var visible by remember { mutableStateOf(true) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("AnimatedVisibility Demo", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { visible = !visible }) {
            Text(if (visible) "Ẩn panel" else "Hiện panel")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Combine fadeIn + slideIn cho hiệu ứng đẹp
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Đây là panel ẩn/hiện", fontWeight = FontWeight.SemiBold)
                    Text("Với fadeIn + slideInVertically animation")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AnimatedVisibilityDemoPreview() {
    AppTheme { AnimatedVisibilityDemo() }
}

// ============================================================
// DEMO 3: Spring vs Tween — cảm giác khác biệt
// ============================================================

@Composable
fun SpringVsTweenDemo() {
    var expanded by remember { mutableStateOf(false) }

    // Spring — vật lý tự nhiên, bouncy
    val springSize by animateDpAsState(
        targetValue = if (expanded) 200.dp else 80.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "spring"
    )

    // Tween — linear, constant speed
    val tweenSize by animateDpAsState(
        targetValue = if (expanded) 200.dp else 80.dp,
        animationSpec = tween(durationMillis = 500),
        label = "tween"
    )

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Spring vs Tween", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { expanded = !expanded }) {
            Text(if (expanded) "Thu nhỏ" else "Phóng to")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Spring (bouncy)")
                Box(
                    modifier = Modifier
                        .size(springSize)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🏀", fontSize = 24.sp)
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Tween (linear)")
                Box(
                    modifier = Modifier
                        .size(tweenSize)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.tertiary),
                    contentAlignment = Alignment.Center
                ) {
                    Text("📦", fontSize = 24.sp)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SpringVsTweenDemoPreview() {
    AppTheme { SpringVsTweenDemo() }
}

// ============================================================
// DEMO 4: Gesture — drag card
// ============================================================

@Composable
fun GestureDemo() {
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Drag Gesture Demo", fontWeight = FontWeight.Bold)
        Text("Kéo thẻ bên dưới!", style = MaterialTheme.typography.bodySmall)

        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.fillMaxWidth().height(300.dp)) {
            Card(
                modifier = Modifier
                    .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragEnd = {
                                // Snap back
                                offsetX = 0f
                                offsetY = 0f
                            }
                        ) { change, dragAmount ->
                            change.consume()
                            offsetX += dragAmount.x
                            offsetY += dragAmount.y
                        }
                    }
                    .size(150.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("🃏 Drag me!", fontSize = 18.sp)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GestureDemoPreview() {
    AppTheme { GestureDemo() }
}
