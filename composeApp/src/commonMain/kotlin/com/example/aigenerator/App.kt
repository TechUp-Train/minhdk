package com.example.aigenerator

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.compose.setSingletonImageLoaderFactory
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.example.aigenerator.utils.readImagePermission

fun getAsyncImageLoader(context: PlatformContext) =
    ImageLoader.Builder(context).crossfade(true).logger(DebugLogger()).build()

@Composable
fun App(context: MultiPlatformContext) {

    setSingletonImageLoaderFactory { context ->
        getAsyncImageLoader(context)
    }

    val launcher = rememberPermissionLauncher(readImagePermission)
    var image by remember { mutableStateOf<PlatformImage?>(null) }

    LaunchedEffect(Unit) {

        val granted = launcher.request()

        if (granted) {
            val urls = loadLocalImage(context)
            println("Total: ${urls.size}")
            image = urls.firstOrNull()
        }

    }

    MaterialTheme {

        image?.let {
            PlatformImage(it)
        }

    }
}