package ui.view.screens.main.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ui.view.themes.AppColors

@Preview
@Composable
fun HeaderGroup(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "AI Image Generator",
            style = MaterialTheme.typography.displayLarge,
            color = AppColors.PrimaryLight
        )

        Spacer(Modifier.height(10.dp))

        Text(
            text = "Transform abstract concepts into\n" +
                    "hyper-realistic visual assets using the\n" +
                    "Arcana engine.",
            style = MaterialTheme.typography.bodyMedium,
            color = AppColors.TextMuted
        )
    }
}