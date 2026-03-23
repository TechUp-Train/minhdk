package com.apero.signature

import android.util.Base64
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import kotlin.random.Random

actual fun signatureParserImpl(): SignatureParser = SignatureParserAndroidImpl()

/**
 * Android implementation of [SignatureParser].
 *
 * Uses Java Cryptography Architecture (JCA):
 * - `javax.crypto.Cipher` with `RSA/None/PKCS1Padding`
 * - `java.security.KeyFactory` to load X.509-encoded RSA public keys
 * - `android.util.Base64` for encoding
 *
 * ## Plaintext format
 * ```
 * <timestamp>@@@<keyId>@@@<nonce>
 * ```
 * where `nonce` is a random integer in [0, 1_000_000).
 */
private class SignatureParserAndroidImpl : SignatureParser {

    override fun parse(
        keyId: String,
        publicKeyPem: String,
        timestamp: Long
    ): Result<SignatureData> = runCatching {
        val sig = encrypt(timestamp, keyId, publicKeyPem)
        SignatureData(sig, keyId, timestamp)
    }

    private fun encrypt(
        timestamp: Long,
        keyId: String,
        pem: String
    ): String {
        val publicKey = loadKey(pem)
            ?: throw IllegalArgumentException("Invalid RSA public key")
        val nonce = Random.nextInt(0, 1_000_000)
        val plain = "$timestamp@@@$keyId@@@$nonce"
        val cipher = Cipher.getInstance("RSA/None/PKCS1Padding")
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        val encrypted = cipher.doFinal(plain.toByteArray())
        return Base64.encodeToString(encrypted, Base64.NO_WRAP)
    }

    private fun loadKey(pem: String): PublicKey? {
        val clean = pem
            .replace(PEM_HEADER, "")
            .replace(PEM_FOOTER, "")
            .replace("\\s".toRegex(), "")
        val bytes = Base64.decode(clean, Base64.NO_WRAP)
        return try {
            KeyFactory.getInstance("RSA").generatePublic(X509EncodedKeySpec(bytes))
        } catch (_: Exception) {
            null
        }
    }

    private companion object {
        const val PEM_HEADER = "-----BEGIN PUBLIC KEY-----"
        const val PEM_FOOTER = "-----END PUBLIC KEY-----"
    }
}
