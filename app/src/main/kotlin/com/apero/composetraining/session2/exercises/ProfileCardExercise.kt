package com.apero.composetraining.session2.exercises

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apero.composetraining.common.AppTheme

/**
 * ⭐ BÀI TẬP 1: Profile Card (Easy — 30 phút)
 *
 * Yêu cầu:
 * - Avatar: Box + CircleShape, hiển thị chữ cái đầu tên (KHÔNG dùng Image thật)
 * - Tên (16sp bold) + job title (14sp gray) bên dưới avatar
 * - Stats row: 3 cột (Posts | Followers | Following) với số bold + label nhỏ
 *   → Dùng IntrinsicSize.Min để 3 cột bằng chiều cao nhau
 *   → VerticalDivider giữa các cột
 * - Follow button full-width ở dưới cùng
 * - Spacer(Modifier.weight(1f)) để đẩy Follow button xuống
 *
 * Tiêu chí:
 * - Modifier.height(IntrinsicSize.Min) trên stats Row
 * - VerticalDivider() giữa các stat column
 * - Follow button luôn ở bottom dù card cao thấp khác nhau
 *
 * Gợi ý:
 * - Avatar initials: profile.name.first().toString()
 * - Stat column: Column(horizontalAlignment = CenterHorizontally) { Text(count); Text(label) }
 */

data class UserProfile(
    val name: String,
    val jobTitle: String,
    val postsCount: Int,
    val followersCount: Int,
    val followingCount: Int
)

// TODO: [Session 2] Bài tập 1 - Implement ProfileCard composable
// Params: profile: UserProfile, onFollowClick: () -> Unit, modifier: Modifier = Modifier
// Layout gợi ý:
//   Card {
//     Column(horizontalAlignment = CenterHorizontally) {
//       Box(CircleShape, background = primary) { Text(initials) }  ← Avatar
//       Text(name, bold 16sp)
//       Text(jobTitle, gray 14sp)
//       Spacer(weight 1f)
//       Row(Modifier.height(IntrinsicSize.Min)) {               ← Stats row
//         StatColumn("Posts", postsCount)
//         VerticalDivider()
//         StatColumn("Followers", followersCount)
//         VerticalDivider()
//         StatColumn("Following", followingCount)
//       }
//       Spacer(weight 1f)
//       Button(onFollowClick) { Text("Follow") }                ← Follow button
//     }
//   }

// TODO: [Session 2] Bài tập 1 - Implement StatColumn composable (private, stateless)
// Params: label: String, count: Int
// Layout: Column(CenterHorizontally) { Text(count bold 18sp); Text(label gray 12sp) }

@Composable
fun ProfileCardScreen() {
    val profile = UserProfile(
        name = "Nguyễn Quang Minh",
        jobTitle = "Android Developer tại Apero",
        postsCount = 128,
        followersCount = 1200,
        followingCount = 890
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        // TODO: Thay placeholder bằng ProfileCard(profile, onFollowClick = {})
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Placeholder avatar
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = profile.name.first().toString(),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.height(12.dp))
                Text(profile.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(profile.jobTitle, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(16.dp))
                Text("TODO: Implement stats row với IntrinsicSize.Min + VerticalDivider",
                    color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                Spacer(Modifier.height(16.dp))
                Button(onClick = {}, modifier = Modifier.fillMaxWidth()) { Text("Follow") }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileCardScreenPreview() {
    AppTheme { ProfileCardScreen() }
}
