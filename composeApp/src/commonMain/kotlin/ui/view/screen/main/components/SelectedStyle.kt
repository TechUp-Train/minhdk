package ui.view.screen.main.components

import androidx.collection.emptyLongList
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import data.model.Style
import ui.view.custom.ColoredComposable
import ui.view.custom.MarqueeText
import ui.view.themes.AppColors

@Composable
fun SelectedStyle(
    modifier: Modifier = Modifier,
    style: Style?
) {
    ColoredComposable(
        colors = listOf(AppColors.PrimaryLight, Color.Transparent),
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth().height(40.dp)
        ) {
            Text(
                text = style?.styleName ?: "No style selected",
                style = MaterialTheme.typography.bodyMedium,
                color = AppColors.TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}