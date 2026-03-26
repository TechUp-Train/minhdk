package ui.view.navigation

import androidx.navigation3.runtime.NavKey
import com.example.aigenerator.PlatformImage
import data.model.PromptResponse
import kotlinx.serialization.Serializable

@Serializable
sealed interface Graph: NavKey

@Serializable
data object Main: Graph

@Serializable
data object PickImage: Graph

@Serializable
data class ImageResult(val response: PromptResponse): Graph

@Serializable
data object Back: Graph

