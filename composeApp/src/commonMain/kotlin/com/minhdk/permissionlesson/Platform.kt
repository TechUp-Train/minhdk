package com.minhdk.permissionlesson

import androidx.compose.runtime.Composable

interface Platform {
    val name: String
}

interface PermissionLauncher<T> {
    var requestCallback: (() -> Unit)?
    fun onResult(granted: Boolean)
    suspend fun request(onRequestCancel: () -> T): Boolean
}

expect fun getPlatform(): Platform

expect fun getPlatformOs(): PlatformOS

expect abstract  class PlatformContext

expect fun hasAccessImagePermission(context: PlatformContext): Boolean

expect fun getPermissionLauncher(): PermissionLauncher<Unit>

@Composable
expect fun ConfigPermissionLauncher(permissionLauncher: PermissionLauncher<*>)

expect suspend fun loadImages(context: PlatformContext): List<Image>
