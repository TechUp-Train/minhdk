package com.example.aigenerator.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.example.aigenerator.MultiPlatformPermission
import org.koin.core.context.GlobalContext

actual val readImagePermission = object : MultiPlatformPermission() {
    override val permission = listOf(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    )

}

// Implement for me how to check whether the permission is granted
actual fun hasPermission(permission: MultiPlatformPermission): Boolean {
    val context = GlobalContext.get().get<Context>()
    return permission.permission.all { perm ->
        ContextCompat.checkSelfPermission(context, perm) == PackageManager.PERMISSION_GRANTED
    }
}