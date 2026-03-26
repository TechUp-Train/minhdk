package ui.view.custom

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import ui.view.themes.AppColors

@Composable
fun ColoredComposable(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 10.dp,
    strokeWidth: Dp = 2.dp,
    enabled: Boolean = true,
    colors: List<Color>,
    content: @Composable BoxScope.() -> Unit
) {

    Box(
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .then(
                    Modifier.runnableBackground(
                        strokeWidth = strokeWidth, cornerRadius = cornerRadius, colors = colors
                    ).padding(strokeWidth).takeIf({ enabled }) ?: Modifier
                )
        ) {
            content()
        }
    }
}

@Composable
fun AppBasicTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    singleLine: Boolean = false,
    enabled: Boolean = true,
) {
    val textColor = AppColors.TextPrimary
    val placeholderColor = AppColors.TextMuted

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        singleLine = singleLine,
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = textColor
        ),
        cursorBrush = SolidColor(textColor),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        decorationBox = { innerTextField ->
            Box {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = MaterialTheme.typography.bodyLarge,
                        color = placeholderColor
                    )
                }
                innerTextField()
            }
        }
    )
}

@Composable
fun MarqueeText(
    text: String,
    color: Color,
    style: TextStyle,
    modifier: Modifier = Modifier
) {

    var textWidth by remember { mutableStateOf(0f) }
    var boxWidth by remember { mutableStateOf(0f) }

    val offsetX = remember { Animatable(0f) }
    val measurer = rememberTextMeasurer()

    Box(
        modifier = modifier
            .clip(RectangleShape)
            .onSizeChanged { size ->
                boxWidth = size.width.toFloat()
            }
    ) {
        Text(
            text = text,
            style = style,
            color = color,
            maxLines = 1,
            modifier = Modifier
                .offset { IntOffset(offsetX.value.toInt(), 0) }
        )
    }

    LaunchedEffect(textWidth, boxWidth) {
        textWidth = measurer.measure(
            text = AnnotatedString(text),
            style = style
        ).size.width.toFloat()
        if (textWidth == 0f || boxWidth == 0f) return@LaunchedEffect

        while (true) {
            val diff = textWidth - boxWidth
            if(diff <= 0) {
                break
            }
            offsetX.snapTo(0f)

            offsetX.animateTo(
                targetValue = -50f,
                animationSpec = infiniteRepeatable(
                    tween(2000, easing = LinearEasing),
                    RepeatMode.Reverse
                )
            )
        }
    }
}

