package com.minhdk.githubkmp

import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import coil3.ImageLoader
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.minhdk.githubkmp.data.core.storage.database.AppDatabase
import com.minhdk.githubkmp.di.appModule
import com.minhdk.githubkmp.di.databaseModule
import com.minhdk.githubkmp.di.networkModule
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineFactory
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect abstract class PlatformContext

@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

expect fun getDatabaseBuilder(context: PlatformContext): RoomDatabase.Builder<AppDatabase>

expect fun getNetworkEngine(): HttpClientEngineFactory<*>

expect fun HttpClientConfig<*>.configEngine()

expect fun getGithubApiToken(): String

fun initAndroidDependencies(
    platformInitialization: KoinApplication.() -> Unit = {}
) {
    startKoin {
        platformInitialization()
        modules(networkModule, databaseModule, appModule)
    }
}

fun initIosDependencies() {
    startKoin {
        modules(networkModule, appModule)
    }
}

fun getAsyncImageLoader(context: coil3.PlatformContext)=
    ImageLoader.Builder(context).crossfade(true).logger(DebugLogger()).build()

