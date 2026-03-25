package ui.view.custom

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

//@Composable
//fun Modifier.runnableBackground(
//    colors: List<Color>,
//    cornerRadius: Float
//): Modifier = composed {
//
//    val infiniteTransition = rememberInfiniteTransition()
//
//    val value by infiniteTransition.animateFloat(
//        initialValue = 0f,
//        targetValue = 1f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(5000, easing = LinearEasing),
//            repeatMode = RepeatMode.Restart
//        )
//    )
//
//    this.drawWithCache {
//        val width = size.width
//        val height = size.height
//        val total = 2 * (width + height)
//        val curValue = total * value
//
//        var xStart = 0f
//        var yStart = 0f
//        var xEnd = 0f
//        var yEnd = 0f
//
//        when {
//            0 <= curValue && curValue <= width -> {
//                xStart = curValue
//                yStart = 0f
//            }
//
//            width < curValue && curValue <= width + height -> {
//                xStart = width
//                yStart = curValue - xStart
//            }
//
//            width + height < curValue && curValue <= 2 * width + height -> {
//                xStart = width - (curValue - (width + height))
//                yStart = height
//            }
//
//            2 * width + height < curValue && curValue <= total -> {
//                xStart = 0f
//                yStart = height - (curValue - (2 * width + height))
//            }
//        }
//
//        xEnd = width - xStart
//        yEnd = height - yStart
//
//        onDrawBehind {
//            val start = Offset(xStart, yStart)
//            val end = Offset(xEnd, yEnd)
//
//            drawRoundRect(
//                brush = Brush.linearGradient(
//                    colors = colors,
//                    start = start,
//                    end = end
//                ),
//                cornerRadius = CornerRadius(cornerRadius, cornerRadius)
//            )
//        }
//    }
//}

@Composable
fun Modifier.runnableBackground(
    colors: List<Color>,
    strokeWidth: Dp,
    cornerRadius: Dp,
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

    val strokeWidthPx: Float
    val cornerRadiusPx: Float

    with(LocalDensity.current) {
        strokeWidthPx = strokeWidth.toPx()
        cornerRadiusPx = cornerRadius.toPx()
    }

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

            clipPath(
                path = Path().apply {
                    addRoundRect(
                        RoundRect(
                            rect = Rect(
                                offset = Offset(strokeWidthPx.toFloat(), strokeWidthPx.toFloat()),
                                size = Size(
                                    size.width - strokeWidthPx * 2,
                                    size.height - strokeWidthPx * 2
                                )
                            ),
                            cornerRadius = CornerRadius(
                                (cornerRadiusPx - strokeWidthPx).toFloat(),
                                (cornerRadiusPx - strokeWidthPx).toFloat()
                            )
                        )
                    )
                },
                clipOp = ClipOp.Difference
            ) {
                drawRoundRect(
                    brush = Brush.linearGradient(
                        colors = colors,
                        start = start,
                        end = end
                    ),
                    cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx)
                )
            }
        }
    }
}