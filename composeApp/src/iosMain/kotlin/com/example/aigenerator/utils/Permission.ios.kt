package com.example.aigenerator.utils

import com.example.aigenerator.MultiPlatformPermission
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSProcessInfo
import platform.Photos.PHAccessLevel
import platform.Photos.PHAccessLevelAddOnly
import platform.Photos.PHAuthorizationStatus
import platform.Photos.PHAuthorizationStatusAuthorized
import platform.Photos.PHAuthorizationStatusLimited
import platform.Photos.PHPhotoLibrary
import platform.UIKit.UIDevice
import kotlin.coroutines.resume

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

@OptIn(ExperimentalForeignApi::class)
actual val writeImagePermission = object: MultiPlatformPermission() {

    override suspend fun action(): Boolean = suspendCancellableCoroutine { cont ->

        val osVersion = UIDevice.currentDevice.systemVersion
        val majorVersion = osVersion.split(".").firstOrNull()?.toIntOrNull() ?: 0

        if (majorVersion >= 14) {
            PHPhotoLibrary.requestAuthorizationForAccessLevel(PHAccessLevelAddOnly) { status ->
                if (cont.isActive) cont.resume(
                    status == PHAuthorizationStatusAuthorized || status == PHAuthorizationStatusLimited
                )
            }
        } else {
            PHPhotoLibrary.requestAuthorization { status ->
                if (cont.isActive) cont.resume(status == PHAuthorizationStatusAuthorized)
            }
        }

    }

}