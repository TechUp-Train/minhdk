package ui.view.screen.main.components

import aigenerator.composeapp.generated.resources.Res
import aigenerator.composeapp.generated.resources.ic_pick
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aigenerator.PlatformImage
import com.example.aigenerator.utils.hasPermission
import com.example.aigenerator.utils.readImagePermission
import org.jetbrains.compose.resources.painterResource
import ui.view.custom.ColoredComposable
import ui.view.screen.main.intents.PickImageIntent
import ui.view.themes.AppColors
import ui.view.themes.Space

@Preview
@Composable
private fun AddPhoto(
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Image(
            painter = painterResource(Res.drawable.ic_pick),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier.size(50.dp).clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(Space.x12))

        Text(
            text = "Add your photo",
            style = MaterialTheme.typography.headlineSmall,
            color = AppColors.TextPrimary,
            maxLines = 1
        )
    }
}

@Composable
fun PickImageGroup(
    modifier: Modifier = Modifier,
    image: PlatformImage?,
    onIntent: (PickImageIntent) -> Unit
) {

    Column(
        modifier = modifier
    ) {
        Text(
            text = "Visual Reference",
            style = MaterialTheme.typography.headlineSmall,
            color = AppColors.TextSecondary
        )

        Spacer(modifier = Modifier.height(Space.x12))

        ColoredComposable(
            colors = listOf(AppColors.Primary, Color.Transparent),
            modifier = modifier.fillMaxWidth().aspectRatio(1f)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                image?.let {
                    PlatformImage(
                        image = it,
                        modifier = Modifier.fillMaxSize().padding(5.dp)
                    )
                } ?: run {
                    AddPhoto(
                        modifier = Modifier.clickable {
                            if(hasPermission(readImagePermission)) {
                                onIntent(PickImageIntent.PickImage)
                            } else {
                                onIntent(PickImageIntent.AskPermission)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PickImageGroupPreview() {
    PickImageGroup(
        image = null
    ) {

    }
}