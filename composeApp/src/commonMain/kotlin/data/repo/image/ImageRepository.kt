package data.repo.image

import com.example.aigenerator.PlatformImage

interface ImageRepository {

    suspend fun loadImage(): List<PlatformImage?>

}