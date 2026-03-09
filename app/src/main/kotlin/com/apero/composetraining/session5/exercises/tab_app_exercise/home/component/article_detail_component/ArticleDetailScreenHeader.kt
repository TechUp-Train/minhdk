package com.apero.composetraining.session5.exercises.tab_app_exercise.home.component.article_detail_component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.WrapText
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.apero.composetraining.session5.exercises.tab_app_exercise.general.BaseIconButton
import com.apero.composetraining.session5.exercises.tab_app_exercise.general.Header

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