package com.example.aigenerator

import android.Manifest
import android.content.ContentUris
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import di.networkModule
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.koin.core.context.startKoin
import kotlin.coroutines.resume

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual typealias PlatformImage = String

actual typealias MultiPlatformContext = android.content.Context

actual abstract class MultiPlatformPermission {
    abstract val permission: List<String>
}

actual fun provideNetworkEngine(): HttpClientEngineFactory<*> {
    return OkHttp
}

actual fun initDependencies() {
    startKoin {
        modules(networkModule)
    }
}

actual suspend fun loadLocalImage(context: MultiPlatformContext): List<PlatformImage?> =
    withContext(Dispatchers.IO) {
        val images = mutableListOf<String>()

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

                images.add(uri)
            }
        }

        images.toList()
    }

actual fun getPlatformType() = PlatformType.ANDROID

@Composable
actual fun PlatformImage(image: PlatformImage?) {
    AsyncImage(
        model = ImageRequest.Builder(LocalPlatformContext.current)
            .data(image)
            .size(200)
            .build(),
        contentDescription = null,
        modifier = Modifier.size(200.dp)
    )
}

@Composable
actual fun rememberPermissionLauncher(permission: MultiPlatformPermission): PermissionLauncher {
    val permissionLauncher by remember { mutableStateOf(
        createPermissionLauncher(permission)
    ) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        permissionLauncher.onResult?.invoke(!result.values.any { !it })
    }
    LaunchedEffect(Unit) {
        permissionLauncher.triggerRequest = {
            launcher.launch(permission.permission.toTypedArray())
        }
    }
    return permissionLauncher
}

private fun createPermissionLauncher(permission: MultiPlatformPermission): PermissionLauncher {
    return object : PermissionLauncher {
        override val permission = permission
        override var triggerRequest: ((MultiPlatformPermission) -> Unit)? = null
        override var onResult: ((Boolean) -> Unit)? = null

        override suspend fun request(): Boolean = suspendCancellableCoroutine { cont ->
            onResult = { granted ->
                cont.resume(granted)
            }
            triggerRequest?.invoke(permission)
        }

    }
}
