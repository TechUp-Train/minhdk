package data.repo.generation

import com.example.aigenerator.PlatformImage
import data.core.remote.service.base.Response
import data.model.PromptResponse
import data.model.Style
import data.source.generation.GenerationRemoteDataSource

class GenerationRepositoryImpl(
    private val source: GenerationRemoteDataSource
) : GenerationRepository {

    override suspend fun generate(
        prompt: String,
        images: List<PlatformImage>,
        style: Style
    ): Response<PromptResponse> {
        return source.generate(prompt, images, style)
    }

}