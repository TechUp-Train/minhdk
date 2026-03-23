package data.source.image

import com.example.aigenerator.PlatformImage

interface LocalImageDataSource {

    suspend fun loadImage(): List<PlatformImage?>

}