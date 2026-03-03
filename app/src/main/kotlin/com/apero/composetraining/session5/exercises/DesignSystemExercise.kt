package com.apero.composetraining.session5.exercises

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * ⭐⭐⭐⭐⭐ BÀI TẬP NÂNG CAO BUỔI 5: Custom Design System — Production-level pattern
 *
 * Mô tả: Xây dựng custom design system với @Immutable, CompositionLocal,
 *         và design tokens — pattern được dùng trong production apps (Apero aiheadshot, etc.)
 *
 * ┌────────────────────────────────────────────────┐
 * │ ████████████████████████████████████████████  │  ← Gradient header
 * │       Design System Demo         ✨ PREMIUM    │
 * │                                                │
 * │ ┌──────────────────────────────────────────┐  │
 * │ │ 🤖 AI RECOMMENDED          [AI badge]    │  │  ← brand color
 * │ │                                          │  │
 * │ │ 📈 TRENDING                [pill]        │  │  ← warning color
 * │ └──────────────────────────────────────────┘  │
 * │ ┌───────────────────────────────────────────┐ │
 * │ │ ✅ Success: Upload complete               │ │  ← success color
 * │ │ ❌ Error: Network failed                  │ │  ← error color
 * │ └───────────────────────────────────────────┘ │
 * └────────────────────────────────────────────────┘
 *
 * Key concepts:
 * - @Immutable: báo với Compose rằng class không thay đổi sau khi tạo
 *   → Compose SKIP recompose khi instance không đổi
 * - staticCompositionLocalOf: CompositionLocal cho values ít thay đổi (tokens)
 * - CompositionLocalProvider: inject design tokens vào composition tree
 * - object AppTheme { val colors: AppColorTokens }: access pattern quen thuộc
 */

// ─── Design Tokens ────────────────────────────────────────────────────────────

/**
 * @Immutable annotation — TẠI SAO CẦN?
 *
 * Vấn đề không có @Immutable:
 *   Compose không thể biết AppColorTokens có thay đổi hay không
 *   → Khi parent recompose, Compose cũng recompose tất cả children
 *     dù tokens không đổi → LÃNG PHÍ
 *
 * Với @Immutable:
 *   Compose biết: "Object này KHÔNG BAO GIỜ thay đổi sau khi tạo"
 *   → Compose CÓ THỂ SKIP recompose nếu object reference không đổi
 *   → Performance tốt hơn đáng kể trong design systems
 *
 * Điều kiện để @Immutable đúng:
 *   1. Tất cả properties phải là val (không phải var)
 *   2. Tất cả properties phải là immutable types (Color, Int, String...)
 *   3. KHÔNG được mutate object sau khi tạo
 *
 * @Immutable vs @Stable:
 *   @Stable: properties có thể thay đổi NHƯNG theo equals() đúng cách
 *   @Immutable: properties KHÔNG BAO GIỜ thay đổi (mạnh hơn @Stable)
 */
@androidx.compose.runtime.Immutable
data class AppColorTokens(
    // Brand colors — màu chính của app
    val brand: Color,          // Màu chủ đạo (AI Purple)
    val brandVariant: Color,   // Biến thể nhạt hơn của brand
    val brandOnBrand: Color,   // Text/icon trên nền brand

    // Semantic colors — màu có nghĩa
    val success: Color,        // Thành công (xanh lá)
    val successContainer: Color,
    val onSuccessContainer: Color,
    val warning: Color,        // Cảnh báo (cam/vàng)
    val warningContainer: Color,
    val onWarningContainer: Color,
    val error: Color,          // Lỗi (đỏ)
    val errorContainer: Color,
    val onErrorContainer: Color,

    // Surface tokens
    val surface: Color,
    val onSurface: Color,
    val surfaceVariant: Color,

    // Gradient token — thường dùng cho headers, hero sections
    val gradient: List<Color>,
)

// ─── Light Token Set ──────────────────────────────────────────────────────────

private val LightAppColors = AppColorTokens(
    // Brand: AI Purple
    brand = Color(0xFF7C3AED),
    brandVariant = Color(0xFFEDE9FE),
    brandOnBrand = Color.White,

    // Success: Xanh lá
    success = Color(0xFF16A34A),
    successContainer = Color(0xFFDCFCE7),
    onSuccessContainer = Color(0xFF14532D),

    // Warning: Cam
    warning = Color(0xFFD97706),
    warningContainer = Color(0xFFFEF3C7),
    onWarningContainer = Color(0xFF92400E),

    // Error: Đỏ
    error = Color(0xFFDC2626),
    errorContainer = Color(0xFFFEE2E2),
    onErrorContainer = Color(0xFF7F1D1D),

    // Surface
    surface = Color.White,
    onSurface = Color(0xFF1C1C1E),
    surfaceVariant = Color(0xFFF9FAFB),

    // Gradient: Purple → Blue
    gradient = listOf(
        Color(0xFF7C3AED),
        Color(0xFF2563EB),
        Color(0xFF06B6D4),
    ),
)

// ─── Dark Token Set ───────────────────────────────────────────────────────────

private val DarkAppColors = AppColorTokens(
    // Brand: Nhạt hơn để contrast tốt trên dark background
    brand = Color(0xFFA78BFA),
    brandVariant = Color(0xFF2E1065),
    brandOnBrand = Color(0xFF1C1C2E),

    // Success
    success = Color(0xFF4ADE80),
    successContainer = Color(0xFF14532D),
    onSuccessContainer = Color(0xFFBBF7D0),

    // Warning
    warning = Color(0xFFFBBF24),
    warningContainer = Color(0xFF78350F),
    onWarningContainer = Color(0xFFFDE68A),

    // Error
    error = Color(0xFFF87171),
    errorContainer = Color(0xFF7F1D1D),
    onErrorContainer = Color(0xFFFECACA),

    // Surface
    surface = Color(0xFF1C1C1E),
    onSurface = Color(0xFFF9FAFB),
    surfaceVariant = Color(0xFF2C2C2E),

    // Gradient: Darker purple → blue
    gradient = listOf(
        Color(0xFF4C1D95),
        Color(0xFF1E3A8A),
        Color(0xFF164E63),
    ),
)

// ─── CompositionLocal ─────────────────────────────────────────────────────────

/**
 * staticCompositionLocalOf vs compositionLocalOf:
 *
 * compositionLocalOf: khi value thay đổi, CHỈ recompose phần sử dụng value đó
 * staticCompositionLocalOf: khi value thay đổi, TOÀN BỘ subtree recompose
 *
 * Dùng staticCompositionLocalOf khi:
 * - Value ít thay đổi (design tokens, theme → chỉ đổi khi dark/light mode)
 * - Muốn performance tốt hơn ở trường hợp KHÔNG thay đổi
 *
 * Dùng compositionLocalOf khi:
 * - Value thay đổi thường xuyên (user data, scroll state, ...)
 */
val LocalAppColors = staticCompositionLocalOf<AppColorTokens> {
    error("AppColorTokens chưa được cung cấp. Hãy wrap trong AppDesignTheme { }")
}

// ─── AppTheme Access Object ───────────────────────────────────────────────────

/**
 * AppTheme object — access point cho design tokens
 *
 * Cách dùng:
 *   AppTheme.colors.brand  // Lấy brand color
 *   AppTheme.colors.success // Lấy success color
 *
 * Tại sao dùng object thay vì gọi LocalAppColors.current trực tiếp?
 * → API gọn hơn: AppTheme.colors.brand vs LocalAppColors.current.brand
 * → Consistent với MaterialTheme.colorScheme pattern
 * → Có thể thêm AppTheme.typography, AppTheme.shapes sau này
 */
object AppTheme {
    // @ReadOnlyComposable: hint cho compiler rằng đây là read-only access
    // → Không tạo sub-composition, performance tốt hơn
    val colors: AppColorTokens
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColors.current
}

// ─── AppDesignTheme Wrapper ───────────────────────────────────────────────────

/**
 * AppDesignTheme — wrapper theme cho toàn app
 *
 * Tại sao KHÔNG thay MaterialTheme mà WRAP thêm vào?
 * → MaterialTheme cung cấp composables M3 (Button, Card, etc.)
 * → AppDesignTheme cung cấp custom tokens ngoài M3
 * → 2 layer theme: M3 layer + Custom layer
 */
@Composable
fun AppDesignTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val appColors = if (darkTheme) DarkAppColors else LightAppColors

    // CompositionLocalProvider: inject custom tokens vào composition tree
    // Tất cả composable bên trong đều có thể dùng AppTheme.colors
    CompositionLocalProvider(
        LocalAppColors provides appColors,
    ) {
        MaterialTheme(
            colorScheme = if (darkTheme) darkColorScheme() else lightColorScheme(),
            content = content,
        )
    }
}

// ─── Demo Screen ──────────────────────────────────────────────────────────────

@Composable
fun DesignSystemDemoScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.colors.surface)
            .verticalScroll(rememberScrollState()),
    ) {
        // Gradient header
        GradientHeader()

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // AI Recommended card
            AiRecommendedCard()

            // Status cards
            StatusCardsRow()

            // Token reference
            TokenReferenceCard()
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

// ─── Gradient Header ──────────────────────────────────────────────────────────

@Composable
private fun GradientHeader(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp)
            .background(
                brush = Brush.linearGradient(colors = AppTheme.colors.gradient),
            ),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Design System Demo",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Premium badge
            Surface(
                shape = MaterialTheme.shapes.extraSmall,
                color = Color.White.copy(alpha = 0.2f),
            ) {
                Text(
                    text = "✨ PREMIUM",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                )
            }
        }
    }
}

// ─── AI Recommended Card ──────────────────────────────────────────────────────

@Composable
private fun AiRecommendedCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colors.brandVariant,
        ),
        shape = MaterialTheme.shapes.large,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // "AI RECOMMENDED" badge — dùng brand color
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "🤖 AI Headshot Generator",
                    style = MaterialTheme.typography.titleMedium,
                    color = AppTheme.colors.onSurface,
                    fontWeight = FontWeight.Bold,
                )

                // AI RECOMMENDED badge
                Surface(
                    shape = MaterialTheme.shapes.extraSmall,
                    color = AppTheme.colors.brand,
                ) {
                    Text(
                        text = "AI RECOMMENDED",
                        color = AppTheme.colors.brandOnBrand,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Professional headshots in minutes using AI. Perfect for LinkedIn and CVs.",
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.colors.onSurface.copy(alpha = 0.7f),
            )

            Spacer(modifier = Modifier.height(12.dp))

            // TRENDING pill — dùng warning color
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Surface(
                    shape = MaterialTheme.shapes.extraLarge, // Pill shape
                    color = AppTheme.colors.warningContainer,
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(text = "📈", fontSize = 12.sp)
                        Text(
                            text = "TRENDING",
                            color = AppTheme.colors.onWarningContainer,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }

                Surface(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = AppTheme.colors.brand.copy(alpha = 0.1f),
                ) {
                    Text(
                        text = "⭐ 4.9 rating",
                        color = AppTheme.colors.brand,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    )
                }
            }
        }
    }
}

// ─── Status Cards ─────────────────────────────────────────────────────────────

@Composable
private fun StatusCardsRow(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        // Success card
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = AppTheme.colors.successContainer,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = "✅", fontSize = 20.sp)
                Column {
                    Text(
                        text = "Upload Complete",
                        style = MaterialTheme.typography.titleSmall,
                        color = AppTheme.colors.onSuccessContainer,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = "5 photos processed successfully",
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.colors.onSuccessContainer,
                    )
                }
            }
        }

        // Error card
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = AppTheme.colors.errorContainer,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = "❌", fontSize = 20.sp)
                Column {
                    Text(
                        text = "Network Error",
                        style = MaterialTheme.typography.titleSmall,
                        color = AppTheme.colors.onErrorContainer,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = "Could not connect. Please try again.",
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.colors.onErrorContainer,
                    )
                }
            }
        }

        // Warning card
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = AppTheme.colors.warningContainer,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = "⚠️", fontSize = 20.sp)
                Column {
                    Text(
                        text = "Storage 80% Full",
                        style = MaterialTheme.typography.titleSmall,
                        color = AppTheme.colors.onWarningContainer,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = "Consider freeing up space",
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.colors.onWarningContainer,
                    )
                }
            }
        }
    }
}

// ─── Token Reference Card ──────────────────────────────────────────────────────

@Composable
private fun TokenReferenceCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "💡 Token Reference",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Hiển thị color tokens như reference
            listOf(
                Triple("AppTheme.colors.brand", AppTheme.colors.brand, "Brand purple"),
                Triple("AppTheme.colors.success", AppTheme.colors.success, "Success green"),
                Triple("AppTheme.colors.warning", AppTheme.colors.warning, "Warning orange"),
                Triple("AppTheme.colors.error", AppTheme.colors.error, "Error red"),
            ).forEach { (tokenName, color, description) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // Color dot
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(MaterialTheme.shapes.extraSmall)
                            .background(color),
                    )
                    Text(
                        text = tokenName,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f),
                    )
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

// ─── Wrapper cho Previews ──────────────────────────────────────────────────────

/**
 * Cách dùng AppDesignTheme trong app thực tế:
 *
 * // MainActivity.kt
 * setContent {
 *     AppDesignTheme {
 *         // Tất cả screens bên trong đều access được AppTheme.colors
 *         NavHost(...)
 *     }
 * }
 */

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true, name = "Design System - Light")
@Composable
private fun DesignSystemLightPreview() {
    AppDesignTheme(darkTheme = false) {
        DesignSystemDemoScreen()
    }
}

@Preview(
    showBackground = true,
    name = "Design System - Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun DesignSystemDarkPreview() {
    AppDesignTheme(darkTheme = true) {
        DesignSystemDemoScreen()
    }
}

@Preview(showBackground = true, name = "AI Recommended Card Preview")
@Composable
private fun AiRecommendedCardPreview() {
    AppDesignTheme {
        AiRecommendedCard(modifier = Modifier.padding(16.dp))
    }
}
