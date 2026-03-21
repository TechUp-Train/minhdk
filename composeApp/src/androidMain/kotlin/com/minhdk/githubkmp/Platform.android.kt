package com.minhdk.githubkmp

import android.os.Build
import androidx.room.RoomDatabase
import com.minhdk.githubkmp.config.getDatabaseBuilder
import com.minhdk.githubkmp.data.core.storage.database.AppDatabase
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.okhttp.OkHttp

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual typealias PlatformContext = android.content.Context

actual fun getNetworkEngine(): HttpClientEngineFactory<*> = OkHttp

actual fun HttpClientConfig<*>.configEngine() {
//    engine {
//        (this as io.ktor.client.engine.okhttp.OkHttpConfig).apply {
//            config {
//                retryOnConnectionFailure(false)
//                // add interceptor, authenticate
//            }
//        }
//    }
}

actual fun getGithubApiToken(): String {
    return BuildConfig.GITHUB_API_TOKEN
}

actual fun getDatabaseBuilder(context: PlatformContext): RoomDatabase.Builder<AppDatabase> {
    return getDatabaseBuilder(context)
}