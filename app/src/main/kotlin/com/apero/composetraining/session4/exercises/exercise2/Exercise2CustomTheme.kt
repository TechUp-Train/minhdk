package com.apero.composetraining.session4.exercises.exercise2

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun Lesson2AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val colors =
        if (darkTheme) darkColors
        else lightColors

    MaterialTheme(
        colorScheme = colors,
        typography = typography,
        content = content
    )
}