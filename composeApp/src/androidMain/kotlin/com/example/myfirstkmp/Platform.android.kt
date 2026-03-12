package com.example.myfirstkmp

import android.annotation.SuppressLint
import android.os.Build

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

@SuppressLint("DefaultLocale")
actual fun formatString(value: Double): String {
    return "$${String.format("%.2f", value)}"
}