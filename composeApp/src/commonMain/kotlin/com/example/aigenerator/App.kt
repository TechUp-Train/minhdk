package com.example.aigenerator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.crossfade
import coil3.util.DebugLogger
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import okio.FileSystem
import ui.view.exchanger.AppExchanger
import ui.view.navigation.Back
import ui.view.navigation.Graph
import ui.view.navigation.ImageResult
import ui.view.navigation.Main
import ui.view.navigation.PickImage
import ui.view.screen.main.MainScreen
import ui.view.themes.AppTheme
import ui.view.navigation.navigationConfig
import ui.view.screen.pickimage.PickImageScreen
import ui.view.screen.result.ImageResultScreen

fun getAsyncImageLoader(context: PlatformContext) =
    ImageLoader.Builder(context)
        .components {
            add(KtorNetworkFetcherFactory())
        }
        .memoryCache {
            MemoryCache.Builder()
                .maxSizePercent(context, 0.25)
                .build()
        }
        .diskCache {
            DiskCache.Builder()
                .directory(FileSystem.SYSTEM_TEMPORARY_DIRECTORY / "image_cache")
                .maxSizeBytes(1024L * 1024L * 100L) // 100MB
                .build()
        }
        .crossfade(true)
        .logger(DebugLogger())
        .build()

private fun handleNavigation(
    destination: Graph,
    backStack: NavBackStack<NavKey>
) {
    when(destination) {
        is Main -> backStack.add(Main)
        is PickImage -> backStack.add(PickImage)
        is ImageResult -> backStack.add(ImageResult(destination.response))
        is Back -> backStack.removeLastOrNull()
    }
}

@Composable
fun App(context: MultiPlatformContext) {

    setSingletonImageLoaderFactory { context ->
        getAsyncImageLoader(context)
    }

    val backStack = rememberNavBackStack(navigationConfig, Main)

    AppTheme {
        Scaffold { paddingValues ->
            NavDisplay(
                backStack = backStack,
                onBack = { backStack.removeLast() },
                entryProvider = entryProvider {
                    entry<Main> {
                        MainScreen(paddingValues) {
                            handleNavigation(it, backStack)
                        }
                    }

                    entry<PickImage> {
                        PickImageScreen(
                            paddingValues,
                            { selectedImages ->
                                AppExchanger.exchangePickImageToMainPickImages.trySend(selectedImages)
                                handleNavigation(Back, backStack)
                            },
                            {
                                handleNavigation(it, backStack)
                            }
                        )
                    }

                    entry<ImageResult> {

                        val input = backStack.lastOrNull() as? ImageResult

                        input?.let {
                            ImageResultScreen(
                                result = input.response,
                                padding = paddingValues
                            ) {
                                handleNavigation(it, backStack = backStack)
                            }
                            return@entry
                        }
                    }

                    entry<Back> {
                        backStack.removeLast()
                    }
                }
            )
        }
    }
}