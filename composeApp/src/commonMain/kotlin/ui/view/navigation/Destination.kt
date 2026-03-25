package ui.view.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Graph: NavKey

@Serializable
data object Main: Graph

@Serializable
data object PickImage: Graph

@Serializable
data object ImageResult: Graph

@Serializable
data object Back: Graph

