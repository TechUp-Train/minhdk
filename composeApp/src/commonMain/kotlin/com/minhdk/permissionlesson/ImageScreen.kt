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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.toUri
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

        if (maxCount > 0) {
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


@Composable
fun ImageItem(
    modifier: Modifier = Modifier,
    model: Any?,
    isCheck: Boolean,
    onCheck: (Boolean) -> Unit = {}
) {
    Box(
        modifier = modifier
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalPlatformContext.current)
                .data(model)
                .size(300, 300)
                .build(),
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
                    onCheck(!isCheck)
                }.align(Alignment.TopEnd)
        )
    }
}

@Composable
fun ImageList(
    modifier: Modifier = Modifier,
    items: List<Image>,
    selectedItems: List<String>,
    selectedChange: (Image, Boolean) -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        columns = GridCells.Fixed(3),
    ) {
        items(items = items, key = { item -> item.id }) { item ->
            ImageItem(
                model = item.url ?: item.uri?.toUri() ?: item.data,
                isCheck = selectedItems.contains(item.id),
                modifier = Modifier.aspectRatio(1f).clip(RoundedCornerShape(12.dp))
            ) { checked ->
                selectedChange(item, checked)
            }
        }
    }
}

@Composable
private fun NoPermission(
    onClickGrant: () -> Unit = {}
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {

        Text(
            text = "App need you permission to show your images",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        Button(
            onClick = onClickGrant,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Grant permission")
        }

    }
}

@Composable
private fun PermissionDialog(
    onGrant: () -> Unit,
    onCancel: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancel,
        title = {
            Text("Yêu cầu quyền truy cập ảnh")
        },
        text = {
            Text(
                "Ứng dụng cần quyền truy cập vào ảnh của bạn để có thể chọn và hiển thị hình ảnh. " +
                        "Vui lòng cấp quyền để tiếp tục sử dụng tính năng này."
            )
        },
        confirmButton = {
            TextButton(onClick = onGrant) {
                Text("Grant")
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun AskPermissionFirst(
    action: (Boolean) -> Unit
) {
    val launcher by remember { mutableStateOf(getPermissionLauncher()) }

    ConfigPermissionLauncher(launcher)

    LaunchedEffect(Unit) {
        val granted = launcher.request {}
        action(granted)
    }
}

@Composable
fun ImagePermissionDialog(
    onCancel: () -> Unit,
    onGoToSetting: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancel,
        title = {
            Text(
                text = "Quyền truy cập ảnh",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Text(
                text = "Ứng dụng cần quyền truy cập thư viện ảnh để tải và hiển thị hình ảnh. " +
                        "Vui lòng cấp quyền để tiếp tục sử dụng tính năng này."
            )
        },
        confirmButton = {
            TextButton(onClick = onGoToSetting) {
                Text("Go to Settings")
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun ImageScreen(context: PlatformContext) {

    var state by remember { mutableStateOf<ImageScreenState>(ImageScreenState.Initial) }

    var showDialog by remember { mutableStateOf(false) }

    var scope = rememberCoroutineScope()

    val onPermissionResult = result@{ granted: Boolean ->
        if (granted) {
            scope.launch {
                state = ImageScreenState.PermissionGranted(
                    images = loadImages(context), mutableListOf<String>()
                )
            }
        } else {
            state = ImageScreenState.PermissionDenied
            showDialog = true
        }
        return@result
    }

    AskPermissionFirst(onPermissionResult)

    when (state) {
        is ImageScreenState.Initial -> {}
        is ImageScreenState.PermissionDenied -> NoPermission()
        is ImageScreenState.PermissionGranted -> ImageList(
            items = (state as ImageScreenState.PermissionGranted).images,
            selectedItems = (state as ImageScreenState.PermissionGranted).selected
        ) click@ { image, selected ->
            val oldState = (state as ImageScreenState.PermissionGranted)
            state = oldState.copy(selected = oldState.selected.toMutableList().apply {
                if(selected) {
                    add(image.id)
                } else {
                    remove(image.id)
                }
            })
            println("fwef: ${(state as ImageScreenState.PermissionGranted).selected}")
        }
    }

    if (showDialog) ImagePermissionDialog(
        {
            showDialog = false
        }, {

        }
    )
}
