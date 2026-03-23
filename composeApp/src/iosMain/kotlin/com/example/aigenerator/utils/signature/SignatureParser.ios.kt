package com.apero.signature

import io.ktor.util.decodeBase64Bytes
import io.ktor.util.encodeBase64
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.IntVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.get
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.set
import kotlinx.cinterop.value
import platform.CoreFoundation.CFCopyDescription
import platform.CoreFoundation.CFDataCreate
import platform.CoreFoundation.CFDataGetBytePtr
import platform.CoreFoundation.CFDataGetLength
import platform.CoreFoundation.CFDictionaryCreate
import platform.CoreFoundation.CFDictionaryRef
import platform.CoreFoundation.CFErrorRefVar
import platform.CoreFoundation.CFNumberCreate
import platform.CoreFoundation.CFNumberRef
import platform.CoreFoundation.CFRelease
import platform.CoreFoundation.CFStringRef
import platform.CoreFoundation.CFTypeRef
import platform.CoreFoundation.CFTypeRefVar
import platform.CoreFoundation.kCFAllocatorDefault
import platform.CoreFoundation.kCFNumberIntType
import platform.Security.SecKeyCreateEncryptedData
import platform.Security.SecKeyCreateWithData
import platform.Security.SecKeyRef
import platform.Security.kSecAttrKeyClass
import platform.Security.kSecAttrKeyClassPublic
import platform.Security.kSecAttrKeyType
import platform.Security.kSecAttrKeyTypeRSA
import platform.Security.kSecKeyAlgorithmRSAEncryptionPKCS1
import io.github.vinceglb.filekit.utils.toNSData

actual fun signatureParserImpl(): SignatureParser = SignatureParserIosImpl()

/**
 * iOS implementation of [SignatureParser].
 *
 * Uses Apple Security framework:
 * - `SecKeyCreateWithData` to load DER-encoded RSA public keys
 * - `SecKeyCreateEncryptedData` with `kSecKeyAlgorithmRSAEncryptionPKCS1`
 * - CoreFoundation for memory management
 * - Ktor `encodeBase64()` for output encoding
 *
 * ## Plaintext format
 * ```
 * <timestamp>@@@<keyId>@@@<nonce>
 * ```
 * where `nonce` is a random integer in [0, 1_000_000).
 */
private class SignatureParserIosImpl : SignatureParser {

    override fun parse(
        keyId: String,
        publicKeyPem: String,
        timestamp: Long
    ): Result<SignatureData> = runCatching {
        val sig = encrypt(timestamp, keyId, publicKeyPem)
        SignatureData(sig, keyId, timestamp)
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun encrypt(
        timestamp: Long,
        keyId: String,
        pem: String
    ): String = memScoped {
        val plainText = "$timestamp@@@$keyId@@@${kotlin.random.Random.nextInt(0, 1_000_000)}"
        val nsPlain = plainText.encodeToByteArray().toNSData()
        val rawPtr = nsPlain.bytes
            ?: throw IllegalStateException("NSData.bytes returned null")
        val length = nsPlain.length.toULong()
        val cfPlain = CFDataCreate(
            kCFAllocatorDefault,
            rawPtr.reinterpret(),
            length.toLong()
        ) ?: throw IllegalStateException("CFDataCreate failed for plaintext")
        try {
            val keyRef = loadKey(pem)
            val errVar = alloc<CFErrorRefVar>()
            val encryptedCfData = SecKeyCreateEncryptedData(
                keyRef,
                kSecKeyAlgorithmRSAEncryptionPKCS1,
                cfPlain,
                errVar.ptr
            ) ?: throw IllegalStateException(
                errVar.value?.let { CFCopyDescription(it)?.toString() }
                    ?: "Unknown encryption error"
            )
            try {
                val lengthx = CFDataGetLength(encryptedCfData).toInt()
                val bytePtr = CFDataGetBytePtr(encryptedCfData)
                    ?: throw IllegalStateException("CFDataGetBytePtr returned null")
                val bytes = ByteArray(lengthx) { i -> bytePtr[i].toByte() }
                bytes.encodeBase64()
            } finally {
                CFRelease(encryptedCfData)
            }
        } finally {
            CFRelease(cfPlain)
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun loadKey(pem: String): SecKeyRef = memScoped {
        val b64 = getRawPublicKey(pem)
        val nsData = b64.decodeBase64Bytes().toNSData()
        val rawPtr = nsData.bytes
            ?: throw IllegalStateException("Failed to get bytes from NSData")
        val length = nsData.length
        val cfData = CFDataCreate(kCFAllocatorDefault, rawPtr.reinterpret(), length.toLong())
            ?: throw IllegalStateException("CFDataCreate failed")

        val attrs = (mapOf(
            kSecAttrKeyType to kSecAttrKeyTypeRSA,
            kSecAttrKeyClass to kSecAttrKeyClassPublic
        ) as Map<CFStringRef?, Any>).toCFDictionary()!!

        val errVar = alloc<CFErrorRefVar>()
        val key = SecKeyCreateWithData(cfData, attrs, errVar.ptr)
            ?: throw IllegalArgumentException(
                errVar.value
                    ?.let { CFCopyDescription(it)?.toString() }
                    ?: "Invalid public key"
            )
        key
    }

    private fun getRawPublicKey(pem: String): String = pem
        .replace("-----BEGIN PUBLIC KEY-----", "")
        .replace("-----END PUBLIC KEY-----", "")
        .replace("\\s".toRegex(), "")
}

@OptIn(ExperimentalForeignApi::class)
private fun Int.toCFNumber(): CFNumberRef? {
    memScoped {
        val value = alloc<IntVar>()
        value.value = this@toCFNumber
        return CFNumberCreate(kCFAllocatorDefault, kCFNumberIntType, value.ptr)
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun Map<CFStringRef?, Any>.toCFDictionary(): CFDictionaryRef? {
    val keys = this.keys.toTypedArray()
    val values = this.values.toTypedArray()

    return memScoped {
        val cfKeys = allocArray<CFTypeRefVar>(keys.size)
        val cfValues = allocArray<CFTypeRefVar>(values.size)

        keys.forEachIndexed { i, key -> cfKeys[i] = key }
        values.forEachIndexed { i, value ->
            cfValues[i] = when (value) {
                is CFTypeRef -> value
                is Int -> value.toCFNumber()
                else -> error("Unsupported CFDictionary value: $value")
            }
        }

        CFDictionaryCreate(
            kCFAllocatorDefault,
            cfKeys,
            cfValues,
            keys.size.toLong(),
            null,
            null
        )
    }
}
