package ui.view.screen.result.component

import aigenerator.composeapp.generated.resources.Res
import aigenerator.composeapp.generated.resources.ic_bolt
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ui.view.themes.AppColors
import ui.view.themes.AppShapes

@Preview
@Composable
fun DownloadButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        shape = AppShapes.large,
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(
            containerColor = AppColors.Primary
        ),
        modifier = modifier
    ) {
        Text(
            text = "Download",
            style = MaterialTheme.typography.headlineSmall,
            color = AppColors.TextPrimary
        )
    }
}