package di

import data.service.github.GithubUserService
import data.service.github.GithubUserServiceImpl
import com.example.kmpday4.configEngine
import com.example.kmpday4.getNetworkEngine
import data.network.RequestException
import data.network.ResponseConverter
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
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
            requestTimeoutMillis = 30_000
            connectTimeoutMillis = 30_000
            socketTimeoutMillis = 30_000
        }

        install(Logging) {
            level = LogLevel.ALL
        }

        install(HttpRequestRetry) {
            retryOnServerErrors(3)
            exponentialDelay()
        }


        /*
        Ktor does not throw exception if not set expectSuccess = true explicit. So using HttpResponseValidator as a thrower
        to expose custom ex for caller
        */
//        HttpResponseValidator {
//            validateResponse { response ->
//                if (!response.status.isSuccess()) {
//                    throw Exception("HTTP ${response.status}")
//                }
//            }
//        }

        configEngine()

    } }

    single<GithubUserService> {
        GithubUserServiceImpl(client = get(), "api.github.com")
    }

}