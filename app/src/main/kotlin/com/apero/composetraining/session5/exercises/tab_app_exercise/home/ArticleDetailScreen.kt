package com.apero.composetraining.session5.exercises.tab_app_exercise.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.apero.composetraining.session5.exercises.tab_app_exercise.home.component.article_detail_component.ArticleDetailScreenHeader

@Composable
fun ArticleDetailScreen(
    contentPadding: PaddingValues,
    id: Int,
    navigateBack: () -> Unit
) {

    LazyColumn(
        contentPadding = contentPadding,
        modifier = Modifier.fillMaxSize()
    ) {

        item {
            ArticleDetailScreenHeader(
                modifier = Modifier.fillMaxWidth()
            ) {
                navigateBack()
            }
        }

    }
}