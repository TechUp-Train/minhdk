package ui.view.screen.pickimage

import aigenerator.composeapp.generated.resources.Res
import aigenerator.composeapp.generated.resources.ic_back
import aigenerator.composeapp.generated.resources.ic_checked
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aigenerator.PlatformImage
import ui.view.themes.AppColors
import ui.view.themes.AppShapes
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import kotlinx.datetime.format.Padding
import org.jetbrains.compose.resources.painterResource
import ui.view.custom.ColoredComposable
import ui.view.navigation.Back
import ui.view.navigation.Graph
import ui.viewmodel.main.PickImageViewModel

@Composable
fun PickImageScreen(
    padding: PaddingValues,
    onConfirm: (List<PlatformImage>) -> Unit,
    onNavigate: (Graph) -> Unit
) {
    val viewModel: PickImageViewModel = koinViewModel()
    val images by viewModel.imageState.collectAsState()
    val selectedImages = remember { mutableStateListOf<PlatformImage>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Background)
            .padding(padding)
    ) {
        PickImageTopBar(
            selectedCount = selectedImages.size,
            onConfirm = { onConfirm(selectedImages.toList()) },
            onNavigate = onNavigate
        )
        
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(images) { image ->
                val isSelected = selectedImages.contains(image)
                ImageGridItem(
                    image = image,
                    isSelected = isSelected,
                    onClick = {
                        println("Clickkkk")
                        if (isSelected) {
                            selectedImages.remove(image)
                        } else {
                            if (selectedImages.size < 2) {
                                selectedImages.add(image)
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun PickImageTopBar(
    selectedCount: Int,
    onNavigate: (Graph) -> Unit,
    onConfirm: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF131722))
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            IconButton(
                onClick = {
                    onNavigate(Back)
                }
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_back),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = Color.White
                )
            }

            Text(
                text = "Apero",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
        }

        Button(
            onClick = onConfirm,
            colors = ButtonDefaults.buttonColors(
                containerColor = AppColors.Primary,
                contentColor = AppColors.SurfaceDark
            ),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            shape = CircleShape
        ) {
            Text(
                text = "CONFIRM",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                letterSpacing = 1.sp
            )
            
            Box(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(AppColors.PrimaryDark),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = selectedCount.toString(),
                    color = AppColors.TextPrimary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ImageGridItem(
    image: PlatformImage,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    ColoredComposable(
        enabled = isSelected,
        colors = listOf(
            AppColors.Primary,
            Color.Transparent
        ),
        cornerRadius = 12.dp,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {

        PlatformImage(
            image = image,
            modifier = Modifier.fillMaxSize().clip(AppShapes.medium)
        )

        Box(
            Modifier
                .size(24.dp)
                .align(Alignment.TopEnd)
                .padding(top = 4.dp, end = 4.dp)
        ) {
            if(isSelected) {
                Image(
                    painter = painterResource(Res.drawable.ic_checked),
                    contentDescription = "Selected",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(AppColors.Background.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {}
            }
        }

        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxSize(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Transparent
            ),
            contentPadding = PaddingValues(0.dp),
            shape = RectangleShape
        ) {}
    }
}
