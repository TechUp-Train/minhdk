package com.example.kmpday2.ui.screens.homes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.kmpday2.data.model.UiState
import com.example.kmpday2.ui.base.StateComposable
import com.example.kmpday2.ui.screens.homes.components.home.HomeScreenHeader
import com.example.kmpday2.ui.screens.homes.components.home.NewUpdatesSection
import com.example.kmpday2.ui.screens.homes.components.home.TrendingBannerFailed
import com.example.kmpday2.ui.screens.homes.components.home.TrendingBannerPager
import com.example.kmpday2.ui.screens.homes.components.home.TrendingBannerPlaceholder
import com.example.kmpday2.ui.theme.DarkBackground
import com.example.kmpday2.ui.viewmodel.MangaViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen() {
    val viewModel: MangaViewModel = koinViewModel()
    val mangaState = viewModel.mangaState.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(DarkBackground).padding(12.dp)
    ) {
        item {
            HomeScreenHeader()
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            StateComposable(
                state = mangaState.value,
                loading = {
                    TrendingBannerPlaceholder()
                },
                success = {
                    TrendingBannerPager(it)
                },
                error = {
                    TrendingBannerFailed {
                        
                    }
                }
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            NewUpdatesSection()
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}


