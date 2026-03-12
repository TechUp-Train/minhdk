package com.example.myfirstkmp.migration.session5.exercises.authtabflowexercise.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import com.example.myfirstkmp.migration.session5.exercises.authtabflowexercise.base.NavbarItemTemplate

val navbarItems = listOf(
    object : NavbarItemTemplate {
        override val position = 0
        override val icon = Icons.Default.Home
        override val label = "Home"
    },
    object : NavbarItemTemplate {
        override val position = 1
        override val icon = Icons.Default.Explore
        override val label = "Discover"
    },
    object : NavbarItemTemplate {
        override val position = 2
        override val icon = Icons.Default.AccountCircle
        override val label = "Profile"
    }
)