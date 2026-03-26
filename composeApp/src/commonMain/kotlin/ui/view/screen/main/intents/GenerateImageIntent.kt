package ui.view.screen.main.intents

import com.example.aigenerator.PlatformImage
import data.model.Style

sealed class GenerateImageIntent {

    data class Wrong(
        val content: String
    ): GenerateImageIntent()

    data class Generation(
        val prompt: String,
        val images: List<PlatformImage>,
        val style: Style
    ): GenerateImageIntent()
}