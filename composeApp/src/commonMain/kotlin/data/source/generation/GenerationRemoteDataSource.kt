package data.source.generation

import coil3.Image
import com.example.aigenerator.PlatformImage
import data.core.remote.service.base.Response
import data.model.PromptResponse
import data.model.Style

interface GenerationRemoteDataSource {

    suspend fun generate(prompt: String, images: List<PlatformImage>, style: Style): Response<PromptResponse>

}