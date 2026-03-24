package data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PromptRequest(
    @SerialName("files") val files: List<String>,
    @SerialName("mode") val mode: String,
    @SerialName("positivePrompt") val positivePrompt: String
)