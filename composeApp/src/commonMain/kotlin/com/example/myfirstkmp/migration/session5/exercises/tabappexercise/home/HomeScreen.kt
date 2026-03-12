package com.example.myfirstkmp.migration.session5.exercises.tabappexercise.home
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myfirstkmp.migration.session5.exercises.tabappexercise.data.sampleFeaturedStories
import com.example.myfirstkmp.migration.session5.exercises.tabappexercise.home.component.home_component.ArticleItem
import com.example.myfirstkmp.migration.session5.exercises.tabappexercise.home.component.home_component.HomeScreenHeader

@Composable
fun HomeScreen(
    contentPadding: PaddingValues,
    goToDetailArticleScreen: (Int) -> Unit
) {

    LazyColumn(
        contentPadding = contentPadding,
        modifier = Modifier.fillMaxSize()
    ) {

        item {
            HomeScreenHeader()
        }

        item {
            Text(
                text = "Chayo my boy, Minh!",
                fontSize = 16.sp,
                color = Color(0XFF0062A3),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Featured Stories",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(15.dp))
        }

        items(items = sampleFeaturedStories, key = { item -> item.id }) { item ->
            ArticleItem(
                story = item,
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
            ) {
                goToDetailArticleScreen(item.id)
            }
        }

    }
}