package com.apero.composetraining.session5.exercises.tab_app_exercise.expoler

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.apero.composetraining.session5.exercises.tab_app_exercise.data.ResultArticle
import com.apero.composetraining.session5.exercises.tab_app_exercise.expoler.component.ArticleCard
import com.apero.composetraining.session5.exercises.tab_app_exercise.expoler.component.FilterChips
import com.apero.composetraining.session5.exercises.tab_app_exercise.expoler.component.TopSearchHeader


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
