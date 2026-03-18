package com.minhdk.githubkmp

import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineFactory

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun getNetworkEngine(): HttpClientEngineFactory<*>

expect fun HttpClientConfig<*>.configEngine()

expect fun getGithubApiToken(): String