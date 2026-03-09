package com.apero.composetraining.session5.exercises.tab_app_exercise.home.component.article_detail_component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TagChip(text: String) {

    Box(
        modifier = Modifier
            .background(
                color = Color(0xFFE2E8F0),
                shape = RoundedCornerShape(50)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {

        Text(
            text = text,
            fontSize = 14.sp
        )
    }
}