package data.repo.image

import com.example.aigenerator.PlatformImage
import data.source.image.LocalImageDataSource

class ImageRepositoryImpl(
    private val localImageDataSource: LocalImageDataSource
): ImageRepository {

    override suspend fun loadImage(): List<PlatformImage?> {
        return localImageDataSource.loadImage()
    }

}