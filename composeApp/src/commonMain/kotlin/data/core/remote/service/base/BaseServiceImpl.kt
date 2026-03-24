package data.core.remote.service.base

import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.head
import io.ktor.client.request.header
import io.ktor.client.request.options
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.Url
import io.ktor.http.contentType
import io.ktor.http.fullPath
import io.ktor.http.path
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlin.collections.iterator

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

    private suspend fun <T> HttpClient.requestMethod(method: Method, serializer: KSerializer<T>, config: (HttpRequestBuilder) -> Unit) : T {
        val response = when(method) {
            Method.GET -> get { config(this) }
            Method.POST -> post { config(this) }
            Method.PUT -> put { config(this) }
            Method.DELETE -> delete { config(this) }
            Method.PATCH -> patch { config(this) }
            Method.HEAD -> head { config(this) }
            Method.OPTIONS -> options { config(this) }
        }
        val raw = response.bodyAsText()
        if(serializer == Unit.serializer()) return Unit as T
        return json.decodeFromString(serializer, raw)
    }

    private fun HttpRequestBuilder.build(mProtocol: URLProtocol = URLProtocol.HTTPS, mHost: String, subDomain: String) {
        url { urlBuilder ->
            protocol = mProtocol
            host = mHost
            urlBuilder.path(subDomain)
        }
    }

    override fun HttpRequestBuilder.fullUrl(url: String) {
//        val url = Url(url)
//        build(mHost = url.host, subDomain = url.fullPath)
        url(url)
    }

    override fun HttpRequestBuilder.path(subDomain: String, vararg values: String) {
        build(mHost = baseHost, subDomain = subDomain.mappingPath(*values))
    }

    override fun HttpRequestBuilder.header(headers: Map<String, Any>) {
        (provideStableHeader() + headers).forEach { (key, value) ->
            header(key, value)
        }
    }

    override fun HttpRequestBuilder.params(params: Map<String, String>) {
        for ((key, value) in params) {
            parameter(key, value)
        }
    }

    override fun HttpRequestBuilder.body(body: Any, type: ContentType) {
        setBody(body)
        contentType(type)
    }

    override suspend fun <T> request(method: Method, serializer: KSerializer<T>, config: HttpRequestBuilder.() -> Unit): Response<T> =
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