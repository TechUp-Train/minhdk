package com.example.aigenerator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.compose.LocalPlatformContext
import coil3.compose.setSingletonImageLoaderFactory
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.example.aigenerator.utils.readImagePermission
import data.core.remote.service.base.Response
import data.core.remote.service.image.ImageService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

fun getAsyncImageLoader(context: PlatformContext) =
    ImageLoader.Builder(context).crossfade(true).logger(DebugLogger()).build()

@Composable
fun App(context: MultiPlatformContext) {

    val imageService = koinInject<ImageService>()

    setSingletonImageLoaderFactory { context ->
        getAsyncImageLoader(context)
    }

    val launcher = rememberPermissionLauncher(readImagePermission)
    var image = remember { mutableStateListOf<PlatformImage?>() }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {

//        when(val res = imageService.getPresignLink()) {
//            is Response.Success -> println("Success: ${res.data.data?.url}")
//            is Response.Error -> println("Error: ${res.message}")
//        }

        val granted = launcher.request()
        if (granted) {
            val urls = loadLocalImage(context)
            image.addAll(urls.subList(0, 2))
            println("Total: ${urls.size}")
        }
    }

    MaterialTheme {

        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().padding(20.dp)
        ) {
//            image?.let {
//                PlatformImage(it)
//            }

            //{"statusCode":200,"message":"success","data":{"url":"https://static.aperogroup.ai/ai-core-qwen-editing/20260324/2767fbd4-ba63-4948-827e-ff7e4bf4354a/output/10843d82-dffa-4618-8ff1-52e996ef93c3/output/1774345835163_0.jpeg","path":"ai-core-qwen-editing/20260324/2767fbd4-ba63-4948-827e-ff7e4bf4354a/output/10843d82-dffa-4618-8ff1-52e996ef93c3/output/1774345835163_0.jpeg"},"timestamp":1774345836680}
            Button(
                onClick = {
                    scope.launch(Dispatchers.IO) {
                        (image.map { it?.toByteArray(context) } as? List<ByteArray>)?.let {
                            val res = imageService.sendPrompt(
                                it,
                                "COMBINE_IMAGES",
                                "Combine them then send me a funny image."
                            )
                            when (res) {
                                is Response.Success -> println("success")
                                else -> println("error")
                            }
                        }
//                        image?.toByteArray(context)?.let {
//                            val res = imageService.sendPrompt(listOf(it), "IMAGE_EDITING", "Change this image with blue theme.")
//                            when(res) {
//                                is Response.Success -> println("success")
//                                else -> println("error")
//                            }
//                        }
                    }
                }
            ) {
                Text("Upload")
            }
        }

    }
}