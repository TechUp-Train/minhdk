package data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PresignLink(
    @SerialName("statusCode") val statusCode: Int? = null,
    @SerialName("message") val message: String? = null,
    @SerialName("data") val data: UploadUrlData? = null,
    @SerialName("timestamp") val timestamp: Long? = null
)

@Serializable
data class UploadUrlData(
    @SerialName("url") val url: String? = null,
    @SerialName("path") val path: String? = null
)
