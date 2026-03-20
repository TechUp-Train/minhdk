package com.minhdk.permissionlesson

import android.Manifest
import android.content.ContentUris
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual typealias PlatformContext = Context

actual fun getPlatformOs(): PlatformOS = PlatformOS.ANDROID

actual fun hasAccessImagePermission(context: Context): Boolean {
    val permission = if (Build.VERSION.SDK_INT >= 33)
        Manifest.permission.READ_MEDIA_IMAGES
    else
        Manifest.permission.READ_EXTERNAL_STORAGE

    return ContextCompat.checkSelfPermission(
        context,
        permission
    ) == PackageManager.PERMISSION_GRANTED
}

actual fun getPermissionLauncher(): PermissionLauncher<Unit> {
    return object : PermissionLauncher<Unit> {
        override var requestCallback: (() -> Unit)? = null

        private var sendResult: ((Boolean) -> Unit)? = null

        override fun onResult(granted: Boolean) {
            sendResult?.invoke(granted)
        }

        override suspend fun request(onRequestCancel: () -> Unit): Boolean =
            suspendCancellableCoroutine { continuation ->
                sendResult = { granted ->
                    continuation.resume(granted)
                }
                requestCallback?.invoke()
            }
    }
}

fun getImagePermissionByVersion(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }
}

@Composable
actual fun ConfigPermissionLauncher(permissionLauncher: PermissionLauncher<*>) {
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        permissionLauncher.onResult(granted)
    }

    permissionLauncher.requestCallback = {
        launcher.launch(getImagePermissionByVersion())
    }
}

actual suspend fun loadImages(context: PlatformContext) = withContext(Dispatchers.IO) {
    val images = mutableListOf<Image>()

    val projection = arrayOf(
        MediaStore.Images.Media._ID
    )

    val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

    val query = context.contentResolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        projection,
        null,
        null,
        sortOrder
    )

    query?.use { cursor ->
        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

        while (cursor.moveToNext()) {
            val id = cursor.getLong(idColumn)
            val uri = ContentUris.withAppendedId(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                id
            ).toString()

            images.add(Image(id = id.toString(), uri = uri))
        }
    }

    images.toList()
}