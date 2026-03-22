package com.example.aigenerator

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.size
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.compose.AsyncImage
import coil3.compose.setSingletonImageLoaderFactory
import coil3.request.crossfade
import coil3.util.DebugLogger
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText

@Composable
@Preview
fun App() {

    setSingletonImageLoaderFactory { context ->
        getAsyncImageLoader(context)
    }

//    LaunchedEffect(Unit) {
//        HttpClient().use { client ->
//            val response = client.get("https://api.github.com/users/octocat")
//            println(response.bodyAsText())
//        }
//    }

    MaterialTheme {

//        AsyncImage(
//            model = "https://d28clw9klscyzj.cloudfront.net/legacy/assets/2016-01-31/files/1045.jpg",
//            contentDescription = "Android Robot",
//            modifier = Modifier.size(300.dp)
//        )
    }
}

fun getAsyncImageLoader(context: PlatformContext)=
    ImageLoader.Builder(context).crossfade(true).logger(DebugLogger()).build()