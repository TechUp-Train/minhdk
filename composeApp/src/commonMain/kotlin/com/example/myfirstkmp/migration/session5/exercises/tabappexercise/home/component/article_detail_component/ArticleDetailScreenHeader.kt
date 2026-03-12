package com.example.myfirstkmp.migration.session5.exercises.tabappexercise.home.component.article_detail_component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.myfirstkmp.migration.session5.exercises.tabappexercise.general.BaseIconButton
import com.example.myfirstkmp.migration.session5.exercises.tabappexercise.general.Header

@Composable
fun ArticleDetailScreenHeader(
    modifier: Modifier = Modifier,
    onClickBack: () -> Unit
) {
    Header(
        modifier = modifier,
        leading = {
            BaseIconButton(
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                onClick = {
                    onClickBack()
                }
            )
        },
        title = {
            Text(
                text = "Article",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
        },
        actions = {
            BaseIconButton(
                icon = Icons.Default.Bookmark,
                onClick = {

                }
            )

            BaseIconButton(
                icon = Icons.Default.Share,
                onClick = {

                }
            )
        }
    )
}