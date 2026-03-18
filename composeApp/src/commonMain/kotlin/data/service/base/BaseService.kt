package data.service.base

import data.network.Response
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.head
import io.ktor.client.request.options
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.http.HttpMethod
import io.ktor.websocket.Frame

interface BaseService {

    fun HttpRequestBuilder.attachDomain(subDomain: String, vararg values: String)

    fun HttpRequestBuilder.addHeaders(headers: Map<String, String>)

    suspend fun <T> withClient(block: suspend HttpClient.() -> T)
}

// just use this for request

private suspend inline fun <reified T> HttpClient.requestMethod(method: HttpMethod, config: (HttpRequestBuilder) -> Unit) : T {
    return when(method) {
        HttpMethod.Get -> get { config(this) }.body<T>()
        HttpMethod.Post -> post { config(this) }.body<T>()
        HttpMethod.Put -> put { config(this) }.body<T>()
        HttpMethod.Delete -> delete { config(this) }.body<T>()
        HttpMethod.Patch -> patch { config(this) }.body<T>()
        HttpMethod.Head -> head { config(this) }.body<T>()
        HttpMethod.Options -> options { config(this) }.body<T>()
        else -> throw Exception("Not supported method!")
    }
}

suspend inline fun <reified T> BaseService.requestInternal(
    method: HttpMethod,
    noinline config: HttpRequestBuilder.() -> Unit
): Response<T> {
    withClient {
        try {
            val result = requestMethod<T>(method) { builder ->
                config(builder)
            } as T
            return Response.Success(result)
        } catch (e: Exception) {
            return Response.Error(
                code = null,
                message = e.message
            )
        }
    }
}