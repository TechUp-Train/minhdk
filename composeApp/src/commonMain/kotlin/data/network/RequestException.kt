package data.network

data class RequestException(
    val code: Int,
    override val message: String,
) : Exception(message)