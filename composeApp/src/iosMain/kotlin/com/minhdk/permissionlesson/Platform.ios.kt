package com.minhdk.permissionlesson

import platform.Photos.PHAuthorizationStatusAuthorized
import platform.Photos.PHAuthorizationStatusLimited
import platform.Photos.PHPhotoLibrary
import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

actual abstract class PlatformContext

actual fun hasAccessImagePermission(context: PlatformContext): Boolean {
    val status = PHPhotoLibrary.authorizationStatus()
    return status == PHAuthorizationStatusAuthorized ||
            status == PHAuthorizationStatusLimited
}