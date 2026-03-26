package ui.view.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.aigenerator.goToSetting
import com.example.aigenerator.isNetworkAvailable
import com.example.aigenerator.rememberPermissionLauncher
import com.example.aigenerator.utils.hasPermission
import com.example.aigenerator.utils.readImagePermission
import data.model.Style
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.format.Padding
import org.koin.compose.viewmodel.koinViewModel
import ui.view.navigation.Graph
import ui.view.navigation.ImageResult
import ui.view.navigation.PickImage
import ui.view.screen.main.components.GenerateButton
import ui.view.screen.main.components.HeaderGroup
import ui.view.screen.main.components.LoadingDialog
import ui.view.screen.main.components.ErrorGroup
import ui.view.screen.main.components.PermissionDialog
import ui.view.screen.main.components.PickImageGroup
import ui.view.screen.main.components.PromptGroup
import ui.view.screen.main.components.SelectedImages
import ui.view.screen.main.components.SelectedStyle
import ui.view.screen.main.components.StyleGroupWithState
import ui.view.screen.main.intents.GenerateImageIntent
import ui.view.screen.main.intents.PermissionDialogIntent
import ui.view.screen.main.intents.PickImageIntent
import ui.view.state.UiState
import ui.view.themes.AppColors
import ui.view.themes.AppShapes
import ui.viewmodel.main.MainViewModel

@Composable
fun MainScreen(
    padding: PaddingValues,
    onNavigate: (Graph) -> Unit
) {

    val viewModel = koinViewModel<MainViewModel>()
    val categories = viewModel.styleState.collectAsStateWithLifecycle()
    val selectedImage = viewModel.imageState.collectAsStateWithLifecycle()
    var selectedStyle = viewModel.selectedStyle.collectAsStateWithLifecycle()

    var prompt by rememberSaveable { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val permissionLauncher = rememberPermissionLauncher(readImagePermission)

    var showPermissionDialog by remember { mutableStateOf(false) }
    var showLoadingDialog = viewModel.loadingState.collectAsStateWithLifecycle()
    var showErrorBar = viewModel.errorState.collectAsStateWithLifecycle()

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        if (viewModel.pickImage && hasPermission(readImagePermission)) onNavigate(PickImage)
    }

    LaunchedEffect(Unit) {
        viewModel.generationState.collect { res ->
            onNavigate(ImageResult(res))
        }
    }

    LazyColumn(
        contentPadding = PaddingValues(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().background(AppColors.Background).padding(paddingValues = padding)
    ) {

        item {
            HeaderGroup(modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(25.dp))
        }

        item {
            PromptGroup(
                text = prompt,
                modifier = Modifier.fillMaxWidth()
            ) { change -> prompt = change }
            Spacer(modifier = Modifier.height(25.dp))
        }

        item {

            if (showPermissionDialog) PermissionDialog {
                when (it) {
                    PermissionDialogIntent.Deny -> {
                        showPermissionDialog = false
                    }

                    PermissionDialogIntent.Grant -> {
                        viewModel.pickImage = true
                        goToSetting(readImagePermission)
                    }
                }
            }

            Box {
                if (selectedImage.value.isNotEmpty()) {
                    SelectedImages(
                        images = selectedImage.value,
                        modifier = Modifier.fillMaxWidth().aspectRatio(1f)
                    ) {
                        onNavigate(PickImage)
                    }
                } else {
                    PickImageGroup(image = null) { intent ->
                        when (intent) {
                            is PickImageIntent.PickImage -> onNavigate(PickImage)
                            is PickImageIntent.AskPermission -> {
                                scope.launch(Dispatchers.Main.immediate) {
                                    if (permissionLauncher.request()) {
                                        onNavigate(PickImage)
                                        return@launch
                                    }
                                    showPermissionDialog = true
                                }
                            }
                        }
                    }
                }

                SelectedStyle(
                    style = selectedStyle.value,
                    modifier = Modifier.wrapContentHeight().width(200.dp)
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(25.dp))
            StyleGroupWithState(
                selectedStyle = selectedStyle.value,
                categories = categories,
                modifier = Modifier.fillMaxWidth()
            ) { selected ->
                viewModel.selectStyle(selected)
            }
        }

        item {
            Spacer(Modifier.height(25.dp))

            if (showLoadingDialog.value) LoadingDialog()

            showErrorBar.value?.let {
                ErrorGroup(isVisible = true, message = it)

                Spacer(modifier = Modifier.height(15.dp))
            }

            GenerateButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                .shadow(
                    elevation = 12.dp,
                    shape = AppShapes.large,
                    spotColor = AppColors.PrimaryLight
                )
            ) click@ {
                if(showErrorBar.value != null) return@click

                viewModel.handleGenerationIntent(
                    when {
                        !isNetworkAvailable() -> GenerateImageIntent.Wrong("Please check you internet!")

                        selectedImage.value.isEmpty() || selectedStyle.value == null || prompt.isEmpty() -> {
                            GenerateImageIntent.Wrong("You must fill the prompt, choose style and your images.")
                        }

                        else -> GenerateImageIntent.Generation(
                            prompt = prompt,
                            images = selectedImage.value,
                            style = selectedStyle.value!!
                        )
                    }
                )
            }
            Spacer(Modifier.height(50.dp))
        }
    }
}