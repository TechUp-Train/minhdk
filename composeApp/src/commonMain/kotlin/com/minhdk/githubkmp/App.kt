package com.minhdk.githubkmp

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImage
import coil3.compose.setSingletonImageLoaderFactory
import com.minhdk.githubkmp.ui.state.UiState
import com.minhdk.githubkmp.ui.viewmodel.ProfileViewModel
import org.koin.compose.viewmodel.koinViewModel


@OptIn(ExperimentalCoilApi::class)
@Composable
@Preview
fun App() {

    setSingletonImageLoaderFactory { context ->
        getAsyncImageLoader(context)
    }

    val viewmodel: ProfileViewModel = koinViewModel()
    val user = viewmodel.userState.collectAsStateWithLifecycle()

    MaterialTheme {
        if (user.value is UiState.Success) {
            val data = (user.value as UiState.Success).data
            data?.let {
                ProfileCard(
                    it.avatarUrl,
                    it.name,
                    it.username,
                    it.bio.ifBlank {
                        "Building things that make other builders faster. OSS contributor by day, rabbit-hole explorer by night. Currently obsessed with KMP, clean architecture, and shipping stuff that actually works."
                    },
                    it.publicRepos,
                    it.followers,
                    it.following
                )
            }
        }
    }
}

@Composable
fun ProfileCard(
    imageUrl: String,
    name: String,
    username: String,
    bio: String,
    repo: Int,
    followers: Int,
    following: Int
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFEFEF)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .width(300.dp)
                .wrapContentHeight(),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Avatar
                Box {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(90.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF6C63FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Name
                Text(
                    text = name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                // Username
                Text(
                    text = username,
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Bio
                Text(
                    text = bio,
                    fontSize = 13.sp,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Stats
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    StatItem(repo.toString(), "REPOS")
                    StatItem(followers.toString(), "FOLLOWERS")
                    StatItem(following.toString(), "FOLLOWING")
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Button
                Button(
                    onClick = {},
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6C63FF)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text("Follow Profile")
                }
            }
        }
    }
}

@Composable
fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}
