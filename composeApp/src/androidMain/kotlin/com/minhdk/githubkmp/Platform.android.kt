package com.minhdk.githubkmp

import android.os.Build
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.okhttp.OkHttp

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual fun getNetworkEngine(): HttpClientEngineFactory<*> = OkHttp

actual fun HttpClientConfig<*>.configEngine() {
    engine {
        (this as io.ktor.client.engine.okhttp.OkHttpConfig).apply {
            config {
                retryOnConnectionFailure(true)
                // add interceptor, authenticate
            }
        }
    }
}

actual fun getGithubApiToken(): String {
    return BuildConfig.GITHUB_API_TOKEN
}