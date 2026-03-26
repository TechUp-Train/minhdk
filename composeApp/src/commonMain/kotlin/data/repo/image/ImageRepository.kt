package data.repo.image

import com.example.aigenerator.PlatformImage
import data.core.remote.service.base.Response

interface ImageRepository {

    suspend fun loadImage(): List<PlatformImage?>

    suspend fun downloadImage(url: String): Response<Unit>

}