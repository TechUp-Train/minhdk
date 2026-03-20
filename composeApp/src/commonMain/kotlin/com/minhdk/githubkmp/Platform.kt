package com.minhdk.githubkmp

import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.minhdk.githubkmp.data.core.storage.database.AppDatabase
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineFactory

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