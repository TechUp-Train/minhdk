package data.core.remote.service.image

import data.core.remote.service.base.Response
import data.model.PresignLink
import data.model.Timestamp

interface ImageService {

    suspend fun requestTimestamp(): Response<Timestamp>

    suspend fun getPresignLink(): Response<PresignLink>

    suspend fun uploadImageToCloud(image: ByteArray): Response<String>

    suspend fun sendPrompt(
        images: List<ByteArray>,
        mode: String,
        prompt: String
    ): Response<String>

}