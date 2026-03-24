package data.model

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class PromptResponse(
    @SerialName("statusCode")
    val statusCode: Int? = null,

    @SerialName("message")
    val message: String? = null,

    @SerialName("data")
    val data: ApiData? = null,

    @SerialName("timestamp")
    val timestamp: Long? = null
)

@Serializable
data class ApiData(
    @SerialName("url")
    val url: String? = null,

    @SerialName("path")
    val path: String? = null
)