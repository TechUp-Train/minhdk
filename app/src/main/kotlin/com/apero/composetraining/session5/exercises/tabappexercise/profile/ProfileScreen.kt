package com.apero.composetraining.session5.exercises.tabappexercise.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onEditProfile: () -> Unit = {},
    onNotifications: () -> Unit = {},
    onPrivacy: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F6F7)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        item {
            Spacer(modifier = Modifier.height(24.dp))
            ProfileHeader()
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            ProfileStats()
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            AccountSettingsSection(
                onEditProfile = onEditProfile,
                onNotifications = onNotifications,
                onPrivacy = onPrivacy,
                onLogout = onLogout
            )
        }
    }
}