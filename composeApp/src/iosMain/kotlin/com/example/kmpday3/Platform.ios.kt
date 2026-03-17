package com.example.kmpday3

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.cinterop.ExperimentalForeignApi
import okio.Path
import okio.Path.Companion.toPath
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask
import platform.UIKit.UIDevice

import com.example.kmpday3.util.ImagePickerDelegate
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.UIKit.UIApplication
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerSourceType
import kotlin.coroutines.resume

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

actual suspend fun getImage(): ByteArray? {
    return suspendCancellableCoroutine { continuation ->
        val picker = UIImagePickerController()
        picker.allowsEditing = true
        picker.sourceType = UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary
        val delegate = ImagePickerDelegate { bytes ->
            continuation.resume(bytes)
        }
        picker.delegate = delegate
        val rootVC = UIApplication.sharedApplication.keyWindow?.rootViewController
        rootVC?.presentViewController(picker, true, null)
    }
}