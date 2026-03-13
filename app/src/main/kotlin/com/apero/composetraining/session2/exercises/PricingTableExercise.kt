package com.apero.composetraining.session2.exercises

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apero.composetraining.common.AppTheme

/**
 * ⭐⭐ BÀI TẬP 2: Pricing Table (Medium — 45 phút)
 *
 * Yêu cầu:
 * - Row chứa 3 PricingCard: Basic / Pro / Premium
 * - Mỗi card có: tên gói + giá + danh sách features (số feature KHÁC NHAU mỗi card)
 * - Dùng IntrinsicSize.Max để cả 3 card BẰNG CHIỀU CAO NHAU dù content dài ngắn khác nhau
 * - Card "Pro" được highlight (border primary, "Popular" badge)
 * - Spacer(Modifier.weight(1f)) đẩy button xuống bottom mỗi card
 * - Button "Chọn gói" ở đáy mỗi card — luôn ngang hàng nhau
 *
 * Tiêu chí:
 * - Row(Modifier.height(IntrinsicSize.Max)) bọc 3 card
 * - Mỗi PricingCard dùng Modifier.fillMaxHeight() để chiếm hết chiều cao Row
 * - Button ở đáy nhờ Spacer(Modifier.weight(1f)) bên trong Column
 * - Không hardcode height
 *
 * Tại sao quan trọng:
 * - Không có IntrinsicSize.Max: 3 card cao thấp theo content → mất thẩm mỹ
 * - Có IntrinsicSize.Max: Compose đo chiều cao lớn nhất (Premium), apply cho tất cả
 *
 * Gợi ý:
 * Row(
 *     modifier = Modifier.height(IntrinsicSize.Max),  // ← KEY
 *     horizontalArrangement = Arrangement.spacedBy(8.dp)
 * ) {
 *     PricingCard(..., modifier = Modifier.weight(1f).fillMaxHeight())
 *     PricingCard(..., modifier = Modifier.weight(1f).fillMaxHeight(), isHighlighted = true)
 *     PricingCard(..., modifier = Modifier.weight(1f).fillMaxHeight())
 * }
 */

data class PricingPlan(
    val name: String,
    val price: String,
    val period: String,
    val features: List<String>,
    val isPopular: Boolean = false
)

private val plans = listOf(
    PricingPlan(
        name = "Basic",
        price = "Free",
        period = "mãi mãi",
        features = listOf(
            "5 AI generations/ngày",
            "Độ phân giải 512px",
            "Watermark"
        )
    ),
    PricingPlan(
        name = "Pro",
        price = "$9.99",
        period = "/ tháng",
        features = listOf(
            "100 AI generations/ngày",
            "Độ phân giải 1024px",
            "Không watermark",
            "Priority queue",
            "Email support"
        ),
        isPopular = true
    ),
    PricingPlan(
        name = "Premium",
        price = "$19.99",
        period = "/ tháng",
        features = listOf(
            "Unlimited generations",
            "Độ phân giải 4K",
            "Không watermark",
            "Priority queue",
            "24/7 Support",
            "Custom styles",
            "API access"
        )
    )
)

// TODO: [Session 2] Bài tập 2 - Implement PricingCard composable (stateless)
// Params: plan: PricingPlan, modifier: Modifier = Modifier, isHighlighted: Boolean = plan.isPopular
// Layout bên trong PricingCard (Column + fillMaxHeight):
//   Box { Card { Column {
//     if (isHighlighted) Badge "Popular" ở góc trên
//     Text(plan.name, bold 16sp)
//     Text(plan.price, bold 28sp, primary color)
//     Text(plan.period, gray 12sp)
//     Divider
//     plan.features.forEach { feature →
//       Row { Icon(Check, tint = primary); Text(feature) }
//     }
//     Spacer(Modifier.weight(1f))   ← đẩy button xuống
//     Button("Chọn gói", Modifier.fillMaxWidth())
//   }}}

// TODO: [Session 2] Bài tập 2 - Implement PricingTableScreen
// Dùng Row(Modifier.height(IntrinsicSize.Max)) bọc 3 PricingCard
// Mỗi card dùng Modifier.weight(1f).fillMaxHeight()

@Composable
fun PricingTableScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Chọn Gói Phù Hợp", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("Tất cả gói đều có 7 ngày dùng thử miễn phí",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant)

        // TODO: Xóa placeholder và implement Row với IntrinsicSize.Max
        // Placeholder hiển thị 3 card KHÔNG bằng chiều cao (để thấy vấn đề cần fix)
        Text("⚠️ Placeholder: 3 cards chưa bằng chiều cao",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.labelMedium)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            plans.forEach { plan ->
                Card(
                    modifier = Modifier.weight(1f),
                    border = if (plan.isPopular)
                        androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                    else null
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(plan.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(plan.price, fontWeight = FontWeight.Bold, fontSize = 22.sp,
                            color = MaterialTheme.colorScheme.primary)
                        Text(plan.period, fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        plan.features.forEach { feature ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Check, contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                    tint = MaterialTheme.colorScheme.primary)
                                Spacer(Modifier.width(4.dp))
                                Text(feature, fontSize = 11.sp)
                            }
                        }
                        // TODO: Thêm Spacer(Modifier.weight(1f)) + Button ở đây
                        // và bọc toàn bộ bằng Row(Modifier.height(IntrinsicSize.Max))
                    }
                }
            }
        }

        Text("💡 TODO: Dùng Row(Modifier.height(IntrinsicSize.Max)) để 3 card bằng chiều cao nhau",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelSmall)
    }
}

@Preview(showBackground = true)
@Composable
private fun PricingTableScreenPreview() {
    AppTheme { PricingTableScreen() }
}
