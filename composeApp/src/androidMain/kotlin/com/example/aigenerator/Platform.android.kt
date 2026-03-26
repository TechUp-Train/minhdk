package com.example.aigenerator

import android.Manifest
import android.content.ContentUris
import android.content.ContentValues
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import data.model.Categories
import di.networkModule
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import kotlin.coroutines.resume
import androidx.core.net.toUri
import di.appModule
import org.koin.core.KoinApplication
import org.koin.core.context.GlobalContext
import android.content.Intent
import android.content.Context
import android.media.MediaScannerConnection
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Environment
import android.util.Log
import coil3.request.crossfade
import coil3.request.placeholder
import java.io.File
import java.io.FileOutputStream

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

actual fun initDependencies(
    config: KoinApplication.() -> Unit
) {
    startKoin {
        config()
        modules(networkModule, appModule)
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
actual fun PlatformImage(
    modifier: Modifier,
    image: PlatformImage?
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalPlatformContext.current)
            .data(image)
            .size(300)
            .crossfade(true)
            .placeholder(R.drawable.img_placeholder)
            .build(),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
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
            var resumed = false
            onResult = { granted ->
                if (!resumed) {
                    resumed = true
                    cont.resume(granted)
                }
            }
            triggerRequest?.invoke(permission)

            cont.invokeOnCancellation {
                onResult = null
            }
        }
    }
}

actual fun getSecretKeys(): List<String> {
    return listOf(
        BuildConfig.API_KEY,
        BuildConfig.PUBLIC_KEY
    )
}

actual suspend fun PlatformImage.toByteArray(context: MultiPlatformContext): ByteArray? {
    val uri = this.toUri()
    val inputStream = context.contentResolver.openInputStream(uri)
    return inputStream?.readBytes() ?: ByteArray(0)
}

actual fun goToSetting(permission: MultiPlatformPermission) {
    val context = GlobalContext.get().get<Context>()
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = android.net.Uri.fromParts("package", context.packageName, null)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(intent)
}

actual fun checkShouldAskWriteImagePermission(): Boolean {
    return Build.VERSION.SDK_INT < Build.VERSION_CODES.Q
}

actual suspend fun saveToPublicGallery(context: MultiPlatformContext, filename: String, bytes: ByteArray) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val values = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, "${filename}.jpeg")
            put(MediaStore.Downloads.MIME_TYPE, "image/jpeg")
            put(MediaStore.Downloads.RELATIVE_PATH, "Download/MyApp")
        }

        val uri = context.contentResolver.insert(
            MediaStore.Downloads.EXTERNAL_CONTENT_URI,
            values
        ) ?: throw Exception("Failed to create file")

        context.contentResolver.openOutputStream(uri)?.use {
            it.write(bytes)
        } ?: throw Exception("Failed to open stream")

        Log.d("fwejfj", "download image: susssceeee")

    } else {
        val downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val folder = File(downloads, "MyApp")

        if (!folder.exists()) folder.mkdirs()

        val file = File(folder, "${filename}.jpeg")

        withContext(Dispatchers.IO) {
            FileOutputStream(file).use {
                it.write(bytes)
            }
        }

        MediaScannerConnection.scanFile(
            context,
            arrayOf(file.absolutePath),
            arrayOf("image/jpeg"),
            null
        )
    }
}