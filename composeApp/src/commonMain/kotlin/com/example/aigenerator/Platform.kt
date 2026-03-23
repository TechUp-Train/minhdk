package com.example.aigenerator

import androidx.compose.runtime.Composable
import io.ktor.client.engine.HttpClientEngineFactory

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun getPlatformType(): PlatformType

expect abstract class MultiPlatformContext

expect class PlatformImage

expect abstract class MultiPlatformPermission

interface PermissionLauncher {
    abstract val permission: MultiPlatformPermission
    var triggerRequest: ((MultiPlatformPermission) -> Unit)?
    var onResult: ((Boolean) -> Unit)?
    abstract suspend fun request(): Boolean
}

// dependencies
expect fun initDependencies()

// Network
expect fun provideNetworkEngine() : HttpClientEngineFactory<*>

expect suspend fun loadLocalImage(context: MultiPlatformContext): List<PlatformImage?>

@Composable
expect fun PlatformImage(image: PlatformImage?)

@Composable
expect fun rememberPermissionLauncher(permission: MultiPlatformPermission): PermissionLauncher
