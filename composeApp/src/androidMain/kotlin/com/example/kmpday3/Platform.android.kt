package com.example.kmpday3

import android.os.Build
import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import okio.Path
import okio.Path.Companion.toPath
import org.jetbrains.compose.resources.DrawableResource
import java.io.File

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual typealias AppContext = Context

actual typealias PlatformDrawable = Int

fun getImageCachePath(
    context: AppContext,
    name: String
): Path {
    val dir = File(context.cacheDir, name)
    if (!dir.exists()) dir.mkdirs()
    return dir.absolutePath.toPath()
}

actual fun loadImage(context: AppContext, res: PlatformDrawable): ImageBitmap? {
    val options = BitmapFactory.Options().apply {
        inSampleSize = 4
        inPreferredConfig = android.graphics.Bitmap.Config.ARGB_8888
    }
    val bitmap = BitmapFactory.decodeResource(
        context.resources,
        res,
        options
    )
    return bitmap.asImageBitmap()
}

