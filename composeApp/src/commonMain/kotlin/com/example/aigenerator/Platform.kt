package com.example.aigenerator

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform