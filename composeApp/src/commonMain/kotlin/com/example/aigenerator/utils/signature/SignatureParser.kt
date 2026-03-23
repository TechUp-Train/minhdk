package com.apero.signature

/**
 * Platform-agnostic interface for generating RSA-based API signatures.
 *
 * Each platform (Android/iOS) provides its own implementation via [signatureParserImpl].
 *
 * ## How it works
 * 1. A plaintext string is constructed: `"<timestamp>@@@<keyId>@@@<nonce>"`
 * 2. The plaintext is encrypted using RSA/PKCS1 with the provided PEM public key.
 * 3. The encrypted bytes are Base64-encoded to produce the final signature.
 *
 * ## Usage
 * ```kotlin
 * val result = SignatureParser.parseData(
 *     keyId = "your-api-key",
 *     publicKeyPem = "-----BEGIN PUBLIC KEY-----\n...\n-----END PUBLIC KEY-----",
 *     timestamp = System.currentTimeMillis() / 1000
 * )
 * result.onSuccess { data ->
 *     // Use data.signature, data.keyId, data.timestamp
 * }
 * ```
 */
interface SignatureParser {
    /**
     * Generate a signature from the given parameters.
     *
     * @param keyId The API key identifier.
     * @param publicKeyPem The RSA public key in PEM format (X.509/SPKI).
     * @param timestamp The current server-synchronized timestamp (epoch seconds).
     * @return [Result] wrapping [SignatureData] on success, or an exception on failure.
     */
    fun parse(
        keyId: String,
        publicKeyPem: String,
        timestamp: Long
    ): Result<SignatureData>

    companion object {
        /**
         * Convenience function that delegates to the platform-specific implementation.
         */
        fun parseData(
            keyId: String,
            publicKeyPem: String,
            timestamp: Long
        ): Result<SignatureData> = signatureParserImpl()
            .parse(keyId, publicKeyPem, timestamp)
    }
}

/**
 * Expect function — each platform provides its own [SignatureParser] implementation.
 */
expect fun signatureParserImpl(): SignatureParser
