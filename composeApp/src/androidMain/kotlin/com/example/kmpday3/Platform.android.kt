package com.example.kmpday3

import android.os.Build
import android.content.Context
import okio.Path
import okio.Path.Companion.toPath
import java.io.File

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

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

lateinit var activity: ComponentActivity   // cần inject activity từ Android host

actual suspend fun getImage(): ByteArray? {
    return suspendCancellableCoroutine { continuation ->
        val launcher = activity.registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            if (uri == null) {
                continuation.resume(null)
                return@registerForActivityResult
            }
            val bytes = activity.contentResolver.openInputStream(uri)?.use { it.readBytes() }
            continuation.resume(bytes)
        }
        launcher.launch("image/*")
    }
}