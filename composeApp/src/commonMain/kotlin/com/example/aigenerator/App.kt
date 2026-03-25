package com.example.aigenerator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import ui.view.themes.AppColors

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
        is ImageResult -> backStack.add(ImageResult)
        is Back -> backStack.removeLast()
    }
}

@Composable
fun App(context: MultiPlatformContext) {

    setSingletonImageLoaderFactory { context ->
        getAsyncImageLoader(context)
    }

    val backStack = rememberNavBackStack(navigationConfig, Main)

    AppTheme {
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLast() },
            entryProvider = entryProvider {
                entry<Main> {
                    MainScreen {
                        handleNavigation(it, backStack)
                    }
                }

                entry<PickImage> {
                    PickImageScreen(
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
                    Box(
                        modifier = Modifier.fillMaxSize().background(color = AppColors.Background)
                    ) {
                        Text(
                            text = "Image Result",
                            fontSize = 20.sp,
                            color = Color.White
                        )
                    }
                }

                entry<Back> {
                    backStack.removeLast()
                }
            }
        )
    }
}

//@Composable
//fun App(context: MultiPlatformContext) {
//
//    val imageService = koinInject<ImageService>()
//
//    setSingletonImageLoaderFactory { context ->
//        getAsyncImageLoader(context)
//    }
//
//    val launcher = rememberPermissionLauncher(readImagePermission)
//    var image = remember { mutableStateListOf<PlatformImage?>() }
//
//    val scope = rememberCoroutineScope()
//
//    LaunchedEffect(Unit) {
//
////        when(val res = imageService.getPresignLink()) {
////            is Response.Success -> println("Success: ${res.data.data?.url}")
////            is Response.Error -> println("Error: ${res.message}")
////        }
//
//        val granted = launcher.request()
//        if (granted) {
//            val urls = loadLocalImage(context)
//            image.addAll(urls.subList(0, 2))
//            println("Total: ${urls.size}")
//        }
//    }
//
//    MaterialTheme {
//
//        Column(
//            verticalArrangement = Arrangement.spacedBy(20.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            modifier = Modifier.fillMaxSize().padding(20.dp)
//        ) {
////            image?.let {
////                PlatformImage(it)
////            }
//
//            //{"statusCode":200,"message":"success","data":{"url":"https://static.aperogroup.ai/ai-core-qwen-editing/20260324/2767fbd4-ba63-4948-827e-ff7e4bf4354a/output/10843d82-dffa-4618-8ff1-52e996ef93c3/output/1774345835163_0.jpeg","path":"ai-core-qwen-editing/20260324/2767fbd4-ba63-4948-827e-ff7e4bf4354a/output/10843d82-dffa-4618-8ff1-52e996ef93c3/output/1774345835163_0.jpeg"},"timestamp":1774345836680}
//            Button(
//                onClick = {
//                    scope.launch(Dispatchers.IO) {
//                        (image.map { it?.toByteArray(context) } as? List<ByteArray>)?.let {
//                            val res = imageService.sendPrompt(
//                                it,
//                                "COMBINE_IMAGES",
//                                "Combine them then send me a funny image."
//                            )
//                            when (res) {
//                                is Response.Success -> println("success: ${res.data.data?.url}")
//                                else -> println("error")
//                            }
//                        }
////                        image?.toByteArray(context)?.let {
////                            val res = imageService.sendPrompt(listOf(it), "IMAGE_EDITING", "Change this image with blue theme.")
////                            when(res) {
////                                is Response.Success -> println("success")
////                                else -> println("error")
////                            }
////                        }
//                    }
//                }
//            ) {
//                Text("Upload")
//            }
//        }
//
//    }
//}