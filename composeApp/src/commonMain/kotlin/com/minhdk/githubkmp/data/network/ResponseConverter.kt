package com.minhdk.githubkmp.data.network

import io.ktor.http.ContentType
import io.ktor.serialization.ContentConverter
import io.ktor.util.reflect.TypeInfo
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.charsets.Charset
import io.ktor.utils.io.readRemaining
import io.ktor.utils.io.readText
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

class ResponseConverter(
    private val json: Json
) : ContentConverter {

    override suspend fun deserialize(
        charset: Charset,
        typeInfo: TypeInfo,
        content: ByteReadChannel
    ): Any {
        val text = content.readRemaining().readText()
        val kotlinType = typeInfo.kotlinType!!
        if(kotlinType != ResponseConverter::class) {
            return Response.Error(code = null, message = "Invalid return type!")
        }
        val typeArgument = kotlinType.arguments.first().type!!
        val innerSerializer = json.serializersModule.serializer(typeArgument)
        val innerValue = json.decodeFromString(innerSerializer, text)
        return try {
            Response.Success(data = innerValue)
        } catch (e: Exception) {
            Response.Error(
                code = null,
                message = e.message
            )
        }
    }

    override suspend fun serialize(
        contentType: ContentType,
        charset: Charset,
        typeInfo: TypeInfo,
        value: Any?
    ) = null
}