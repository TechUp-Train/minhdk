package com.example.aigenerator

import androidx.compose.ui.window.ComposeUIViewController

fun MainViewController() = ComposeUIViewController {

    initDependencies {
        modules(contextModule)
    }

    App(object: MultiPlatformContext(){})
}