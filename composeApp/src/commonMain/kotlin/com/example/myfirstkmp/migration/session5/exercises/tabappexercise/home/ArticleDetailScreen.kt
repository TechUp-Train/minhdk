package com.example.myfirstkmp.migration.session5.exercises.tabappexercise.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.myfirstkmp.migration.session5.exercises.tabappexercise.data.sampleFeaturedStories
import com.example.myfirstkmp.migration.session5.exercises.tabappexercise.home.component.article_detail_component.ArticleDetailScreenHeader
import com.example.myfirstkmp.migration.session5.exercises.tabappexercise.home.component.article_detail_component.ArticleParagraph
import com.example.myfirstkmp.migration.session5.exercises.tabappexercise.home.component.article_detail_component.TagChip

@Composable
fun ArticleDetailScreen(
    contentPadding: PaddingValues,
    id: Int,
    navigateBack: () -> Unit
) {

    val article by remember {
        mutableStateOf(
            sampleFeaturedStories.find { it.id == id }
        )
    }

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

        item {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                AsyncImage(
                    model = article?.thumbnail,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .clip(RoundedCornerShape(16.dp))
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = article?.category ?: "Architecture",
                    color = Color(0xFF3B82F6),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = article?.title
                        ?: "The Future of Sustainable Architecture in Urban Environments",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 32.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    AsyncImage(
                        model = article?.thumbnail,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = article?.author?.name ?: "Elena Rodriguez",
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = article?.readTime ?: "• 6 min read",
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                ArticleParagraph(
                    "As the global population continues to shift toward urban centers, the demand for sustainable housing and commercial spaces has reached an all-time high."
                )

                ArticleParagraph(
                    "Sustainable architecture is no longer just a niche trend; it has become a fundamental requirement for the future of cities."
                )

                ArticleParagraph(
                    "By integrating renewable energy technologies, green materials, and innovative design strategies, architects are reshaping how we interact with our built environment."
                )

                ArticleParagraph(
                    "From energy-efficient buildings to eco-conscious urban planning, the future of architecture is rooted in harmony with nature."
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Tags",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    TagChip("Sustainable")
                    TagChip("Architecture")
                    TagChip("Urban")
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}