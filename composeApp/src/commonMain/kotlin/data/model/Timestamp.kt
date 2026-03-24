package data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Timestamp(
    @SerialName("statusCode") val statusCode: Int? = null,
    @SerialName("message") val message: String? = null,
    @SerialName("data") val data: TimestampData? = null,
    @SerialName("timestamp") val timestamp: Long? = null
)

@Serializable
data class TimestampData(
    @SerialName("timestamp") val timestamp: Long? = null,
    @SerialName("timestring") val timestring: String? = null
)
