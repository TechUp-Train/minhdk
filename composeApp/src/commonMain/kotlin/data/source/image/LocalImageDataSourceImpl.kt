package data.source.image

import com.example.aigenerator.MultiPlatformContext
import com.example.aigenerator.PlatformImage
import com.example.aigenerator.loadLocalImage

class LocalImageDataSourceImpl(
    private val context: MultiPlatformContext
): LocalImageDataSource {

    override suspend fun loadImage(): List<PlatformImage?> {
        return loadLocalImage(context)
    }

}