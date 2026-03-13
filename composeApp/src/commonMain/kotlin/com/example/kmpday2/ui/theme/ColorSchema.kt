package com.example.kmpday2.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val MangaDarkColors = darkColorScheme(
    primary = PurplePrimary,
    secondary = PurpleSecondary,
    tertiary = BlueAccent,
    background = DarkBackground,
    surface = DarkSurface,
    onPrimary = Color.White,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

val MangaLightColors = lightColorScheme(
    primary = PurplePrimary,
    secondary = PurpleSecondary,
    background = Color.White,
    surface = Color(0xFFF6F6F6),
    onPrimary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black
)