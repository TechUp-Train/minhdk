package data.repo.image

import aigenerator.composeapp.generated.resources.Res
import com.example.aigenerator.MultiPlatformContext
import com.example.aigenerator.PlatformImage
import com.example.aigenerator.saveToPublicGallery
import data.core.remote.service.base.Response
import data.core.remote.service.image.ImageService
import data.source.image.LocalImageDataSource
import kotlin.js.ExperimentalJsExport
import kotlin.time.Clock

class ImageRepositoryImpl(
    private val localImageDataSource: LocalImageDataSource,
    private val imageService: ImageService,
    private val context: MultiPlatformContext
): ImageRepository {

    override suspend fun loadImage(): List<PlatformImage?> {
        return localImageDataSource.loadImage()
    }

    override suspend fun downloadImage(url: String): Response<Unit> {
        try {
            imageService.downloadImage(url)?.let { raw ->
                saveToPublicGallery(
                    context,
                    Clock.System.now().epochSeconds.toString(),
                    raw)
            }
            return Response.Success(Unit)
        } catch (e: Exception) {
            return Response.Error(null, e.toString())
        }
    }

}