package data.core.remote.service.base

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.http.ContentType
import kotlinx.serialization.KSerializer

interface BaseService {

    fun HttpRequestBuilder.fullUrl(url: String)

    fun HttpRequestBuilder.path(subDomain: String, vararg values: String)

    fun HttpRequestBuilder.header(headers: Map<String, Any>)

    fun HttpRequestBuilder.params(params: Map<String, String>)

    fun HttpRequestBuilder.body(body: Any, type: ContentType)

    suspend fun <T> request(method: Method, serializer: KSerializer<T>, config: HttpRequestBuilder.() -> Unit): Response<T>

}