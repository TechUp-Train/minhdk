package com.minhdk.permissionlesson

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun App(context: PlatformContext) {
    MaterialTheme {
        ImageScreen(context)
    }
}