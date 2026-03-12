package com.example.myfirstkmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun formatString(value: Double): String