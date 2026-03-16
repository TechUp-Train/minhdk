package com.example.kmpday3

import android.os.Build
import android.content.Context
import okio.Path
import okio.Path.Companion.toPath
import java.io.File

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual typealias AppContext = Context

fun getImageCachePath(
    context: AppContext,
    name: String
): Path {
    val dir = File(context.cacheDir, name)
    if (!dir.exists()) dir.mkdirs()
    return dir.absolutePath.toPath()
}