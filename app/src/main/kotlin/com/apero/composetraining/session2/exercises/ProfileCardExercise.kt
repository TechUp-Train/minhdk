package com.apero.composetraining.session2.exercises

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apero.composetraining.common.AppTheme
import okhttp3.internal.userAgent


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
        name = "Doan Khac Minh",
        jobTitle = "Android Developer tại Apero",
        postsCount = 128,
        followersCount = 1200,
        followingCount = 890
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(12.dp)
    ) {

        ProfileImage(
            username = profile.name,
            modifier = Modifier
                .size(70.dp)
                .background(color = Color.LightGray, shape = CircleShape)
        )

        Spacer(
            modifier = Modifier.height(15.dp)
        )

        Text(
            text = profile.name,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color.Black
        )

        Spacer(
            modifier = Modifier.height(5.dp)
        )

        Text(
            text = profile.jobTitle,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color.Black
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        HorizontalDivider(
            thickness = 2.dp,
            color = Color.LightGray,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
        ) {

            ProfileStat(
                amount = profile.postsCount.toString(),
                title = "Posts",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )

            Box(
                modifier = Modifier
                    .width(2.dp)
                    .fillMaxHeight()
                    .background(Color.LightGray)
                    .padding(vertical = 10.dp)
            )

            ProfileStat(
                amount = profile.followersCount.toString(),
                title = "Followers",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )

            Box(
                modifier = Modifier
                    .width(2.dp)
                    .fillMaxHeight()
                    .padding(5.dp)
            )

            ProfileStat(
                amount = profile.followingCount.toString(),
                title = "Following",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )

        }

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        HorizontalDivider(
            thickness = 2.dp,
            color = Color.LightGray,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(30.dp)
        )

        Button(
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors().copy(containerColor = Color.Blue.copy(alpha = 0.3f)),
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Follow"
            )
        }

    }


}


    @Preview
@Composable
private fun ProfileImage(
    modifier: Modifier = Modifier,
    username: String = "Doan Khac Minh"
) {

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Text(
            text = username.first().toString(),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }

}

@Preview
@Composable
fun ProfileStat(
    modifier: Modifier = Modifier,
    amount: String = "123",
    title: String = "Posts"
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {

        Text(
            text = amount,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color.Black
        )

        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color.Black
        )

    }

}

@Preview(showBackground = false)
@Composable
private fun ProfileCardScreenPreview() {
    AppTheme { ProfileCardScreen() }
}
