package com.example.kmpday2.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf

data class Spacing(
    val xs: Float = 4f,
    val sm: Float = 8f,
    val md: Float = 16f,
    val lg: Float = 24f,
    val xl: Float = 32f
)

val LocalSpacing = staticCompositionLocalOf { Spacing() }