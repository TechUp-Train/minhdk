package com.apero.composetraining.session5.exercises.tabappexercise.expoler

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.apero.composetraining.session5.exercises.tabappexercise.data.PopularItem
import com.apero.composetraining.session5.exercises.tabappexercise.data.Trending
import com.apero.composetraining.session5.exercises.tabappexercise.expoler.component.CategoryChips
import com.apero.composetraining.session5.exercises.tabappexercise.expoler.component.PopularSection
import com.apero.composetraining.session5.exercises.tabappexercise.expoler.component.RecentSearches
import com.apero.composetraining.session5.exercises.tabappexercise.expoler.component.SearchArea
import com.apero.composetraining.session5.exercises.tabappexercise.expoler.component.TrendingNow

@Composable
fun ExploreScreen(
    recentSearches: List<String>,
    popular: List<PopularItem>,
    categories: List<String>,
    trending: List<Trending>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(8.dp))

        SearchArea()

        RecentSearches(recentSearches)

        PopularSection(popular)

        CategoryChips(categories)

        TrendingNow(trending)

        Spacer(Modifier.height(32.dp))
    }
}