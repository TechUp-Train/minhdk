package com.example.kmpday2.ui.screens.homes.components.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kmpday2.data.model.Manga
import com.example.kmpday2.ui.base.TextButton
import com.example.kmpday2.ui.base.shimmerEffect
import com.example.kmpday2.ui.screens.homes.intents.BannerCardIntent
import kotlinx.datetime.Month

@Composable
fun TrendingBannerPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .padding(16.dp)
            .clip(RoundedCornerShape(20.dp))
            .shimmerEffect()
    )
}

@Composable
fun TrendingBannerFailed(
    sendIntent: (BannerCardIntent) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Something went wrong !",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        TextButton(
            text = "Retry",
            modifier = Modifier.padding(7.dp)
        ) {
            sendIntent(BannerCardIntent.Reload)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PageIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxWidth()
    ) {
        repeat(pagerState.pageCount) { index ->
            val selected = pagerState.currentPage == index
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(if (selected) 10.dp else 8.dp)
                    .clip(CircleShape)
                    .background(
                        if (selected) Color.White
                        else Color.White.copy(alpha = 0.3f)
                    )
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TrendingBannerPager(
    data: List<Manga>
) {

    val pagerState = rememberPagerState(pageCount = { data.size })

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize().height(400.dp),
            key = { page -> data[page].id }
        ) { page ->
            val item = data[page]
            TrendingBannerCard(
                imageUrl = item.imageUrl,
                tag = "TRENDING #$page",
                title = item.title,
                description = item.description,
                onReadNow = {},
                onAdd = {}
            )
        }

        Spacer(Modifier.height(12.dp))

        PageIndicator(pagerState = pagerState)
    }
}