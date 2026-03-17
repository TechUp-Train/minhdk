package com.example.kmpday3

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import coil3.annotation.ExperimentalCoilApi
import com.example.kmpday3.exercises.exercise3.UploadImageScreen

@OptIn(ExperimentalCoilApi::class)
@Composable
@Preview
fun App() {
    MaterialTheme {
        UploadImageScreen()
    }
}
