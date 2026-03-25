package ui.view.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.aigenerator.PlatformImage
import com.example.aigenerator.goToSetting
import com.example.aigenerator.rememberPermissionLauncher
import com.example.aigenerator.utils.hasPermission
import com.example.aigenerator.utils.readImagePermission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import ui.view.navigation.Graph
import ui.view.navigation.PickImage
import ui.view.screen.main.components.HeaderGroup
import ui.view.screen.main.components.PermissionDialog
import ui.view.screen.main.components.PickImageGroup
import ui.view.screen.main.components.PromptGroup
import ui.view.screen.main.components.StyleGroupWithState
import ui.view.screen.main.intents.PermissionDialogIntent
import ui.view.screen.main.intents.PickImageIntent
import ui.view.themes.AppColors
import ui.viewmodel.main.MainViewModel

@Preview
@Composable
fun MainScreen(
    onNavigate: (Graph) -> Unit
) {

    val viewModel = koinViewModel<MainViewModel>()
    val categories = viewModel.styleState.collectAsStateWithLifecycle()
    val selectedImage = viewModel.imageState.collectAsStateWithLifecycle()

    var prompt by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val permissionLauncher = rememberPermissionLauncher(readImagePermission)

    var showPermissionDialog by remember { mutableStateOf(false) }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        if(viewModel.pickImage  && hasPermission(readImagePermission)) onNavigate(PickImage)
    }

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().background(AppColors.Background).padding(16.dp)
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
            if(selectedImage.value.isNotEmpty()) {
                Text("Co 2 images ne !!!")
                return@item
            }
            if(showPermissionDialog) PermissionDialog {
                when(it) {
                    PermissionDialogIntent.Deny -> {
                        showPermissionDialog = false
                    }
                    PermissionDialogIntent.Grant -> {
                        viewModel.pickImage = true
                        goToSetting(readImagePermission)
                    }
                }
            }

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
            Spacer(modifier = Modifier.height(25.dp))
        }

        item {
            StyleGroupWithState(
                categories = categories,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(50.dp))
        }
    }
}