package com.minhdk.permissionlesson

import androidx.compose.runtime.Composable
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import platform.CoreGraphics.CGSizeMake
import platform.Foundation.NSData
import platform.Foundation.NSMutableData
import platform.Foundation.NSSortDescriptor
import platform.Foundation.appendData
import platform.Foundation.getBytes
import platform.Photos.PHAsset
import platform.Photos.PHAssetMediaTypeImage
import platform.Photos.PHAssetResource
import platform.Photos.PHAssetResourceManager
import platform.Photos.PHAssetResourceRequestOptions
import platform.Photos.PHAssetResourceTypePhoto
import platform.Photos.PHAuthorizationStatusAuthorized
import platform.Photos.PHAuthorizationStatusDenied
import platform.Photos.PHAuthorizationStatusLimited
import platform.Photos.PHAuthorizationStatusNotDetermined
import platform.Photos.PHAuthorizationStatusRestricted
import platform.Photos.PHFetchOptions
import platform.Photos.PHFetchResult
import platform.Photos.PHImageContentModeAspectFill
import platform.Photos.PHImageManager
import platform.Photos.PHImageRequestOptions
import platform.Photos.PHImageRequestOptionsDeliveryModeHighQualityFormat
import platform.Photos.PHImageRequestOptionsDeliveryModeOpportunistic
import platform.Photos.PHImageRequestOptionsResizeModeFast
import platform.Photos.PHPhotoLibrary
import platform.UIKit.UIDevice
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.darwin.ByteVar
import platform.darwin.DISPATCH_TIME_FOREVER
import platform.darwin.dispatch_semaphore_create
import platform.darwin.dispatch_semaphore_signal
import platform.darwin.dispatch_semaphore_wait
import platform.posix.memcpy
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.let

class IOSPlatform : Platform {
    override val name: String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

actual abstract class PlatformContext

actual fun hasAccessImagePermission(context: PlatformContext): Boolean {
    val status = PHPhotoLibrary.authorizationStatus()
    return status == PHAuthorizationStatusAuthorized ||
            status == PHAuthorizationStatusLimited
}

actual fun getPermissionLauncher(): PermissionLauncher<Unit> {
    return object : PermissionLauncher<Unit> {

        override var requestCallback: (() -> Unit)? = null

        override fun onResult(granted: Boolean) {}

        override suspend fun request(onRequestCancel: () -> Unit): Boolean =
            suspendCancellableCoroutine { cont ->
                PHPhotoLibrary.requestAuthorization { status ->
                    val granted = status == PHAuthorizationStatusAuthorized ||
                            status == PHAuthorizationStatusLimited
                    if (cont.isActive) cont.resume(granted) { _, _, _ ->
                        onRequestCancel()
                    }
                }
            }
    }
}

actual fun getPlatformOs(): PlatformOS = PlatformOS.IOS

@Composable
actual fun ConfigPermissionLauncher(permissionLauncher: PermissionLauncher<*>) {}


@OptIn(ExperimentalForeignApi::class)
fun NSData.toByteArray(): ByteArray {
    val size = this.length.toInt()
    if (size == 0) return ByteArray(0)
    return ByteArray(size).apply {
        usePinned { pinned ->
            memcpy(pinned.addressOf(0), this@toByteArray.bytes, this@toByteArray.length)
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
actual suspend fun loadImages(context: PlatformContext): List<Image> = withContext(Dispatchers.IO) {
    val fetchOptions = PHFetchOptions().apply {
        sortDescriptors = listOf(
            NSSortDescriptor(key = "creationDate", ascending = false)
        )
    }

    val fetchResult = PHAsset.fetchAssetsWithMediaType(
        mediaType = PHAssetMediaTypeImage,
        options = fetchOptions
    )

    val images = mutableListOf<Image>()

    fetchResult.enumerateObjectsUsingBlock { obj, _, _ ->
        val asset = obj as? PHAsset ?: return@enumerateObjectsUsingBlock
        images.add(
            Image(
                id = asset.localIdentifier,
                uri = asset.localIdentifier
            )
        )
    }

    images
}
