package ui.view.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import ui.view.screens.main.components.HeaderGroup
import ui.view.screens.main.components.PickImageGroup
import ui.view.screens.main.components.PromptGroup
import ui.view.screens.main.components.StyleGroupWithState
import ui.view.themes.AppColors
import ui.viewmodel.main.MainViewModel

@Preview
@Composable
fun MainScreen(
    padding: Dp = 16.dp
) {

    val viewModel = koinViewModel<MainViewModel>()
    val categories = viewModel.styleState.collectAsStateWithLifecycle()

    var prompt by remember { mutableStateOf("") }

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().background(AppColors.Background).padding(padding)
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
            PickImageGroup(image = null)
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