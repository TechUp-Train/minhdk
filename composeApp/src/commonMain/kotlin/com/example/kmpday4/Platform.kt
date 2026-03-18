package com.example.kmpday4

import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineFactory

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun getNetworkEngine(): HttpClientEngineFactory<*>

expect fun HttpClientConfig<*>.configEngine()