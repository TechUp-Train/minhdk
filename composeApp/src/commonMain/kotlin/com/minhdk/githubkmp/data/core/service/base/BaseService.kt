package com.minhdk.githubkmp.data.core.service.base

import com.minhdk.githubkmp.data.config.network.Response
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.http.HttpMethod
import kotlinx.serialization.KSerializer

interface BaseService {

    fun HttpRequestBuilder.path(subDomain: String, vararg values: String)

    fun HttpRequestBuilder.header(headers: Map<String, String>)

    suspend fun <T> request(method: HttpMethod, serializer: KSerializer<T>, config: HttpRequestBuilder.() -> Unit): Response<T>

}