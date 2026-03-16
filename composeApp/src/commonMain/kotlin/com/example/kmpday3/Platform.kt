package com.example.kmpday3

import androidx.compose.ui.graphics.ImageBitmap

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect abstract class AppContext

expect class PlatformDrawable

expect fun loadImage(context: AppContext, res: PlatformDrawable): ImageBitmap?