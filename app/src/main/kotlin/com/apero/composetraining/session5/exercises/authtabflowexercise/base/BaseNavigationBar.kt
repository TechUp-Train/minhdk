package com.apero.composetraining.session5.exercises.authtabflowexercise.base

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp


interface NavbarItemTemplate {
    val position: Int
    val icon: ImageVector
    val label: String
}

@Composable
private fun RowScope.BottomBarItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    selectedIconColor: Color = Color.Black,
    selectedTextColor: Color = Color.Black,
    unSelectedIconColor: Color = selectedIconColor.copy(alpha = 0.7f),
    unselectedTextColor: Color = selectedTextColor.copy(alpha = 0.7f),
    itemContainerColors: List<Color> = listOf(Color.Transparent, Color.Transparent),
    onClick: () -> Unit
) {
    NavigationBarItem(
        selected = isSelected,
        onClick = onClick,
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(25.dp)
            )
        },
        label = { Text(label) },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = selectedIconColor,
            selectedTextColor = selectedTextColor,
            unselectedIconColor = unSelectedIconColor,
            unselectedTextColor = unselectedTextColor,
            indicatorColor = Color.Transparent
        ),
        modifier = Modifier
            .padding(horizontal = 10.dp, 5.dp)
            .background(
                color = if (isSelected) itemContainerColors.first() else itemContainerColors.last(),
                shape = RoundedCornerShape(15.dp)
            )
    )
}

@Composable
fun <T: NavbarItemTemplate> BaseBottomBar(
    modifier: Modifier = Modifier,
    items: List<T>,
    isSelected: (Int) -> Boolean,
    onItemSelected: (Int) -> Unit
) {
    NavigationBar(modifier = modifier) {
        items.forEach { item ->
            BottomBarItem(
                icon = item.icon,
                label = item.label,
                isSelected = isSelected(item.position)
            ) {
                onItemSelected(item.position)
            }
        }

    }
}