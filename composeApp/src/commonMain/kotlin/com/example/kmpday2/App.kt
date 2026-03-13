package com.example.kmpday2

import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.setSingletonImageLoaderFactory
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.example.kmpday2.di.dataModule
import com.example.kmpday2.di.viewmodelModule
import com.example.kmpday2.ui.screens.homes.HomeScreen
import com.example.kmpday2.ui.theme.AppTheme
import org.koin.core.context.startKoin
import org.koin.mp.KoinPlatform.startKoin

@OptIn(ExperimentalCoilApi::class)
@Composable
@Preview
fun App() {

    startKoin {
        modules(
            dataModule, viewmodelModule
        )
    }

    AppTheme {

        setSingletonImageLoaderFactory { context ->
            getAsyncImageLoader(context)
        }

        HomeScreen()
    }
}

fun getAsyncImageLoader(context: PlatformContext)=
    ImageLoader.Builder(context).crossfade(true).logger(DebugLogger()).build()