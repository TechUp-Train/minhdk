package com.minhdk.permissionlesson

data class Image(
    val id: String,
    val uri: String? = null,
    val url: String? = null,
    val data: ByteArray? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Image

        if (id != other.id) return false
        if (uri != other.uri) return false
        if (url != other.url) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (uri?.hashCode() ?: 0)
        result = 31 * result + (url?.hashCode() ?: 0)
        result = 31 * result + (data?.contentHashCode() ?: 0)
        return result
    }
}
