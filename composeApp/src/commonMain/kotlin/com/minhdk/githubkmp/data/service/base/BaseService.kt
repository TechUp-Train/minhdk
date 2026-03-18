package com.minhdk.githubkmp.data.service.base

import com.minhdk.githubkmp.data.network.Response
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.http.HttpMethod
import kotlinx.serialization.KSerializer

interface BaseService {

    fun HttpRequestBuilder.attachDomain(subDomain: String, vararg values: String)

    fun HttpRequestBuilder.addHeaders(headers: Map<String, String>)

    suspend fun <T> request(method: HttpMethod, serializer: KSerializer<T>, config: HttpRequestBuilder.() -> Unit): Response<T>

}