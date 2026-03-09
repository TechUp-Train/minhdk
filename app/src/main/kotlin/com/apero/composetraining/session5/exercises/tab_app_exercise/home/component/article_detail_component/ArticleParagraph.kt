package com.apero.composetraining.session5.exercises.tab_app_exercise.home.component.article_detail_component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ArticleParagraph(text: String) {

    Text(
        text = text,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        color = Color(0xFF334155),
        modifier = Modifier.padding(bottom = 16.dp)
    )
}