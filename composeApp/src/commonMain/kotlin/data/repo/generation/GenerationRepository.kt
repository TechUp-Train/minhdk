package data.repo.generation

import com.example.aigenerator.PlatformImage
import data.core.remote.service.base.Response
import data.model.PromptResponse
import data.model.Style

interface GenerationRepository {

    suspend fun generate(prompt: String, images: List<PlatformImage>, style: Style): Response<PromptResponse>

}