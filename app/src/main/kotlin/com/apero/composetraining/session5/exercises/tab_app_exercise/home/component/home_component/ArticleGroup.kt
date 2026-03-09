package com.apero.composetraining.session5.exercises.tab_app_exercise.home.component.home_component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.apero.composetraining.session5.exercises.tab_app_exercise.data.FeaturedStory
import com.apero.composetraining.session5.exercises.tab_app_exercise.general.BaseCard

@Composable
fun ArticleInfo(
    modifier: Modifier = Modifier,
    readingMinutes: String,
    type: String
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .background(Color(0XFFF1F5F9), shape = RoundedCornerShape(10.dp))
                .padding(5.dp)
        ) {
            Text(
                text = "$readingMinutes • $type",
                fontSize = 14.sp,
                maxLines = 1
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null
        )
    }
}

@Composable
fun RowScope.ArticleCardTitle(
    title: String,
    description: String,
    readTime: String,
    category: String
) {
    Column(
        modifier = Modifier.weight(1f)
    ) {

        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = description,
            fontSize = 16.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            color = Color.Black.copy(alpha = 0.5f)
        )

        Spacer(modifier = Modifier.height(20.dp))

        ArticleInfo(
            modifier = Modifier.fillMaxWidth(),
            readingMinutes = readTime,
            type = category
        )
    }
}

@Composable
fun ArticleItem(
    story: FeaturedStory,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {

    BaseCard(
        modifier = modifier
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .clickable { onClick() }
                .padding(12.dp)
        ) {

            ArticleCardTitle(
                title = story.title,
                description = story.description,
                readTime = story.readTime,
                category = story.category
            )

            Spacer(
                modifier = Modifier
                    .width(20.dp)
                    .fillMaxHeight()
            )

            AsyncImage(
                model = story.thumbnail,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(15.dp))
            )
        }
    }
}