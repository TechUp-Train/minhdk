package com.apero.composetraining.session5.exercises.tabappexercise.expoler

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.apero.composetraining.session5.exercises.tabappexercise.data.ResultArticle
import com.apero.composetraining.session5.exercises.tabappexercise.expoler.component.ArticleCard
import com.apero.composetraining.session5.exercises.tabappexercise.expoler.component.FilterChips
import com.apero.composetraining.session5.exercises.tabappexercise.expoler.component.TopSearchHeader


// -----------------------------------------------------
// MAIN SCREEN
// -----------------------------------------------------
@Composable
fun SearchResultScreen(
    title: String,
    totalResults: Int,
    filters: List<String>,
    onBack: () -> Unit = {},
    onFilterClick: () -> Unit = {},
    articles: List<ResultArticle>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {

        TopSearchHeader(title, totalResults, onBack, onFilterClick)

        FilterChips(filters)

        Spacer(Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(articles) { article ->
                ArticleCard(article)
            }
            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}
