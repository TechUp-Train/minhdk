package com.example.kmpday2.ui.base

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.getValue
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shape

fun Modifier.reuseBackground(
    color: Color = Color.Unspecified,
    brush: Brush? = null,
    cornerRadius: Dp = 0.dp,
    borderWidth: Dp = 0.dp,
    borderColor: Color = Color.Transparent,
    shape: Shape? = null
): Modifier {
    val finalShape = shape ?: RoundedCornerShape(cornerRadius)

    return this
        .clip(finalShape)
        .drawBehind {
            when {
                brush != null -> drawRect(
                    brush = brush,
                    size = size
                )
                color != Color.Unspecified -> drawRect(
                    color = color,
                    size = size
                )
            }

            if (borderWidth > 0.dp && borderColor != Color.Transparent) {
                val outline = finalShape.createOutline(
                    size = size,
                    layoutDirection = layoutDirection,
                    density = this
                )

                drawOutline(
                    outline = outline,
                    color = borderColor,
                    style = Stroke(borderWidth.toPx())
                )
            }
        }
}

fun Modifier.shimmerEffect(
    duration: Int = 1200
): Modifier = composed {
    val transition = rememberInfiniteTransition()

    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(duration, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val brush = Brush.linearGradient(
        colors = listOf(
            Color.LightGray.copy(alpha = 0.6f),
            Color.LightGray.copy(alpha = 0.2f),
            Color.LightGray.copy(alpha = 0.6f)
        ),
        start = Offset(translateAnim - 1000f, 0f),
        end = Offset(translateAnim, 0f)
    )

    background(brush)
}