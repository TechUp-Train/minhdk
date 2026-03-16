package com.example.kmpday3

import androidx.compose.ui.window.ComposeUIViewController
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.setSingletonImageLoaderFactory
import com.example.kmpday3.exercises.exercise2.buildImageLoader

@OptIn(ExperimentalCoilApi::class)
fun MainViewController() = ComposeUIViewController {

    setSingletonImageLoaderFactory { context ->
        buildImageLoader(
            context = context,
            cache = getImageCachePath(name = "local_cache_image")
        )
    }

    App()
}