package data.core.remote.service.image

import data.core.remote.service.base.Response
import data.model.PresignLink
import data.model.PromptResponse
import data.model.Timestamp

interface ImageService {

    suspend fun sendPrompt(
        images: List<ByteArray>,
        mode: String,
        prompt: String
    ): Response<PromptResponse>

    suspend fun downloadImage(url: String): ByteArray?

}