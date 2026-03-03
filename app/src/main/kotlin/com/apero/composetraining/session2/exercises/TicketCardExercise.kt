package com.apero.composetraining.session2.exercises

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.QrCode2
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apero.composetraining.common.AppTheme

/**
 * ⭐⭐⭐ BÀI TẬP NÂNG CAO BUỔI 2: Event Ticket Card
 *
 * Mô tả: Build ticket card có divider đặc biệt — 2 nửa hình tròn "cắt" vào 2 bên
 *
 * ┌─────────────────────────────────────────┐
 * │  🎵  TECH SUMMIT 2026                   │
 * │      Android & Compose Workshop         │
 * │                                         │
 * │  📍 Hà Nội Convention Center           │
 * │  Sat, Mar 7 · 09:00 AM                 │
 * ├──── ◐ ─────────────────── ◑ ────────────┤  ← Notched divider
 * │  ADMIT ONE       │  QR CODE             │
 * │  Row A - Seat 12 │  [     ]             │
 * │  VIP Section     │  [  📱 ]             │
 * │                  │  [     ]             │
 * └─────────────────────────────────────────┘
 *
 * Yêu cầu kỹ thuật:
 * 1. Dashed divider bằng Canvas + PathEffect.dashPathEffect
 * 2. Notched circles ở 2 bên bằng Box + CircleShape (Modifier.offset)
 * 3. Bottom section: Row với Divider dọc (VerticalDivider) giữa 2 cột
 * 4. Modifier chain: clip, background, shadow — ORDER MATTERS
 * 5. Tất cả text dùng MaterialTheme.typography
 *
 * Khái niệm Buổi 2:
 * - Box: notch circles xếp chồng lên divider
 * - Row/Column: layout 2 section
 * - Modifier order: clip → background (không phải ngược lại)
 * - Alignment: Alignment.CenterStart, Alignment.CenterEnd cho notches
 */

// ─── Main Component ──────────────────────────────────────────────────────────

@Composable
fun EventTicketCard(
    eventName: String,
    eventSubtitle: String,
    location: String,
    dateTime: String,
    seatInfo: String,
    section: String,
    modifier: Modifier = Modifier
) {
    // Card background với clip trước, elevation sau
    // TODO: [Buổi 2] Tại sao clip phải TRƯỚC khi set background Card?
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column {
            // ── TOP SECTION: Event info ──────────────────────────────────
            TicketTopSection(
                eventName = eventName,
                eventSubtitle = eventSubtitle,
                location = location,
                dateTime = dateTime
            )

            // ── NOTCHED DIVIDER ──────────────────────────────────────────
            // TODO: [Nâng cao] Implement notched divider
            // Cách đơn giản: Box + 2 circles ở 2 đầu
            NotchedDivider()

            // ── BOTTOM SECTION: Seat + QR ────────────────────────────────
            TicketBottomSection(
                seatInfo = seatInfo,
                section = section
            )
        }
    }
}

@Composable
fun TicketTopSection(
    eventName: String,
    eventSubtitle: String,
    location: String,
    dateTime: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(20.dp)
    ) {
        // Event emoji + name
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("🎵", fontSize = 28.sp)
            Column {
                Text(
                    text = eventName,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    text = eventSubtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Location + DateTime
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Location
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = "Địa điểm",
                    tint = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = location,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                )
            }

            // DateTime badge
            Surface(
                shape = RoundedCornerShape(50),
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f)
            ) {
                Text(
                    text = dateTime,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

/**
 * Notched Divider — điểm nâng cao nhất của bài này
 *
 * Kỹ thuật: Box với 2 CircleShape "cắt" vào 2 bên + Canvas dashed line ở giữa
 *
 * TODO: [Nâng cao] Implement đúng như hình:
 * Box {
 *     // Dashed divider ở giữa
 *     DashedDivider(modifier = Modifier.align(Alignment.Center))
 *
 *     // Notch trái — "cắt" vào edge trái
 *     Box(modifier = Modifier
 *         .size(24.dp)
 *         .offset(x = (-12).dp)  // Half outside left edge
 *         .clip(CircleShape)
 *         .background(MaterialTheme.colorScheme.background)
 *         .align(Alignment.CenterStart)
 *     )
 *
 *     // Notch phải — "cắt" vào edge phải
 *     Box(modifier = Modifier
 *         .size(24.dp)
 *         .offset(x = 12.dp)
 *         .clip(CircleShape)
 *         .background(MaterialTheme.colorScheme.background)
 *         .align(Alignment.CenterEnd)
 *     )
 * }
 *
 * Trick: background của notch circles = background của màn hình
 * → tạo ảo giác "cắt vào" card
 */
@Composable
fun NotchedDivider(
    notchSize: Dp = 24.dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(notchSize)
    ) {
        // Dashed line ở giữa
        DashedDivider(
            modifier = Modifier.align(Alignment.Center)
        )

        // Notch trái
        Box(
            modifier = Modifier
                .size(notchSize)
                .offset(x = -(notchSize / 2))
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.background)
                .align(Alignment.CenterStart)
        )

        // Notch phải
        Box(
            modifier = Modifier
                .size(notchSize)
                .offset(x = notchSize / 2)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.background)
                .align(Alignment.CenterEnd)
        )
    }
}

/**
 * Dashed divider dùng Canvas + PathEffect
 *
 * TODO: [Nâng cao] Tại sao cần Canvas thay vì HorizontalDivider thông thường?
 * → HorizontalDivider chỉ vẽ solid line.
 * → Canvas cho phép custom PathEffect.dashPathEffect với interval tùy chọn.
 */
@Composable
fun DashedDivider(
    color: Color = MaterialTheme.colorScheme.outlineVariant,
    dashWidth: Float = 12f,
    gapWidth: Float = 8f,
    strokeWidth: Float = 2f,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(strokeWidth.dp)
    ) {
        drawDashedLine(
            color = color,
            dashWidth = dashWidth,
            gapWidth = gapWidth,
            strokeWidth = strokeWidth
        )
    }
}

private fun DrawScope.drawDashedLine(
    color: Color,
    dashWidth: Float,
    gapWidth: Float,
    strokeWidth: Float
) {
    val pathEffect = PathEffect.dashPathEffect(
        intervals = floatArrayOf(dashWidth, gapWidth),
        phase = 0f
    )
    drawLine(
        color = color,
        start = Offset(0f, size.height / 2),
        end = Offset(size.width, size.height / 2),
        strokeWidth = strokeWidth,
        cap = StrokeCap.Round,
        pathEffect = pathEffect
    )
}

@Composable
fun TicketBottomSection(
    seatInfo: String,
    section: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Seat info — Column bên trái
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "ADMIT ONE",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = seatInfo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = section,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Divider dọc giữa 2 column
        // TODO: [Buổi 2] Tại sao cần VerticalDivider thay vì width(1.dp).background()?
        // VerticalDivider tự động fill height của parent Row
        VerticalDivider(
            modifier = Modifier
                .height(80.dp)
                .padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )

        // QR Code placeholder bên phải
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.QrCode2,
                    contentDescription = "QR Code",
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Scan to enter",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// ─── Preview ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true, name = "Ticket — Light")
@Composable
private fun TicketCardLightPreview() {
    AppTheme {
        EventTicketCard(
            eventName = "TECH SUMMIT 2026",
            eventSubtitle = "Android & Compose Workshop",
            location = "Hà Nội Convention Center",
            dateTime = "Sat, Mar 7 · 09:00 AM",
            seatInfo = "Row A - Seat 12",
            section = "VIP Section"
        )
    }
}

@Preview(
    showBackground = true,
    name = "Ticket — Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun TicketCardDarkPreview() {
    AppTheme {
        EventTicketCard(
            eventName = "TECH SUMMIT 2026",
            eventSubtitle = "Android & Compose Workshop",
            location = "Hà Nội Convention Center",
            dateTime = "Sat, Mar 7 · 09:00 AM",
            seatInfo = "Row A - Seat 12",
            section = "VIP Section"
        )
    }
}

// ─── Câu Hỏi Thảo Luận ───────────────────────────────────────────────────────
/*
 * Q1: Notch circles dùng background = màn hình để tạo ảo giác "cắt vào"
 *     → Điều gì xảy ra nếu đặt ticket card lên nền màu khác?
 *     → Cách fix proper: dùng custom Shape với path carving (nâng cao)
 *
 * Q2: Tại sao dùng Canvas cho dashed line thay vì một Row lặp nhiều Box nhỏ?
 *     → Performance: Canvas = single draw call. Row+Box = N composables.
 *
 * Q3: VerticalDivider vs Modifier.width(1.dp).background()
 *     → VerticalDivider semantic rõ hơn, height tự fill parent
 *     → Modifier.background thì phải set height thủ công
 *
 * Q4: Modifier.offset() trong notch — tại sao không dùng padding âm?
 *     → padding âm không exist trong Compose. offset() để dịch chuyển visual
 *       mà không ảnh hưởng layout của siblings.
 */
