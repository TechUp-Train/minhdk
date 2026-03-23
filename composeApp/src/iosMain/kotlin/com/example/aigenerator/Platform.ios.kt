package com.example.aigenerator

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.UIKitView
import coil3.PlatformContext
import di.networkModule
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.darwin.Darwin
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.koin.core.context.startKoin
import platform.CoreGraphics.CGSize
import platform.CoreGraphics.CGSizeMake
import platform.Foundation.NSSortDescriptor
import platform.Photos.PHAsset
import platform.Photos.PHAssetMediaTypeImage
import platform.Photos.PHAuthorizationStatusAuthorized
import platform.Photos.PHAuthorizationStatusLimited
import platform.Photos.PHFetchOptions
import platform.Photos.PHFetchResult
import platform.Photos.PHImageContentModeAspectFill
import platform.Photos.PHImageManager
import platform.Photos.PHImageRequestOptions
import platform.Photos.PHImageRequestOptionsDeliveryModeHighQualityFormat
import platform.Photos.PHImageRequestOptionsResizeModeFast
import platform.Photos.PHPhotoLibrary
import platform.UIKit.UIDevice
import platform.UIKit.UIImage
import platform.UIKit.UIImageView
import platform.UIKit.UIViewContentMode
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

actual fun initDependencies() {
    startKoin {
        modules(networkModule)
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
actual fun PlatformImage(image: PlatformImage?) {

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
        modifier = Modifier.size(300.dp)
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