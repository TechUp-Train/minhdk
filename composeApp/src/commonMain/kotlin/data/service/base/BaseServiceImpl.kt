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
import io.ktor.http.URLProtocol
import io.ktor.http.path
import io.ktor.util.appendAll

open class BaseServiceImpl(
    private val client: HttpClient,
    private val baseHost: String
): BaseService {

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

    override fun HttpRequestBuilder.attachDomain(subDomain: String, vararg values: String) {
        url {
            protocol = URLProtocol.HTTPS
            host = baseHost
            path(subDomain.mappingPath(*values))
        }
    }

    override fun HttpRequestBuilder.addHeaders(headers: Map<String, String>) {
        this.headers.appendAll(headers)
    }

    override suspend fun <T> withClient(block: suspend (HttpClient) -> T) {
        block(client)
    }

//    override suspend fun <T> requestInternal(method: HttpMethod, config: HttpRequestBuilder.() -> Unit): Response<T> {
//        try {
//            val result = client.requestMethod(method) { builder ->
//                config(builder)
//            } as T
//            return Response.Success(result)
//        } catch (e: Exception) {
//            return Response.Error(
//                code = null,
//                message = e.message
//            )
//        }
//    }

}