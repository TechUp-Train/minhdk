package com.example.kmpday3.exercises

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.kmpday3.exerciscedata.bannerImageUrls

@Composable
fun ImageItem(
    url: String,
    modifier: Modifier = Modifier
) {

    AsyncImage(
        model = ImageRequest.Builder(LocalPlatformContext.current)
            .data(url)
            .crossfade(true)
            .build(),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
    )

    Spacer(modifier = Modifier.height(20.dp))
}

@Composable
fun ImageList(
    modifier: Modifier = Modifier
) {

    LazyColumn(
        contentPadding = PaddingValues(12.dp),
        modifier = modifier
    ) {

        items(items = bannerImageUrls, key = { it }) {
            ImageItem(
                it,
                Modifier.fillMaxWidth().height(350.dp)
            )
        }

    }

}