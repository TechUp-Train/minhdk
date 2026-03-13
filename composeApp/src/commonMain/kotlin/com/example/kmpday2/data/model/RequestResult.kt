package com.example.kmpday2.data.model

enum class RequestStatus {
    INITIAL, SUCCESS, ERROR
}

data class RequestResult<T>(
    val data: T? = null,
    val status: RequestStatus = RequestStatus.INITIAL,
    val error: String? = null
)