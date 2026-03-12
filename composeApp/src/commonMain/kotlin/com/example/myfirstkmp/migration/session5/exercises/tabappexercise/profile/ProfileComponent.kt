package com.example.myfirstkmp.migration.session5.exercises.tabappexercise.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileHeader() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            contentAlignment = Alignment.BottomEnd
        ) {

            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE6B58F))
            )

            FloatingActionButton(
                onClick = {},
                modifier = Modifier.size(36.dp),
                containerColor = Color(0xFF1E88E5)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Alex Thompson",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "alex.thompson@design.com",
            color = Color.Gray,
            fontSize = 14.sp
        )
    }
}

@Composable
fun ProfileStats() {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 48.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        ProfileStat("12", "Orders")
        ProfileStat("2.4k", "Points")
        ProfileStat("Silver", "Rank")
    }
}

@Composable
fun ProfileStat(
    value: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = value,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color(0xFF1565C0)
        )

        Text(
            text = label.uppercase(),
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun AccountSettingsSection(
    onEditProfile: () -> Unit,
    onNotifications: () -> Unit,
    onPrivacy: () -> Unit,
    onLogout: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {

        Text(
            text = "ACCOUNT SETTINGS",
            fontSize = 12.sp,
            letterSpacing = 1.sp,
            color = Color.Gray,
            modifier = Modifier.padding(vertical = 12.dp)
        )

        SettingItem(
            icon = Icons.Default.Person,
            title = "Edit Profile",
            subtitle = "Update your personal info",
            onClick = onEditProfile
        )

        SettingItem(
            icon = Icons.Default.Notifications,
            title = "Notifications",
            subtitle = "Configure alerts and sounds",
            onClick = onNotifications
        )

        SettingItem(
            icon = Icons.Default.Lock,
            title = "Privacy & Security",
            subtitle = "Manage your data and visibility",
            onClick = onPrivacy
        )

        SettingItem(
            icon = Icons.Default.Logout,
            title = "Logout",
            subtitle = "Sign out of your account",
            onClick = onLogout,
            isLogout = true
        )
    }
}

@Composable
fun SettingItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    isLogout: Boolean = false
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(
                    if (isLogout) Color(0xFFFFEBEE)
                    else Color(0xFFE3F2FD)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isLogout) Color.Red else Color(0xFF1976D2)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = title,
                fontWeight = FontWeight.Medium,
                color = if (isLogout) Color.Red else Color.Black
            )

            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.Gray
        )
    }
}
