package ui.view.screen.main.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import data.model.Category
import data.model.Style
import kotlinx.coroutines.launch
import ui.view.state.UiState
import ui.view.themes.AppColors
import ui.view.themes.AppShapes
import ui.view.themes.Space


@Composable
private fun StyleRow(
    styles: List<Style>
) {
    val scrollState = rememberScrollState()

//    val (screenWidth, screenHeight) = getScreenSize()
//    val imageSize = screenWidth.toFloat().pxToDp() * 0.2f

    Row(
        modifier = Modifier.horizontalScroll(scrollState).height(IntrinsicSize.Max)
    ) {
        styles.forEachIndexed { index, style ->
            Row(
                modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxHeight()
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalPlatformContext.current)
                            .data(style.imageUrl)
                            .size(200)
                            .crossfade(true)
                            .build(),
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                        modifier = Modifier.size(120.dp).clip(AppShapes.medium)
                    )

                    Text(
                        text = style.styleName ?: "-----",
                        style = MaterialTheme.typography.bodySmall,
                        color = AppColors.TextPrimary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .width(120.dp)
                            .wrapContentHeight()
                    )
                }

                if(index < styles.size - 1) Spacer(Modifier.width(12.dp).fillMaxHeight())
            }
        }
    }
}

@Composable
fun StyleGroup(
    categories: List<Category>,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { categories.size })
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
    ) {
        ScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            edgePadding = 0.dp,
            containerColor = Color.Transparent,
            divider = {},
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                    color = AppColors.Primary
                )
            }
        ) {
            categories.forEachIndexed { index, cat ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(
                            text = cat.categoryName ?: "-----",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if(index == pagerState.currentPage) AppColors.Primary else AppColors.PrimaryLight
                        )
                    }
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        HorizontalPager(
            state = pagerState,
            userScrollEnabled = false,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            categories.getOrNull(page)?.let { cat ->
                cat.styles?.let {
                    StyleRow(it)
                }
            }
        }
    }
}

@Composable
fun StyleGroupWithState(
    categories: State<UiState<List<Category>?>>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = "Choose your style",
            style = MaterialTheme.typography.headlineSmall,
            color = AppColors.TextSecondary
        )

        Spacer(modifier = Modifier.height(Space.x12))

        when(categories.value) {
            is UiState.Loading -> {}
            is UiState.Success -> {
                (categories.value as UiState.Success).data?.let {
                    StyleGroup(
                        categories = it,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            is UiState.Error -> {}
        }
    }
}

