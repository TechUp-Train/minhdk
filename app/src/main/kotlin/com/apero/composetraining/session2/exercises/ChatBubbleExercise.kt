package com.apero.composetraining.session2.exercises

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apero.composetraining.common.AppTheme

/**
 * ⭐⭐⭐⭐ BÀI TẬP NÂNG CAO BUỔI 2: Chat Bubble Layout
 *
 * Mô tả: Build chat UI với bubble alignment khác nhau cho sent/received messages
 *
 * Received (trái):               Sent (phải):
 * [Avatar] ┌─────────────┐       ┌─────────────┐
 *          │ Tin nhắn    │       │ Tin nhắn    │ [Avatar]
 *          │ nhận được   │       │ đã gửi      │
 *          └─────────────┘       └─────────────┘ ✓✓ 10:30
 *          10:29
 *
 * Yêu cầu:
 * 1. Row với Alignment khác nhau:
 *    - Received: Avatar trái + Bubble + space bên phải
 *    - Sent: space bên trái + Bubble + Avatar phải
 * 2. Bubble shape: RoundedCornerShape bất đối xứng
 *    (sent: bo tất cả trừ góc dưới phải)
 * 3. Timestamp + Read receipt (checkmark) — align cuối row
 * 4. Dùng Modifier.align() per-child trong Box
 * 5. Arrangement.End / Arrangement.Start cho Row sent/received
 *
 * Khái niệm Buổi 2:
 * - Row horizontalArrangement (SpaceBetween → Arrangement.End/Start)
 * - Modifier.align() — per-child alignment trong parent
 * - Asymmetric RoundedCornerShape
 * - weight(1f) + wrapContentWidth()
 */

// ─── Data Model ──────────────────────────────────────────────────────────────

data class ChatMessage(
    val id: String,
    val senderName: String,
    val content: String,
    val time: String,
    val isSent: Boolean,       // true = mình gửi, false = người khác gửi
    val isRead: Boolean = false
)

// ─── Main Composable ─────────────────────────────────────────────────────────

/**
 * Dispatcher — chọn sent vs received bubble
 */
@Composable
fun ChatBubble(
    message: ChatMessage,
    modifier: Modifier = Modifier
) {
    if (message.isSent) {
        SentBubble(message = message, modifier = modifier)
    } else {
        ReceivedBubble(message = message, modifier = modifier)
    }
}

/**
 * Received bubble — Avatar trái, bubble phải có max width 75%
 *
 * TODO: [Buổi 2] Implement layout:
 * Row {
 *     SenderAvatar(...)                          // Avatar bên trái
 *     Spacer(4dp)
 *     Column {
 *         BubbleContent(isSent = false, ...)     // Bubble
 *         Timestamp(...)                          // Thời gian
 *     }
 *     Spacer(weight(1f))                         // Đẩy bubble sang trái, max 75% width
 * }
 */
@Composable
fun ReceivedBubble(
    message: ChatMessage,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        // Avatar người gửi
        SenderAvatar(
            name = message.senderName,
            modifier = Modifier.size(32.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Bubble + Timestamp column
        Column(modifier = Modifier.weight(0.75f)) {
            // Sender name (chỉ show trong group chat)
            Text(
                text = message.senderName,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = 4.dp, bottom = 2.dp)
            )

            // Bubble — received shape: tất cả bo trừ góc dưới trái
            BubbleContent(
                content = message.content,
                isSent = false
            )

            // Timestamp
            Text(
                text = message.time,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 4.dp, top = 2.dp)
            )
        }

        // Spacer để bubble không chiếm full width (để 25% bên phải)
        Spacer(modifier = Modifier.weight(0.25f))
    }
}

/**
 * Sent bubble — space trái, bubble phải, Avatar phải
 *
 * TODO: [Buổi 2] Implement layout:
 * Row(horizontalArrangement = Arrangement.End) {
 *     Spacer(weight(1f))                         // Push về bên phải
 *     Column(horizontalAlignment = Alignment.End) {
 *         BubbleContent(isSent = true, ...)
 *         Row { Timestamp + ReadReceipt }        // Align cuối
 *     }
 *     Spacer(4dp)
 *     SenderAvatar(...)                          // Avatar bên phải
 * }
 */
@Composable
fun SentBubble(
    message: ChatMessage,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.End   // TODO: Tại sao Arrangement.End?
    ) {
        // Space bên trái để bubble không full width
        Spacer(modifier = Modifier.weight(0.25f))

        // Bubble + Timestamp
        Column(
            modifier = Modifier.weight(0.75f),
            horizontalAlignment = Alignment.End    // Align về phải
        ) {
            // Bubble — sent shape: tất cả bo trừ góc dưới phải
            BubbleContent(
                content = message.content,
                isSent = true
            )

            // Timestamp + Read receipt
            Row(
                modifier = Modifier.padding(end = 4.dp, top = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = message.time,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                // Read receipt checkmarks
                ReadReceiptIcon(isRead = message.isRead)
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Avatar bên phải (mình)
        SenderAvatar(
            name = "Me",
            modifier = Modifier.size(32.dp),
            backgroundColor = MaterialTheme.colorScheme.primary
        )
    }
}

/**
 * Bubble content với asymmetric RoundedCornerShape
 *
 * TODO: [Buổi 2] Thay đổi shape theo isSent:
 * - Received: RoundedCornerShape(topStart=2dp, topEnd=12dp, bottomStart=12dp, bottomEnd=12dp)
 * - Sent:     RoundedCornerShape(topStart=12dp, topEnd=12dp, bottomStart=12dp, bottomEnd=2dp)
 *
 * Đây là "tail" của bubble — góc nhỏ chỉ về phía người gửi
 */
@Composable
fun BubbleContent(
    content: String,
    isSent: Boolean,
    modifier: Modifier = Modifier
) {
    val bubbleShape = if (isSent) {
        // Sent: góc dưới phải nhỏ (tail)
        RoundedCornerShape(
            topStart = 12.dp,
            topEnd = 12.dp,
            bottomStart = 12.dp,
            bottomEnd = 2.dp   // ← tail
        )
    } else {
        // Received: góc dưới trái nhỏ (tail)
        RoundedCornerShape(
            topStart = 2.dp,   // ← tail
            topEnd = 12.dp,
            bottomStart = 12.dp,
            bottomEnd = 12.dp
        )
    }

    val bubbleColor = if (isSent) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    val textColor = if (isSent) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Box(
        modifier = modifier
            .clip(bubbleShape)
            .background(bubbleColor)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium,
            color = textColor,
            lineHeight = 20.sp
        )
    }
}

@Composable
fun SenderAvatar(
    name: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.secondary
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name.first().uppercaseChar().toString(),
            color = Color.White,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ReadReceiptIcon(
    isRead: Boolean,
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = if (isRead) Icons.Default.DoneAll else Icons.Default.Done,
        contentDescription = if (isRead) "Đã xem" else "Đã gửi",
        modifier = modifier.size(14.dp),
        tint = if (isRead) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        }
    )
}

// ─── Sample Data ──────────────────────────────────────────────────────────────

private val sampleMessages = listOf(
    ChatMessage(
        id = "1",
        senderName = "Khắc Minh",
        content = "Bro ơi, cái IntrinsicSize.Max dùng thế nào vậy? Tự nhiên 2 card của tao height khác nhau 😅",
        time = "10:28",
        isSent = false
    ),
    ChatMessage(
        id = "2",
        senderName = "Me",
        content = "Dùng Row(modifier = Modifier.height(IntrinsicSize.Max)) rồi mỗi card thêm .fillMaxHeight() là xong bro",
        time = "10:29",
        isSent = true,
        isRead = true
    ),
    ChatMessage(
        id = "3",
        senderName = "Khắc Minh",
        content = "EZ luôn! Cảm ơn bro. Mà sao phải cần fillMaxHeight() nữa nhỉ?",
        time = "10:30",
        isSent = false
    ),
    ChatMessage(
        id = "4",
        senderName = "Me",
        content = "IntrinsicSize.Max set constraint cho Row. fillMaxHeight() bảo card 'hãy chiếm hết height của Row đó'. Thiếu 1 trong 2 thì không work đâu 😄",
        time = "10:30",
        isSent = true,
        isRead = false
    )
)

// ─── Preview ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true, name = "Chat — Light Mode")
@Composable
private fun ChatBubbleLightPreview() {
    AppTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            sampleMessages.forEach { message ->
                ChatBubble(message = message)
            }
        }
    }
}

@Preview(
    showBackground = true,
    name = "Chat — Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun ChatBubbleDarkPreview() {
    AppTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            sampleMessages.forEach { message ->
                ChatBubble(message = message)
            }
        }
    }
}

// ─── Câu Hỏi Thảo Luận ───────────────────────────────────────────────────────
/*
 * Q1: Tại sao dùng weight(0.75f) thay vì fillMaxWidth(0.75f)?
 *     → fillMaxWidth(0.75f) lấy 75% của parent width tuyệt đối.
 *       weight() chia sẻ không gian còn lại sau siblings.
 *       Trong Row có Avatar (32dp), weight(0.75f) = 75% của (width - 32dp - gaps).
 *
 * Q2: Asymmetric RoundedCornerShape — tại sao "tail" chỉ 2dp?
 *     → Tạo visual cue về hướng của bubble (trỏ về avatar sender).
 *       Đây là UI pattern của iMessage, WhatsApp, Telegram.
 *
 * Q3: Arrangement.End trong SentBubble Row — có thể bỏ không?
 *     → Có thể bỏ vì Spacer(weight(0.25f)) đã push sang phải.
 *       Nhưng Arrangement.End rõ intent hơn — code đọc dễ hiểu hơn.
 *
 * Q4: Modifier.align() per-child vs horizontalAlignment của Column?
 *     → Column(horizontalAlignment = Alignment.End) = tất cả children align End.
 *     → Modifier.align(Alignment.Start) per-child = exception cho 1 child cụ thể.
 *     → Kết hợp: Column(horizontalAlignment = End) với 1 child có .align(Start)
 */
