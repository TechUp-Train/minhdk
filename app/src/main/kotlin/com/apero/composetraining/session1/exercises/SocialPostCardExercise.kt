package com.apero.composetraining.session1.exercises

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Repeat
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apero.composetraining.common.AppTheme

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

@Composable
fun SocialPostCard(
    username: String,
    timeAgo: String,
    content: String,
    likeCount: Int = 0,
    commentCount: Int = 0,
    retweetCount: Int = 0,
    modifier: Modifier = Modifier,
    // Nullable slot cho attachment — chỉ render nếu != null
    attachment: (@Composable () -> Unit)? = null
) {
    // TODO: Implement SocialPostCard layout
    // - Card với RoundedCornerShape(12.dp) và elevation
    // - Column bên trong với padding(12.dp)
    // - Gọi PostHeader(username, timeAgo)
    // - Spacer rồi Text content với maxLines=3, overflow=Ellipsis
    // - Kiểm tra attachment != null → Spacer + gọi attachment()
    // - Spacer + HorizontalDivider
    // - Gọi PostActionBar(likeCount, commentCount, retweetCount)
    Box {}
}

// ─── Sub-components (cần tự implement) ───────────────────────────────────────

@Composable
fun PostHeader(
    username: String,
    timeAgo: String,
    modifier: Modifier = Modifier
) {
    // TODO: Implement PostHeader
    // - Row với fillMaxWidth, verticalAlignment = CenterVertically
    // - Gọi UserAvatar(username)
    // - Spacer(width = 10.dp)
    // - Column (weight(1f)): Text username (Bold) + Text timeAgo (secondary color)
    Box {}
}

@Composable
fun UserAvatar(
    username: String,
    modifier: Modifier = Modifier
) {
    // TODO: Implement UserAvatar
    // - Tính màu từ username.hashCode() (dùng avatarColors list + abs() % size)
    // - Box size(40.dp) + clip(CircleShape) + background(color)
    // - Text hiển thị chữ cái đầu của username (uppercaseChar)
    // GỢI Ý: val color = avatarColors[Math.abs(username.hashCode()) % avatarColors.size]
    Box {}
}

@Composable
fun PostActionBar(
    likeCount: Int,
    commentCount: Int,
    retweetCount: Int,
    modifier: Modifier = Modifier
) {
    // TODO: Implement PostActionBar
    // - Row với fillMaxWidth, Arrangement.SpaceBetween
    // - 4 ActionItem: Like (FavoriteBorder), Comment (ChatBubbleOutline), Retweet (Repeat), Share (Share)
    // - Share không hiện count (truyền count = null)
    Box {}
}

@Composable
fun ActionItem(
    icon: ImageVector,
    count: Int?,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    // TODO: Implement ActionItem (reusable)
    // - Row với verticalAlignment = CenterVertically, spacedBy(4.dp)
    // - Icon size(18.dp) với tint = onSurfaceVariant
    // - Nếu count != null && count > 0: hiển thị Text count
    Box {}
}

// ─── Preview ─────────────────────────────────────────────────────────────────

// Preview 1: Không có attachment
@Preview(showBackground = true, name = "Post — No Attachment")
@Composable
private fun SocialPostNoAttachmentPreview() {
    AppTheme {
        SocialPostCard(
            username = "nqmgaming",
            timeAgo = "2 min ago",
            content = "Vừa migrate ANeko sang Jetpack Compose xong! " +
                    "Smart recomposition giúp animation smooth hơn hẳn so với View system. " +
                    "Ai đang dùng Compose production chưa?",
            likeCount = 42,
            commentCount = 12,
            retweetCount = 7
        )
    }
}

// Preview 2: Có Image attachment (dùng Box placeholder)
@Preview(showBackground = true, name = "Post — With Image Attachment")
@Composable
private fun SocialPostWithImagePreview() {
    AppTheme {
        SocialPostCard(
            username = "thang44hdai",
            timeAgo = "15 min ago",
            content = "DSA practice session hôm nay — solved Binary Search Tree in O(log n). " +
                    "Cái này sẽ apply được vào Room database query optimization không nhỉ?",
            likeCount = 15,
            commentCount = 3,
            retweetCount = 2,
            attachment = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "📸 Image Attachment",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        )
    }
}

// Preview 3: Có Code block attachment
@Preview(showBackground = true, name = "Post — With Code Block")
@Composable
private fun SocialPostWithCodePreview() {
    AppTheme {
        SocialPostCard(
            username = "KhacMinh2305",
            timeAgo = "1 hour ago",
            content = "Mới học được cái Slot API trong Compose — flexible hơn XML include rất nhiều!",
            likeCount = 8,
            commentCount = 5,
            retweetCount = 1,
            attachment = {
                // Code block attachment — mono font, nền tối
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp)),
                    color = Color(0xFF1E1E1E)
                ) {
                    Text(
                        text = """
@Composable
fun AppCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card { 
        content() // Slot!
    }
}""".trimIndent(),
                        modifier = Modifier.padding(12.dp),
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                        fontSize = 12.sp,
                        color = Color(0xFFD4D4D4)
                    )
                }
            }
        )
    }
}

// Preview 4: Dark Mode
@Preview(
    showBackground = true,
    name = "Post — Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun SocialPostDarkPreview() {
    AppTheme {
        SocialPostNoAttachmentPreview()
    }
}
