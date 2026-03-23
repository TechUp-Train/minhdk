package com.example.aigenerator

import androidx.compose.ui.window.ComposeUIViewController

fun MainViewController() = ComposeUIViewController {

    initDependencies()

    App(object: MultiPlatformContext(){})
}