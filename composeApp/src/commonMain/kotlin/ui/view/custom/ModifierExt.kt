package ui.view.custom

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun Modifier.runnableBackground(
    colors: List<Color>,
    cornerRadius: Float
): Modifier = composed {

    val infiniteTransition = rememberInfiniteTransition()

    val value by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    this.drawWithCache {
        val width = size.width
        val height = size.height
        val total = 2 * (width + height)
        val curValue = total * value

        var xStart = 0f
        var yStart = 0f
        var xEnd = 0f
        var yEnd = 0f

        when {
            0 <= curValue && curValue <= width -> {
                xStart = curValue
                yStart = 0f
            }

            width < curValue && curValue <= width + height -> {
                xStart = width
                yStart = curValue - xStart
            }

            width + height < curValue && curValue <= 2 * width + height -> {
                xStart = width - (curValue - (width + height))
                yStart = height
            }

            2 * width + height < curValue && curValue <= total -> {
                xStart = 0f
                yStart = height - (curValue - (2 * width + height))
            }
        }

        xEnd = width - xStart
        yEnd = height - yStart

        onDrawBehind {
            val start = Offset(xStart, yStart)
            val end = Offset(xEnd, yEnd)

            drawRoundRect(
                brush = Brush.linearGradient(
                    colors = colors,
                    start = start,
                    end = end
                ),
                cornerRadius = CornerRadius(cornerRadius, cornerRadius)
            )
        }
    }
}