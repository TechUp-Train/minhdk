package di

import com.example.aigenerator.provideNetworkEngine
import data.core.remote.service.image.ImageService
import data.core.remote.service.image.ImageServiceImpl
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule = module {

    single<HttpClient> {

        HttpClient(provideNetworkEngine()) {

            expectSuccess = true

            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    encodeDefaults = true
                    prettyPrint = false
                })
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 30_000
                connectTimeoutMillis = 15_000
                socketTimeoutMillis = 30_000
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        print("KtorClient: $message")
                    }
                }
                level = LogLevel.BODY
            }

            install(HttpRequestRetry) {
                retryOnServerErrors(maxRetries = 3)
                retryOnException(maxRetries = 3, retryOnTimeout = true)
                exponentialDelay()
            }

        }
    }

    single<ImageService> {
        ImageServiceImpl(client = get(), baseHost = "api.github.com")
    }

}