package com.example.kmpday2.ui.screens.homes.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.kmpday2.ui.base.TextButton
import com.example.kmpday2.ui.base.reuseBackground
import kmpday2.composeapp.generated.resources.Res
import kmpday2.composeapp.generated.resources.ic_watch
import org.jetbrains.compose.resources.painterResource

@Composable
fun TrendingBannerCard(
    modifier: Modifier = Modifier,
    imageUrl: String,
    tag: String,
    title: String,
    description: String,
    onReadNow: () -> Unit,
    onAdd: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(24.dp))
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
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.75f)
                        ),
                        startY = 200f
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(20.dp)
        ) {

            Box(
                modifier = Modifier
                    .reuseBackground(
                        color = Color(0xFFFF9800),
                        cornerRadius = 10.dp
                    )
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = tag,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }

            Spacer(Modifier.height(10.dp))

            Text(
                text = title,
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = description,
                color = Color.White.copy(alpha = 0.75f),
                fontSize = 14.sp,
                maxLines = 2
            )

            Spacer(Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {

                TextButton(
                    text = "Read Now",
                    bgColor = Color(0xFF9C27B0),
                    textColor = Color.White,
                    cornerRadius = 50.dp,
                    onClick = onReadNow
                )

                Spacer(Modifier.width(12.dp))

                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .reuseBackground(
                            color = Color.White.copy(alpha = 0.18f),
                            cornerRadius = 50.dp
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun TrendingBannerCardPreview() {
    TrendingBannerCard(
        imageUrl = "https://i.imgur.com/5Iqt1fR.png",
        tag = "TRENDING #1",
        title = "Solo Leveling: Arise",
        description = "The world's weakest hunter reaches the pinnacle of strength in a world where rank is...",
        onReadNow = {},
        onAdd = {}
    )
}