package com.example.kmpday3

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.cinterop.ExperimentalForeignApi
import okio.Path
import okio.Path.Companion.toPath
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask
import platform.UIKit.UIDevice

import kotlinx.cinterop.*
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.cinterop.*
import org.jetbrains.skia.*
import platform.Foundation.*
import platform.posix.memcpy

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

actual abstract class AppContext

actual typealias PlatformDrawable = String

@OptIn(ExperimentalForeignApi::class)
fun getImageCachePath(name: String): Path {
    val cacheDir = NSFileManager.defaultManager
        .URLForDirectory(
            directory = NSCachesDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = true,
            error = null
        )?.path ?: error("Cannot resolve NSCachesDirectory")

    return "$cacheDir/$name".toPath()
}


// I will change later with an optimized solution that downscales before filling the data into ram instead of this stupid code.
// Krop is built on Coil that supports resizing by #size(w, h), but i want to try the native code.
@OptIn(ExperimentalForeignApi::class)
actual fun loadImage(
    context: AppContext,
    res: PlatformDrawable
): ImageBitmap? {
    val path = NSBundle.mainBundle.pathForResource(res, null) ?: return null
    val data = NSData.dataWithContentsOfFile(path) ?: return null
    val bytes = ByteArray(data.length.toInt())
    bytes.usePinned {
        memcpy(it.addressOf(0), data.bytes, data.length)
    }
    val image = Image.makeFromEncoded(bytes)
    val newWidth = image.width / 4
    val newHeight = image.height / 4
    val surface = Surface.makeRasterN32Premul(newWidth, newHeight)
    val canvas = surface.canvas
    canvas.drawImageRect(
        image,
        Rect.makeWH(image.width.toFloat(), image.height.toFloat()),
        Rect.makeWH(newWidth.toFloat(), newHeight.toFloat())
    )
    val scaledImage = surface.makeImageSnapshot()
    return scaledImage.toComposeImageBitmap()
}