package data.core.remote.service.image

import com.apero.signature.SignatureParser
import com.example.aigenerator.getSecretKeys
import data.core.remote.service.base.BaseServiceImpl
import data.core.remote.service.base.Method
import data.core.remote.service.base.Response
import data.model.PresignLink
import data.model.PromptRequest
import data.model.PromptResponse
import data.model.Timestamp
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.readBytes
import io.ktor.client.statement.readRawBytes
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.isActive
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlin.coroutines.resume
import kotlin.time.Clock

class ImageServiceImpl(
    private val client: HttpClient,
    baseHost: String
) : BaseServiceImpl(client, baseHost), ImageService {

    override fun provideStableHeader(): Map<String, String> {
        return mapOf(
            HttpHeaders.Accept to ContentType.Application.Json.toString(),
            HttpHeaders.ContentType to ContentType.Application.Json.toString(),
            "x-api-bundleId" to "for.techtrek",
            "x-api-token" to "not_get_api_token",
            "App-name" to "Tecktrek",
            "country-code" to "US",
            "app-version" to "1.0.0",
            "x-api-deviceid" to "23012003"
        )
    }

    private suspend fun generateSignature(
        timeStamp: Long
    ): Response<List<String>>  {
        var apiKey = ""
        var publicKey = ""
        getSecretKeys().let {
            apiKey = it.firstOrNull() ?: ""
            publicKey = it.lastOrNull() ?: ""
        }
        val result = SignatureParser.parseData(
            keyId = apiKey,
            publicKeyPem = publicKey,
            timestamp = timeStamp
        )

        if(result.isSuccess) {
            return result.getOrNull()?.let { data ->
                Response.Success(
                    listOf(
                        data.timestamp.toString(), data.signature
                    )
                )
            } ?: Response.Error(null, "Timestamp null")
        } else {
            return Response.Error(null, result.exceptionOrNull()?.message)
        }
    }

    private suspend fun requestTimestamp(): Response<Timestamp> {
        var timestamp: Long
        val signature = when (val res = generateSignature(Clock.System.now().epochSeconds)) {
            is Response.Success -> {
                timestamp = res.data.first().toLong()
                res.data.last()
            }

            else -> return Response.Error(code = null, message = "No signature provided!")
        }
        val res = request(Method.GET, Timestamp.serializer()) {
            fullUrl("https://video-gen-core.aperogroup.ai/timestamp")
            header(
                mapOf(
                    "x-api-signature" to signature,
                    "x-api-timestamp" to timestamp.toString()
                )
            )
        }
        return res
    }

    private suspend fun <T> requestWithKeys(request: suspend (String, String) -> Response<T>): Response<T> {
        val timestamp = when (val res = requestTimestamp()) {
            is Response.Success -> res.data.data?.timestamp ?: 0L
            else -> return Response.Error(code = null, message = "No timestamp provided!")
        }
        val signature = when (val res = generateSignature(timestamp)) {
            is Response.Success -> {
                res.data.last()
            }

            else -> return Response.Error(code = null, message = "No signature provided!")
        }
        return request(timestamp.toString(), signature)
    }

    private suspend fun getPresignLink(): Response<PresignLink> {
        return requestWithKeys { timestamp, signature ->
            request(Method.GET, PresignLink.serializer()) {
                path("api/v5.1/qwen-editing/presigned-link")
                header(
                    mapOf(
                        "x-api-signature" to signature,
                        "x-api-timestamp" to timestamp
                    )
                )
            }
        }
    }

    private suspend fun uploadImageToCloud(image: ByteArray): Response<String> {
        val uploadInfo = when (val res = getPresignLink()) {
            is Response.Success -> res.data.data ?: run {
                return Response.Error(null, "Upload link is null!")
            }

            else -> return Response.Error(null, "Upload link not found!")
        }
        val url = uploadInfo.url ?: return Response.Error(null, "Url for uploading not found!")
        val imagePlaceHolder =
            uploadInfo.path ?: return Response.Error(null, "Placeholder for AI service not found!")
        return requestWithKeys { timestamp, signature ->
            val response = request(Method.PUT, Unit.serializer()) {
                fullUrl(url)
                header(
                    mapOf(
                        "x-api-signature" to signature,
                        "x-api-timestamp" to timestamp
                    )
                )
                body(image, ContentType.parse("image/jpg"))
            }
            return@requestWithKeys when (response) {
                is Response.Success -> Response.Success(imagePlaceHolder)
                is Response.Error -> Response.Error(null, "Upload Image Failed!")
            }
        }
    }

    override suspend fun sendPrompt(
        images: List<ByteArray>,
        mode: String,
        prompt: String
    ): Response<PromptResponse> {
        println("BenjaminLogging: Push images to cloud")
        val files = mutableListOf<String>()
        images.forEach { image ->
            val placeHolder = when (val res = uploadImageToCloud(image)) {
                is Response.Success -> res.data
                else -> {
                    return Response.Error(
                        null,
                        "Can not send prompt due to missing placeholder"
                    )
                }
            }
            files.add(placeHolder)
        }
        val prompt = PromptRequest(files, mode, prompt)
        println("BenjaminLogging: Start gen: ${prompt}")
        return requestWithKeys { timestamp, signature ->
            request(Method.POST, PromptResponse.serializer()) {
                path("/api/v5.1/qwen-editing")
                header(
                    mapOf(
                        "x-api-signature" to signature,
                        "x-api-timestamp" to timestamp
                    )
                )
                body(
                    Json.encodeToString(PromptRequest.serializer(), prompt),
                    ContentType.Application.Json
                )
            }
        }
    }

    override suspend fun downloadImage(url: String): ByteArray? = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = client.get(url)
            response.readRawBytes()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}
