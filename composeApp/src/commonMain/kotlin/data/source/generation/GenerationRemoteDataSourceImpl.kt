package data.source.generation

import com.example.aigenerator.MultiPlatformContext
import com.example.aigenerator.PlatformImage
import com.example.aigenerator.toByteArray
import data.core.remote.service.base.Response
import data.core.remote.service.image.ImageService
import data.model.PromptResponse
import data.model.Style

class GenerationRemoteDataSourceImpl(
    private val imageService: ImageService,
    private val context: MultiPlatformContext
): GenerationRemoteDataSource {

    override suspend fun generate(prompt: String, images: List<PlatformImage>, style: Style): Response<PromptResponse> {
        val rawImages = (images.map { it.toByteArray(context) } as? List<ByteArray>)
        return rawImages?.let { raw ->
            imageService.sendPrompt(
                raw,
                style.styleMode ?: "",
                style.imagePrompt + "\n" + prompt
            )
        } ?: Response.Error(null, "Extract raw failed !")
    }

}