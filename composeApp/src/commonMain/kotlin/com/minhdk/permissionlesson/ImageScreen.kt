package com.minhdk.permissionlesson

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.request.ImageRequest
import kotlinx.coroutines.launch

@Preview
@Composable
fun ScreenHeader(
    modifier: Modifier = Modifier,
    title: String = "All Photos",
    selectedCount: Int = 0,
    maxCount: Int = 0,
    onConfirmClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.width(4.dp))
        }

        if(maxCount > 0) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        if (selectedCount > 0) Color(0xFFE879F9)
                        else Color.LightGray
                    )
                    .clickable(enabled = selectedCount > 0) {
                        onConfirmClick()
                    }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Confirm ($selectedCount/$maxCount)",
                    color = Color.White,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Preview
@Composable
fun ImageItem(
    modifier: Modifier = Modifier,
    isCheck: Boolean = false,
    onCheck: () -> Unit = {}
) {
    Box(
        modifier = modifier
    ) {
        Image(
            painter = ColorPainter(Color.Green),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Image(
            painter = ColorPainter(if (!isCheck) Color.LightGray else Color.Magenta),
            contentDescription = null,
            modifier = Modifier
                .size(30.dp)
                .padding(4.dp)
                .clip(CircleShape)
                .clickable {
                    onCheck()
                }.align(Alignment.TopEnd)
        )
    }
}

@Preview
@Composable
fun ImageList(
    modifier: Modifier = Modifier,
    items: List<Int> = listOf(1,2,3,4,5,6)
) {
    LazyVerticalGrid(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        columns = GridCells.Fixed(3),
    ) {
        items(items = items, key = { it }) {
            ImageItem(
                modifier = Modifier.aspectRatio(1f).clip(RoundedCornerShape(12.dp))
            )
        }
    }
}

@Composable
fun ImageScreen(
    context: PlatformContext,
    requestPermission: suspend (success: () -> Unit, failed: () -> Unit) -> Unit
) {

    val hasPermission = hasAccessImagePermission(context)

    val images = remember { mutableStateListOf<Int>() } // simulate collect data from loader
    var showPermissionDialog = remember { mutableStateOf<Boolean>(!hasPermission) }

    val scope = rememberCoroutineScope()

    suspend fun request() {
        requestPermission({
            // load data
        }, {
            showPermissionDialog.value = true
        })
    }

    // one-shot check for load data
    LaunchedEffect(Unit) {
        if(hasPermission) {
            // load data
            return@LaunchedEffect
        }
        request()
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ScreenHeader(
            modifier = Modifier.fillMaxWidth(),
        ) {

        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        if(hasPermission) {
            // UI has data (Loading, empty, filled)
        } else {
            // UI has no permission

            // onClick Grant permission
            scope.launch {
                request()
            }
        }

        // onclick go to setting ò dialog => send lamda load data trong case success
        if(showPermissionDialog.value) {
            // show dialog

            // showPermissionDialog.value = false on dismiss

            // click go to setting
            lifecycleCallback = {
                // load / reload data is has permission
                // or do nothing if not has permission

                lifecycleCallback = null // clear
            }
        }
    }
}
