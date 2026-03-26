package ui.view.screen.result

import aigenerator.composeapp.generated.resources.Res
import aigenerator.composeapp.generated.resources.ic_back
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import com.example.aigenerator.MultiPlatformContext
import com.example.aigenerator.checkShouldAskWriteImagePermission
import com.example.aigenerator.rememberPermissionLauncher
import com.example.aigenerator.utils.writeImagePermission
import data.core.remote.service.base.Response
import data.model.PromptResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import ui.view.custom.ColoredComposable
import ui.view.navigation.Back
import ui.view.navigation.Graph
import ui.view.screen.result.component.DownloadButton
import ui.view.screen.result.component.NotifyDialog
import ui.view.screen.result.component.downloadFailed
import ui.view.screen.result.component.downloadSuccess
import ui.view.screen.result.component.needPermisison
import ui.view.themes.AppColors
import ui.view.themes.AppShapes
import ui.viewmodel.main.ResultViewModel

@Composable
fun ImageResultScreen(
    padding: PaddingValues,
    result: PromptResponse,
    onNavigate: (Graph) -> Unit
) {

    val viewmodel: ResultViewModel = koinViewModel()

    val permissionLauncher = rememberPermissionLauncher(writeImagePermission)

    val scope = rememberCoroutineScope()

    var showPermissionDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showFailedDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewmodel.downloadState.collect { state ->
            when(state) {
                is Response.Success -> showSuccessDialog = true
                else -> showFailedDialog = true
            }
        }
    }

    Column(
        modifier = Modifier.background(AppColors.Background).padding(padding),
    ) {

        IconButton(
            onClick = {
                onNavigate(Back)
            },
            modifier = Modifier.align(Alignment.Start)
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_back),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = AppColors.Primary
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth().weight(1f)
        ) {
            ColoredComposable(
                colors = listOf(AppColors.PrimaryLight, Color.Transparent),
                modifier = Modifier.fillMaxWidth().aspectRatio(1f)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalPlatformContext.current)
                        .data(result.data?.url)
                        .size(300)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .padding(5.dp)
                        .clip(AppShapes.medium)
                )
            }
        }

        if(showPermissionDialog) NotifyDialog(needPermisison) {
            showPermissionDialog = false
        }

        if(showSuccessDialog) NotifyDialog(downloadSuccess) {
            showSuccessDialog = false
        }

        if(showFailedDialog) NotifyDialog(downloadFailed) {
            showFailedDialog = false
        }

        DownloadButton(
            modifier = Modifier.fillMaxWidth().height(60.dp)
        ) {
            scope.launch(Dispatchers.Main.immediate) {
                if(checkShouldAskWriteImagePermission()) {
                    val granted = permissionLauncher.request()
                    if(granted) {
                        result.data?.url?.let {
                            viewmodel.loadImageData(it)
                        }
                    } else {
                        showPermissionDialog = true
                    }
                } else {
                    result.data?.url?.let {
                        viewmodel.loadImageData(it)
                    }
                }
            }
        }

        Spacer(Modifier.height(50.dp))

    }

}