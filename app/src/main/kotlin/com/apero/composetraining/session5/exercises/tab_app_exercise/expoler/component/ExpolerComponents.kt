package com.apero.composetraining.session5.exercises.tab_app_exercise.expoler.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.apero.composetraining.session5.exercises.tab_app_exercise.data.PopularItem
import com.apero.composetraining.session5.exercises.tab_app_exercise.data.Trending


@Composable
fun SearchArea() {
    OutlinedTextField(
        value = "",
        onValueChange = {},
        placeholder = {
            Text("Search destinations, activities...")
        },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = null)
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(30.dp)
    )
}

@Composable
fun RecentSearches(items: List<String>) {
    Spacer(Modifier.height(20.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            "RECENT SEARCHES",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
        Text(
            "Clear all",
            color = Color(0xFF1E88E5),
            fontSize = 13.sp
        )
    }

    Spacer(Modifier.height(12.dp))

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items.forEach { item ->
            AssistChip(
                onClick = {},
                label = { Text(item) }
            )
        }
    }
}


@Composable
fun PopularSection(popular: List<PopularItem>) {
    Spacer(Modifier.height(24.dp))

    Text(
        "POPULAR SEARCHES",
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Gray
    )

    Spacer(Modifier.height(12.dp))

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            PopularCard(popular[0])
            PopularCard(popular[1])
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            PopularCard(popular[2])
            PopularCard(popular[3])
        }
    }
}

@Composable
fun PopularCard(item: PopularItem) {
    Box(
        modifier = Modifier
            .width(160.dp)
            .height(100.dp)
            .clip(RoundedCornerShape(14.dp))
    ) {
        AsyncImage(
            model = item.imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Black.copy(alpha = 0.3f))
        )
        Text(
            item.title,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}

@Composable
fun CategoryChips(categories: List<String>) {
    Spacer(Modifier.height(14.dp))

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        categories.forEachIndexed { index, item ->
            val selected = index == 0
            AssistChip(
                onClick = {},
                label = { Text(item) },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = if (selected) Color(0xFF0D47A1) else Color(0xFFE3F2FD),
                    labelColor = if (selected) Color.White else Color(0xFF1565C0)
                )
            )
        }
    }
}

@Composable
fun TrendingNow(items: List<Trending>) {
    Spacer(Modifier.height(24.dp))

    Text(
        "TRENDING NOW",
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Gray
    )

    Spacer(Modifier.height(12.dp))

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items) { item ->
            TrendingCard(item)
        }
    }
}

@Composable
fun TrendingCard(item: Trending) {
    Column(
        modifier = Modifier
            .width(260.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
    ) {
        AsyncImage(
            model = item.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .height(150.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                item.title,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )
            Text(
                item.subtitle,
                color = Color.Gray,
                fontSize = 13.sp
            )
        }
    }
}