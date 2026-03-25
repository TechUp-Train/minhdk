package com.example.aigenerator.utils

import com.example.aigenerator.MultiPlatformPermission
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Photos.PHAuthorizationStatusAuthorized
import platform.Photos.PHAuthorizationStatusLimited
import platform.Photos.PHPhotoLibrary

actual val readImagePermission = object: MultiPlatformPermission() {

    override suspend fun action(): Boolean = suspendCancellableCoroutine { cont ->
        PHPhotoLibrary.requestAuthorization { status ->
            val granted = status == PHAuthorizationStatusAuthorized ||
                    status == PHAuthorizationStatusLimited
            if (cont.isActive) cont.resume(granted) { _, _, _ -> }
        }
    }
}

actual fun hasPermission(permission: MultiPlatformPermission): Boolean {
    if (permission !== readImagePermission) return false

    val status = PHPhotoLibrary.authorizationStatus()
    return status == PHAuthorizationStatusAuthorized ||
            status == PHAuthorizationStatusLimited
}