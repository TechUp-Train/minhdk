package com.minhdk.githubkmp

import androidx.room.RoomDatabase
import com.minhdk.githubkmp.config.getDatabaseBuilder
import com.minhdk.githubkmp.data.core.storage.database.AppDatabase
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.darwin.Darwin
import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

actual abstract class PlatformContext

actual fun getNetworkEngine(): HttpClientEngineFactory<*> = Darwin

actual fun HttpClientConfig<*>.configEngine() {}

actual fun getGithubApiToken(): String {
    return ""
}

actual fun getDatabaseBuilder(context: PlatformContext): RoomDatabase.Builder<AppDatabase> {
    return getDatabaseBuilder()
}