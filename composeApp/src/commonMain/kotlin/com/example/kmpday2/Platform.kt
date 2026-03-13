package com.example.kmpday2

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform