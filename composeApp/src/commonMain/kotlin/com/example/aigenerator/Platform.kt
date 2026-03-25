package com.example.aigenerator

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import data.model.Categories
import io.ktor.client.engine.HttpClientEngineFactory
import org.koin.core.KoinApplication

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
expect fun initDependencies(config: KoinApplication.() -> Unit)

// Network
expect fun provideNetworkEngine() : HttpClientEngineFactory<*>

expect fun getSecretKeys(): List<String>

expect fun getDeviceId(): String

expect suspend fun loadLocalImage(context: MultiPlatformContext): List<PlatformImage?>

@Composable
expect fun PlatformImage(
    modifier: Modifier = Modifier,
    image: PlatformImage?
)

expect suspend fun PlatformImage.toByteArray(context: MultiPlatformContext): ByteArray?

@Composable
expect fun rememberPermissionLauncher(permission: MultiPlatformPermission): PermissionLauncher

expect suspend fun readStyles(context: MultiPlatformContext): Categories

expect fun getScreenSize(): Pair<Double, Double>