package com.minhdk.githubkmp.data.config.network

data class RequestException(
    val code: Int,
    override val message: String,
) : Exception(message)