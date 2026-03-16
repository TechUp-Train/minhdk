package com.example.kmpday3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.setSingletonImageLoaderFactory
import com.example.kmpday3.exercises.exercise2.buildImageLoader

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalCoilApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {

            setSingletonImageLoaderFactory { context ->
                buildImageLoader(context = context, cache = getImageCachePath(this, "local_cache_image"))
            }

            App()
        }
    }
}