package com.example.kmpday4

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform