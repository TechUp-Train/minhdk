package com.example.kmpday3

import okio.Path

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect abstract class AppContext