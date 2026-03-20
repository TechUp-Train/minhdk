package com.minhdk.githubkmp.data.config.network

sealed class Response<out T> {
    class Success<T>(val data: T): Response<T>()
    class Error(val code: Int?, val message: String?): Response<Nothing>()
}