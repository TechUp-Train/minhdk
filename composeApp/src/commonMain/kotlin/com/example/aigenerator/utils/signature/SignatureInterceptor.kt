package com.apero.signature

import io.ktor.client.HttpClientConfig
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.request.headers
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import kotlin.time.Clock

/**
 * Ktor HTTP client interceptor that automatically adds signature-based
 * authentication headers to every outgoing request.
 *
 * ## Headers added
 * | Header             | Description                                      |
 * |--------------------|--------------------------------------------------|
 * | `x-api-signature`  | RSA-encrypted signature (Base64)                 |
 * | `x-api-timestamp`  | Epoch timestamp used in the signature            |
 * | `x-api-bundleId`   | Application bundle identifier                   |
 * | `x-api-token`      | Token placeholder                                |
 * | `App-name`         | Application name                                 |
 * | `country-code`     | ISO 3166-1 alpha-2 country code (optional)       |
 * | `app-version`      | Application version string (optional)            |
 * | `x-api-deviceid`   | Device identifier (optional)                     |
 *
 * ## Usage
 * ```kotlin
 * val client = HttpClient {
 *     installSignatureInterceptor(
 *         apiKey = "...",
 *         publicKey = "-----BEGIN PUBLIC KEY-----\n...",
 *         bundleId = "com.example.app",
 *         appName = "MyApp",
 *         appVersion = "1.0.0",
 *         countryCode = "US",
 *         deviceId = null
 *     )
 * }
 * ```
 */
class SignatureInterceptor(
    private val apiKey: String,
    private val publicKey: String,
    private val bundleId: String,
    private val appName: String,
    private val appVersion: String? = null,
    private val countryCode: String? = null,
    private val deviceId: String? = null,
    private val timestampProvider: () -> Long = { Clock.System.now().epochSeconds }
) {

    /**
     * Installs this interceptor into the given Ktor [HttpClientConfig].
     */
    fun install(config: HttpClientConfig<*>) {
        config.install("SignatureHeaders") {
            requestPipeline.intercept(HttpRequestPipeline.State) {
                val timestamp = timestampProvider()
                val signatureResult = SignatureParser.parseData(
                    apiKey,
                    publicKey,
                    timestamp
                )

                signatureResult.onSuccess { signature ->
                    context.headers {
                        append(HttpHeaders.Accept, ContentType.Application.Json.toString())
                        append(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                        append("x-api-signature", signature.signature)
                        append("x-api-timestamp", signature.timestamp.toString())
                        append("x-api-bundleId", bundleId)
                        append("x-api-token", "not_get_api_token")
                        append("App-name", appName)
                        countryCode?.let { code -> append("country-code", code) }
                        appVersion?.let { version -> append("app-version", version) }
                        deviceId?.let { id -> append("x-api-deviceid", id) }
                    }
                }

                proceed()
            }
        }
    }
}

/**
 * Extension function to easily install [SignatureInterceptor] on a Ktor HttpClient.
 */
fun HttpClientConfig<*>.installSignatureInterceptor(
    apiKey: String,
    publicKey: String,
    bundleId: String,
    appName: String,
    appVersion: String? = null,
    countryCode: String? = null,
    deviceId: String? = null,
    timestampProvider: () -> Long = { Clock.System.now().epochSeconds }
) {
    SignatureInterceptor(
        apiKey, publicKey, bundleId, appName,
        appVersion, countryCode, deviceId, timestampProvider
    ).install(this)
}
