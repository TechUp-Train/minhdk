package com.minhdk.githubkmp.data.core.service.base
import com.minhdk.githubkmp.data.config.network.Response
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.head
import io.ktor.client.request.header
import io.ktor.client.request.options
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpMethod
import io.ktor.http.URLProtocol
import io.ktor.http.path
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

open class BaseServiceImpl(
    private val client: HttpClient,
    private val baseHost: String
): BaseService {

    private val json = Json { ignoreUnknownKeys = true }

    private fun String.mappingPath(vararg values: String, key: String = "/", start: String = "{", end: String = "}"): String {
        var count = 0
        val paths = mutableListOf<String>()
        split(key).forEachIndexed { _, string ->
            paths.add(
                if(string.startsWith(start) && string.endsWith(end)) {
                    if(count >= values.size) throw Exception("missing value for $string")
                    values[count++]
                } else {
                    string
                }
            )
        }
        return paths.joinToString("/")
    }

    private suspend fun <T> HttpClient.requestMethod(method: HttpMethod, serializer: KSerializer<T>, config: (HttpRequestBuilder) -> Unit) : T {
        val response = when(method) {
            HttpMethod.Get -> get { config(this) }
            HttpMethod.Post -> post { config(this) }
            HttpMethod.Put -> put { config(this) }
            HttpMethod.Delete -> delete { config(this) }
            HttpMethod.Patch -> patch { config(this) }
            HttpMethod.Head -> head { config(this) }
            HttpMethod.Options -> options { config(this) }
            else -> throw Exception("Not supported method!")
        }.bodyAsText()
        return json.decodeFromString(serializer, response)
    }

    override fun HttpRequestBuilder.path(subDomain: String, vararg values: String) {
        url {
            protocol = URLProtocol.HTTPS
            host = baseHost
            path(subDomain.mappingPath(*values))
        }
    }

    override fun HttpRequestBuilder.header(headers: Map<String, String>) {
        (provideStableHeader() + headers).forEach { (key, value) ->
            header(key, value)
        }
    }

    override suspend fun <T> request(method: HttpMethod, serializer: KSerializer<T>, config: HttpRequestBuilder.() -> Unit): Response<T> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                Response.Success(client.requestMethod(method, serializer) { builder ->
                    config(builder)
                })
            } catch (e: Exception) {
                Response.Error(
                    code = null,
                    message = e.message
                )
            }
        }

    open fun provideStableHeader(): Map<String, String> = emptyMap()

}