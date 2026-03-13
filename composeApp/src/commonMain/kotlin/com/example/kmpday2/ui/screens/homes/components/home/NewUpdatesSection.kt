package com.example.kmpday2.ui.screens.homes.components.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.kmpday2.ui.base.reuseBackground
import kmpday2.composeapp.generated.resources.Res
import kmpday2.composeapp.generated.resources.ic_watch
import org.jetbrains.compose.resources.painterResource

@Composable
private fun UpdateItem(
    imageUrl: String = "https://files01.duytan.edu.vn/svruploads/ktiengnhat-duytan/upload/images/638565444451441731-anime-nhat-ban.png",
    title: String,
    genre: String,
    chapter: String
) {
    Column(
        modifier = Modifier.width(140.dp)
    ) {

        Box(
            modifier = Modifier
                .height(185.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
        ) {

            AsyncImage(
                model = imageUrl,
                placeholder = painterResource(Res.drawable.ic_watch),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .reuseBackground(
                        color = Color(0xFFB388FF),
                        cornerRadius = 10.dp
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .align(Alignment.TopStart)
            ) {
                Text(
                    text = chapter,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = title,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = genre,
            color = Color.Gray,
            fontSize = 13.sp,
            maxLines = 1
        )
    }
}

@Preview
@Composable
fun NewUpdatesSection(
    modifier: Modifier = Modifier,
    onViewAll: () -> Unit = {}
) {
    Column(modifier = modifier.fillMaxWidth()) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .width(3.dp)
                        .height(22.dp)
                        .background(Color(0xFFFF6D00))
                )

                Spacer(Modifier.width(8.dp))

                Text(
                    text = "New Updates",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "View All",
                color = Color(0xFFBB86FC),
                fontSize = 14.sp
            )
        }

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(start = 16.dp, end = 8.dp)
        ) {
            UpdateItem(
                title = "Jujutsu Kaisen",
                genre = "Action, Fantasy",
                chapter = "CH. 254"
            )

            Spacer(Modifier.width(14.dp))

            UpdateItem(
                title = "One Piece",
                genre = "Adventure",
                chapter = "CH. 1110"
            )

            Spacer(Modifier.width(14.dp))

            UpdateItem(
                title = "Spy x Family",
                genre = "Comedy, Spy",
                chapter = "CH. 88"
            )
        }
    }
}