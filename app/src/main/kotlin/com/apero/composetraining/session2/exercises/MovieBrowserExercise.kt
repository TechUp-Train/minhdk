package com.apero.composetraining.session2.exercises

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apero.composetraining.R
import com.apero.composetraining.common.AppTheme
import com.apero.composetraining.common.SampleData
import kotlin.math.cos

/**
 * ⭐⭐ BONUS: Movie Browser (tham khảo NewsFeedExercise)
 *
 * Yêu cầu:
 * - LazyRow trên cùng: "Trending" horizontal scroll (poster 120x180dp)
 * - LazyColumn phía dưới: "All Movies" vertical list
 * - Mỗi movie item: Row(Poster + Column(Title + Year + Rating))
 * - Scaffold với TopAppBar "🎬 Movies"
 */

data class MyMovie(
    val id: Int,
    val title: String,
    val year: Int,
    val rating: Double
)

private val movies = listOf(
    MyMovie(1, "Movie 1", 2023, 8.5),
    MyMovie(2, "Movie 2", 2022, 8.5),
    MyMovie(3, "Movie 3", 2021, 8.4),
    MyMovie(4, "Movie 4", 2023, 8.5),
    MyMovie(5, "Movie 5", 2021, 8.4),
    MyMovie(6, "Movie 6", 2022, 8.5),
    MyMovie(7, "Movie 7", 2021, 8.4),
    MyMovie(8, "Movie 8", 2023, 8.5),
    MyMovie(9, "Movie 9", 2020, 8.3),
    MyMovie(10, "Movie 10", 2022, 8.6)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieBrowserScreen() {

    Scaffold(
        topBar = {
            Topbar(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray)
            )
        }
    ) { padding ->
        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(padding)
        ) {

            item {
                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Image(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = null
                    )

                    Text(
                        text = "Trendings",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(start = 12.dp)
                    )

                }

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                MovieGroup(
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Image(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = null
                    )

                    Text(
                        text = "All Movies",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(start = 12.dp)
                    )

                }
            }

            items(items = movies, key = { movie -> movie.id }) { item ->
                VerticalMovieItem(
                    movie = item,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                )
            }

        }
    }
}

@Preview
@Composable
private fun Topbar(
    modifier: Modifier = Modifier,
    onClickMore: () -> Unit = {}
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f)
        ) {

            Image(
                imageVector = Icons.Default.Movie,
                contentDescription = null
            )

            Text(
                text = "My Movies",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 12.dp)
            )

        }

        IconButton(
            onClick = onClickMore,
            modifier = Modifier.padding(5.dp)
        ) {

            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = null
            )

        }

    }

}

@Preview
@Composable
private fun MovieGroup(
    modifier: Modifier = Modifier,
    movies: List<MyMovie> = com.apero.composetraining.session2.exercises.movies
) {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    LazyRow(
        contentPadding = PaddingValues(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
    ) {

        items(items = movies, key = { movie -> movie.id }) { item ->
            MovieItem(
                item = item,
                modifier = Modifier
                    .width(screenWidth * 0.2f)
                    .aspectRatio(2f / 3f)
            )
        }
    }
}

@Preview
@Composable
private fun AllMovieGroup(
    modifier: Modifier = Modifier,
    movies: List<MyMovie> = com.apero.composetraining.session2.exercises.movies
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 12.dp),
        modifier = modifier
    ) {

        items(items = movies, key = { movie -> movie.id }) { item ->
            VerticalMovieItem(
                movie = item,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            )
        }
    }
}

@Composable
fun MovieItem(
    modifier: Modifier = Modifier,
    item: MyMovie = movies.first(),
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {

        Image(
            painter = ColorPainter(color = Color.Cyan.copy(alpha = 0.3f)),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
        )

        Text(
            text = item.title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(start = 12.dp)
        )

    }
}

@Preview
@Composable
fun VerticalMovieItem(
    modifier: Modifier = Modifier,
    movie: MyMovie = movies.first()
) {
    Column(
        modifier = modifier
    ) {

        HorizontalDivider(
            thickness = 2.dp,
            color = Color.LightGray,
        )

        Spacer(
            modifier = Modifier.height(5.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
        ) {

            Image(
                painter = ColorPainter(Color.Cyan.copy(alpha = 0.5f)),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(100.dp)
                    .height(130.dp)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {

                Text(
                    text = movie.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = movie.year.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = "Rating: ${movie.rating}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(5.dp))


            }

        }

    }
}

@Preview(showBackground = true)
@Composable
private fun MovieBrowserScreenPreview() {
    AppTheme { MovieBrowserScreen() }
}
