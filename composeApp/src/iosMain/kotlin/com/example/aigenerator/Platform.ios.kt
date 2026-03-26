package com.example.aigenerator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import data.model.Categories
import di.appModule
import di.networkModule
import io.github.vinceglb.filekit.utils.toByteArray
import io.github.vinceglb.filekit.utils.toNSData
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.darwin.Darwin
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import platform.CoreGraphics.CGSize
import platform.CoreGraphics.CGSizeMake
import platform.Foundation.NSBundle
import platform.Foundation.NSDictionary
import platform.Foundation.NSSortDescriptor
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.dictionaryWithContentsOfFile
import platform.Foundation.stringWithContentsOfFile
import platform.Photos.PHAsset
import platform.Photos.PHAssetMediaTypeImage
import platform.Photos.PHFetchOptions
import platform.Photos.PHFetchResult
import platform.Photos.PHImageContentModeAspectFill
import platform.Photos.PHImageManager
import platform.Photos.PHImageRequestOptions
import platform.Photos.PHImageRequestOptionsDeliveryModeHighQualityFormat
import platform.Photos.PHImageRequestOptionsResizeModeFast
import platform.UIKit.UIDevice
import platform.UIKit.UIImage
import platform.UIKit.UIImageView
import platform.UIKit.UIScreen
import platform.UIKit.UIViewContentMode
import platform.UIKit.UIApplicationOpenSettingsURLString
import platform.UIKit.UIApplication
import platform.Foundation.NSURL
import platform.Photos.PHAssetCreationRequest
import platform.Photos.PHAssetResourceTypePhoto
import platform.Photos.PHPhotoLibrary
import kotlin.coroutines.resume


class IOSPlatform : Platform {
    override val name: String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

actual fun getPlatformType() = PlatformType.IOS

actual abstract class MultiPlatformContext

actual typealias PlatformImage = PHAsset

actual abstract class MultiPlatformPermission {
    abstract suspend fun action(): Boolean
}

actual fun provideNetworkEngine(): HttpClientEngineFactory<*> {
    return Darwin
}

actual fun initDependencies(config: KoinApplication.() -> Unit) {
    startKoin {
        config()
        modules(networkModule, appModule)
    }
}

suspend fun loadImages(limit: Int = Int.MAX_VALUE): List<PHAsset> {
    return withContext(Dispatchers.IO) {

        val result = mutableListOf<PHAsset>()
        val options = PHFetchOptions().apply {
            sortDescriptors = listOf(
                NSSortDescriptor(
                    key = "creationDate",
                    ascending = false
                )
            )
        }

        val fetchResult: PHFetchResult =
            PHAsset.fetchAssetsWithMediaType(
                mediaType = PHAssetMediaTypeImage,
                options = options
            )

        val count = minOf(fetchResult.count.toInt(), limit)

        for (i in 0 until count) {
            val asset = fetchResult.objectAtIndex(i.toULong()) as? PHAsset ?: continue
            result.add(asset)
        }

        result
    }
}

@OptIn(ExperimentalForeignApi::class)
suspend fun PHAsset.loadUIImage(
    targetSize: CValue<CGSize> = CGSizeMake(300.0, 300.0)
): UIImage? = suspendCancellableCoroutine { cont ->

    val options = PHImageRequestOptions().apply {
        deliveryMode = PHImageRequestOptionsDeliveryModeHighQualityFormat
        resizeMode = PHImageRequestOptionsResizeModeFast
    }

    PHImageManager.defaultManager().requestImageForAsset(
        asset = this,
        targetSize = targetSize,
        contentMode = PHImageContentModeAspectFill,
        options = options
    ) { image, _ ->
        cont.resume(image)
    }
}

@OptIn(ExperimentalForeignApi::class)
actual suspend fun loadLocalImage(context: MultiPlatformContext): List<PlatformImage?> {
    return loadImages()
}

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun PlatformImage(
    modifier: Modifier,
    image: PlatformImage?
) {

    var data by remember { mutableStateOf<UIImage?>(null) }

    LaunchedEffect(Unit) {
        data = image?.loadUIImage()
    }

    UIKitView(
        factory = {
            UIImageView().apply {
                clipsToBounds = true
                contentMode = UIViewContentMode.UIViewContentModeScaleAspectFill
            }
        },
        update = { view ->
            view.image = data
        },
        modifier = modifier
    )
}

@Composable
actual fun rememberPermissionLauncher(permission: MultiPlatformPermission): PermissionLauncher {
    return remember {
        mutableStateOf(
            object : PermissionLauncher {

                override val permission = permission
                override var triggerRequest: ((MultiPlatformPermission) -> Unit)? = null
                override var onResult: ((Boolean) -> Unit)? = null

                override suspend fun request(): Boolean {
                    return permission.action()
                }
            }
        )
    }.value
}

actual fun getSecretKeys(): List<String> {
    val path = NSBundle.mainBundle.pathForResource(
        name = "Secrets", ofType = "plist"
    ) ?: return emptyList()

    val dict = NSDictionary.dictionaryWithContentsOfFile(path) ?: return emptyList()

    val apiKey = dict["API_KEY"] as? String ?: ""
    val publicKey = dict["PUBLIC_KEY"] as? String ?: ""
    return listOf(apiKey, publicKey)
}

@OptIn(ExperimentalForeignApi::class)
actual suspend fun PlatformImage.toByteArray(context: MultiPlatformContext): ByteArray? {
    return toByteArray()
}

@OptIn(ExperimentalForeignApi::class)
suspend fun PHAsset.toByteArray(): ByteArray? = suspendCancellableCoroutine { continuation ->
    val manager = PHImageManager.defaultManager()
    val options = PHImageRequestOptions().apply {
        deliveryMode = PHImageRequestOptionsDeliveryModeHighQualityFormat
        isSynchronous()
    }

    manager.requestImageDataAndOrientationForAsset(
        asset = this,
        options = options
    ) { data, _, _, _ ->
        if (data != null) {
            continuation.resume(data.toByteArray())
        } else {
            continuation.resume(null)
        }
    }
}

actual fun goToSetting(permission: MultiPlatformPermission) {
    val url = NSURL.URLWithString(UIApplicationOpenSettingsURLString)
    if (url != null) {
        UIApplication.sharedApplication.openURL(url, options = emptyMap<Any?, Any>(), completionHandler = null)
    }
}


actual suspend fun saveToPublicGallery(
    context: MultiPlatformContext,
    filename: String,
    bytes: ByteArray
) {
    val data = bytes.toNSData()

    PHPhotoLibrary.sharedPhotoLibrary().performChanges({
        val request = PHAssetCreationRequest.creationRequestForAsset()
        request.addResourceWithType(
            type = PHAssetResourceTypePhoto,
            data = data,
            options = null
        )
    }, completionHandler = { success, error ->
        if (!success) {
            println("Error saving to Photos: $error")
        }
    })
}

actual fun checkShouldAskWriteImagePermission(): Boolean {
    return true
}