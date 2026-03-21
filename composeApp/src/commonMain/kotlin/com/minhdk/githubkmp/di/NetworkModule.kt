package com.minhdk.githubkmp.di

import com.minhdk.githubkmp.data.core.service.github.GithubService
import com.minhdk.githubkmp.data.core.service.github.GithubServiceImpl
import com.minhdk.githubkmp.configEngine
import com.minhdk.githubkmp.getNetworkEngine
import com.minhdk.githubkmp.data.config.network.RequestException
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule = module {

    single { HttpClient(getNetworkEngine()) {

        expectSuccess = false

        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    prettyPrint = true
                }
            )
        }

        HttpResponseValidator {
            validateResponse { response ->
                val code = response.status.value
                if (code in 200..299) return@validateResponse
                throw RequestException(
                    code = response.status.value,
                    message = response.bodyAsText()
                )
            }

            handleResponseExceptionWithRequest { cause, request ->
                // map exception here if needed...
                throw cause
            }
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 10_000
            connectTimeoutMillis = 10_000
            socketTimeoutMillis = 10_000
        }

        install(HttpRequestRetry) {
            retryOnServerErrors(3)
            exponentialDelay()
        }

        configEngine()

    } }

    single<GithubService> {
        GithubServiceImpl(client = get(), "api.github.com")
    }

}