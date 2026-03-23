package data.core.remote.service.base

import io.ktor.client.request.HttpRequestBuilder
import kotlinx.serialization.KSerializer

interface BaseService {

    fun HttpRequestBuilder.path(subDomain: String, vararg values: String)

    fun HttpRequestBuilder.header(headers: Map<String, String>)

    fun HttpRequestBuilder.params(params: Map<String, String>)

    suspend fun <T> request(method: Method, serializer: KSerializer<T>, config: HttpRequestBuilder.() -> Unit): Response<T>

}