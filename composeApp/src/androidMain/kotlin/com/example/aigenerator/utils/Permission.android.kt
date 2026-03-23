package com.example.aigenerator.utils

import android.Manifest
import android.os.Build
import com.example.aigenerator.MultiPlatformPermission

actual val readImagePermission = object : MultiPlatformPermission() {
    override val permission = listOf(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    )

}