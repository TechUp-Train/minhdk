package ui.view.screens.main.components

import androidx.compose.runtime.Composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import ui.view.custom.AppBasicTextField
import ui.view.custom.ColoredComposable
import ui.view.themes.AppColors
import ui.view.themes.AppTheme
import ui.view.themes.Space

@Composable
fun PromptGroup(
    modifier: Modifier = Modifier,
    text: String,
    placeholder: String = "Type your prompt...",
    onTextInput: (String) -> Unit = {}
) {

    Column(
        modifier = modifier
    ) {
        Text(
            text = "Conceptual Prompt",
            style = MaterialTheme.typography.headlineSmall,
            color = AppColors.TextSecondary
        )

        Spacer(modifier = Modifier.height(Space.x12))

        ColoredComposable(
            background = AppColors.SurfaceLight,
            colors = listOf(AppColors.Primary, Color.Transparent),
            modifier = Modifier.fillMaxWidth().height(150.dp)
        ) {
            AppBasicTextField(
                value = text,
                onValueChange = onTextInput,
                placeholder = placeholder,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}


@Preview
@Composable
fun PromptGroupPreview() {
    AppTheme {
        PromptGroup(
            text = "Create for me an awesome image from the input.",
            modifier = Modifier.fillMaxWidth()
        ) { change ->

        }
    }
}