package com.example.kmpday2.ui.screens.homes.intents

sealed class BannerCardIntent {
    data object Reload : BannerCardIntent()
    data class Read(val id: Int) : BannerCardIntent()
    data class Add(val id: Int) : BannerCardIntent()
    data class Click(val id: Int) : BannerCardIntent()
}