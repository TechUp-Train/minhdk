package data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PromptResponse(
    @SerialName("statusCode")
    val statusCode: Int? = null,

    @SerialName("message")
    val message: String? = null,

    @SerialName("data")
    val data: ImageData? = null,

    @SerialName("timestamp")
    val timestamp: Long? = null
)

@Serializable
data class ImageData(
    @SerialName("url")
    val url: String? = null,

    @SerialName("path")
    val path: String? = null
)