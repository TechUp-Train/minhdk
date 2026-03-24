package data.core.remote.service.image

import com.apero.signature.SignatureParser
import com.example.aigenerator.getSecretKeys
import data.core.remote.service.base.BaseServiceImpl
import data.core.remote.service.base.Method
import data.core.remote.service.base.Response
import data.model.PresignLink
import data.model.Prompt
import data.model.Timestamp
import io.ktor.client.HttpClient
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlin.coroutines.resume
import kotlin.time.Clock

class ImageServiceImpl(
    client: HttpClient,
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
    ): Response<List<String>> = suspendCancellableCoroutine { cont ->
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

        result.onSuccess { data ->
            cont.resume(
                Response.Success(
                    listOf(
                        data.timestamp.toString(), data.signature
                    )
                )
            )
        }

        result.onFailure { error ->
            cont.resume(Response.Error(null, error.message))
        }
    }

    override suspend fun requestTimestamp(): Response<Timestamp> {
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

    override suspend fun getPresignLink(): Response<PresignLink> {
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

    override suspend fun uploadImageToCloud(image: ByteArray): Response<String> {
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
    ): Response<String> {
        val files = mutableListOf<String>()
        images.forEach { image ->
            val placeHolder = when (val res = uploadImageToCloud(image)) {
                is Response.Success -> res.data
                else -> return Response.Error(
                    null,
                    "Can not send prompt due to missing placeholder"
                )
            }
            files.add(placeHolder)
        }
        val prompt = Prompt(files, mode, prompt)
        return requestWithKeys { timestamp, signature ->
            request(Method.POST, String.serializer()) {
                path("/api/v5.1/qwen-editing")
                header(
                    mapOf(
                        "x-api-signature" to signature,
                        "x-api-timestamp" to timestamp
                    )
                )
                body(
                    Json.encodeToString(Prompt.serializer(), prompt),
                    ContentType.Application.Json
                )
            }
        }
    }

}
