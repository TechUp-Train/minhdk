package ui.view.screen.main.components

import aigenerator.composeapp.generated.resources.Res
import aigenerator.composeapp.generated.resources.ic_back
import aigenerator.composeapp.generated.resources.ic_bolt
import aigenerator.composeapp.generated.resources.ic_change
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import ui.view.themes.AppColors
import ui.view.themes.AppShapes

@Preview
@Composable
fun GenerateButton(
    modifier: Modifier = Modifier,
    text: String = "Generate image",
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        shape = AppShapes.large,
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        modifier = modifier
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(AppShapes.large)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            AppColors.PrimaryDark,
                            AppColors.Primary
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_bolt),
                    contentDescription = null,
                    tint = Color(0xFF4C3FFB),
                    modifier = Modifier.size(18.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = text,
                    style = MaterialTheme.typography.headlineSmall,
                    color = AppColors.TextPrimary
                )
            }
        }
    }
}