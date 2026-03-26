package com.example.aigenerator.utils

import com.example.aigenerator.MultiPlatformPermission

expect val readImagePermission: MultiPlatformPermission

expect val writeImagePermission: MultiPlatformPermission

expect fun hasPermission(permission: MultiPlatformPermission): Boolean
