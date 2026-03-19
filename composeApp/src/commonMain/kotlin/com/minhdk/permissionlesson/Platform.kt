package com.minhdk.permissionlesson

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect abstract  class PlatformContext

expect fun hasAccessImagePermission(context: PlatformContext): Boolean
