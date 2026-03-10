package com.apero.composetraining.session5.exercises.tab_app_exercise.expoler.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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



@Composable
fun TopSearchHeader(
    title: String,
    totalResults: Int,
    onBack: () -> Unit,
    onFilterClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(top = 12.dp, bottom = 4.dp)
            .fillMaxWidth()
    ) {
        Icon(
            Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
                .clickable { onBack() }
        )

        Spacer(Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                title,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                "$totalResults results found",
                color = Color.Gray,
                fontSize = 13.sp
            )
        }

        Icon(
            Icons.Default.FilterList,
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
                .clickable { onFilterClick() }
        )
    }
}


@Composable
fun FilterChips(filters: List<String>) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(filters.size) { index ->
            val selected = index == 0
            FilterChip(
                selected = selected,
                onClick = {},
                label = {
                    Text(
                        filters[index],
                        fontSize = 14.sp,
                        color = if (selected) Color.White else Color(0xFF0D47A1)
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF0D47A1),
                    containerColor = Color(0xFFE3F2FD)
                )
            )
        }
    }
}


@Composable
fun ArticleCard(article: ResultArticle) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (article.tag != null) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF1976D2))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            article.tag,
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(Modifier.width(6.dp))
                }

                Text(
                    article.date,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Spacer(Modifier.height(6.dp))

            Text(
                article.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(6.dp))

            Text(
                article.description,
                fontSize = 14.sp,
                color = Color.Gray,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(10.dp))

            Text(
                "Read More →",
                color = Color(0xFF1976D2),
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
        }

        Spacer(Modifier.width(12.dp))

        AsyncImage(
            model = article.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .width(90.dp)
                .height(90.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )
    }
}