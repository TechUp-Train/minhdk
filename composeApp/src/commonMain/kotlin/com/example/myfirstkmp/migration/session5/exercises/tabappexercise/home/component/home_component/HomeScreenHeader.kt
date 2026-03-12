package com.example.myfirstkmp.migration.session5.exercises.tabappexercise.home.component.home_component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.WrapText
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.myfirstkmp.migration.session5.exercises.tabappexercise.general.BaseIconButton
import com.example.myfirstkmp.migration.session5.exercises.tabappexercise.general.Header

@Preview
@Composable
fun HomeScreenHeader(modifier: Modifier = Modifier) {
    Header(
        leading = {
            BaseIconButton(
                icon = Icons.AutoMirrored.Filled.WrapText,
                onClick = {

                }
            )
        },
        title = {
            Text(
                text = "Home",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
        },
        actions = {
            BaseIconButton(
                icon = Icons.Default.Notifications,
                onClick = {

                }
            )

            BaseIconButton(
                icon = Icons.Default.Search,
                onClick = {

                }
            )
        }
    )
}