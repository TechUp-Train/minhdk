package com.example.myfirstkmp.migration.session1.exercises

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs

/**
 * ⭐⭐⭐ BÀI TẬP NÂNG CAO: Social Post Card
 *
 * Yêu cầu:
 * 1. Header: Avatar (CircleShape, màu từ username) + Username + Timestamp
 * 2. Content: Text với maxLines = 3, TextOverflow.Ellipsis
 * 3. Attachment Slot (NULLABLE) — chỉ render nếu != null
 * 4. Action bar: Like + Comment + Retweet + Share (Arrangement.SpaceBetween)
 * 5. Modifier param bắt buộc trên tất cả @Composable
 * 6. 3 @Preview: không attachment / có image / có code block
 *
 * Khái niệm áp dụng từ Buổi 1:
 * - Slot API với NULLABLE content: (@Composable () -> Unit)? = null
 * - Modifier chain & order
 * - Reuse component (ActionItem)
 * - Multiple @Preview
 */

// ─── Main Component ──────────────────────────────────────────────────────────

// defind colors:
private val avatarColors = listOf(
    Color.Red,
    Color.Green,
    Color.Blue,
    Color.Yellow,
    Color.Magenta,
    Color.Cyan,
    Color.Gray,
    Color.LightGray,
    Color.Transparent
)

private val postContent = "\uD83C\uDFD7 MVVM\n" +
        "\n" +
        "\uD83D\uDD04 Unidirectional Data Flow\n" +
        "\n" +
        "\uD83D\uDCE6 Modularization\n" +
        "\n" +
        "\uD83E\uDDEA Unit Test\n" +
        "\n" +
        "Kiến trúc tốt giúp team scale dễ dàng và bảo trì lâu dài."

@Composable
fun SocialPostCard(
    modifier: Modifier = Modifier,
    username: String,
    timeAgo: String,
    content: String,
    likeCount: Int = 0,
    commentCount: Int = 0,
    retweetCount: Int = 0,
    attachment: (@Composable () -> Unit)? = null
) {

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(12.dp)
        ) {
            PostHeader(username, timeAgo)

            Spacer(modifier = Modifier.fillMaxWidth().height(12.dp))

            PostContent(
                content = content,
                limitLines = 3
            )

            Spacer(modifier = Modifier.fillMaxWidth().height(12.dp))

            attachment?.invoke()

            Spacer(modifier = Modifier.fillMaxWidth().height(12.dp))

            PostActionBar(
                likeCount = likeCount,
                commentCount = commentCount,
                retweetCount = retweetCount,
                onClickLike = {},
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
            )
        }

    }
}

// ─── Sub-components (cần tự implement) ───────────────────────────────────────

@Composable
fun PostHeader(
    username: String,
    timeAgo: String,
    modifier: Modifier = Modifier
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        UserAvatar(
            username, Modifier
                .size(40.dp)
                .clip(CircleShape)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp)
        ) {
            Text(
                text = username,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = timeAgo,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun UserAvatar(
    username: String,
    modifier: Modifier = Modifier
) {
    val imageColor = avatarColors[abs(username.hashCode()) % avatarColors.size]
    Image(
        painter = ColorPainter(imageColor),
        contentDescription = null,
        modifier = modifier
    )
}

@Composable
fun PostActionBar(
    likeCount: Int,
    commentCount: Int,
    retweetCount: Int,
    onClickLike: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        ActionItem(
            icon = Icons.Default.FavoriteBorder,
            count = likeCount,
            onClick = onClickLike
        )

        ActionItem(
            icon = Icons.Default.Edit,
            count = commentCount
        ) {}

        ActionItem(
            icon = Icons.Default.Refresh,
            count = retweetCount
        ) {}

        ActionItem(
            icon = Icons.Default.Share,
            count = null
        ) {}
    }
}

@Composable
fun ActionItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    count: Int?,
    onClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .clickable {
                onClick()
            }
            .padding(5.dp)
    ) {
        Image(
            imageVector = icon,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(25.dp)
        )

        count?.let {
            Text(
                text = it.toString(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier
                    .padding(horizontal = 6.dp)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

@Composable
fun PostContent(
    content: String,
    limitLines: Int? = null
) {
    Text(
        text = content,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        maxLines = limitLines ?: Int.MAX_VALUE,
        color = Color.Black
    )
}

//--------------------------------------Previews--------------------------------------
@Preview(showBackground = true)
@Composable
private fun PostHeaderPreview() {
    PostHeader("Min", "23:01 23/01/2003")
}

@Preview
@Composable
private fun PostContentPreview() {
    PostContent(postContent)
}

@Preview
@Composable
fun PostActionBarItemPreview() {
    ActionItem(
        icon = Icons.Default.FavoriteBorder,
        count = 100
    ) {

    }
}

@Preview
@Composable
fun PostActionBarPreview() {
    PostActionBar(
        likeCount = 100,
        commentCount = 200,
        retweetCount = 300,
        onClickLike = {}
    )
}

@Preview
@Composable
fun SocialCardPreview() {
    SocialPostCard(
        username = "Doan Khac Minh",
        timeAgo = "23 days ago",
        content = postContent,
        likeCount = 2003,
        commentCount = 23,
        retweetCount = 1,
        attachment = null,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    )
}
