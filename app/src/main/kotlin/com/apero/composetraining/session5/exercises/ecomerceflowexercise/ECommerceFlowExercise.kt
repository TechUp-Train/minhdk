package com.apero.composetraining.session5.exercises.ecomerceflowexercise

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.apero.composetraining.common.AppTheme
import kotlinx.serialization.Serializable

/**
 * ⭐⭐⭐ BÀI TẬP 3: E-Commerce Flow (Challenge — 90 phút)
 *
 * Yêu cầu:
 * - Flow: CategoryListKey → ProductListKey(categoryId) → ProductDetailKey(productId) → CartKey
 * - Dùng sealed class ProductFlowKey để nhóm tất cả keys
 * - "Add to Cart" không navigate — chỉ update cartCount (Int state)
 * - CartKey hiện danh sách items + tổng giá
 * - BackHandler trên CartKey: hiện confirm dialog "Bỏ giỏ hàng?" trước khi back
 * - Badge trên cart icon (NavigationBar) hiện số lượng items
 *
 * Sealed class gợi ý:
 * ```kotlin
 * sealed class ProductFlowKey {
 *     data object CategoryList : ProductFlowKey()
 *     data class ProductList(val categoryId: Int) : ProductFlowKey()
 *     data class ProductDetail(val productId: Int, val categoryId: Int) : ProductFlowKey()
 *     data object Cart : ProductFlowKey()
 * }
 */

val categories = listOf(
    "Electronics",
    "Fashion & Apparel",
    "Home & Garden",
    "Beauty & Personal Care",
    "Sports & Outdoors",
    "Toys & Hobbies",
    "Automotive",
    "Books & Media"
)

sealed class CategoryFlow : NavKey {
    @Serializable
    data object Category : CategoryFlow()

    @Serializable
    data object ListProduct : CategoryFlow()

    @Serializable
    data class ProductDetail(val productId: Int) : CategoryFlow()

    @Serializable
    data object Cart : CategoryFlow()

    @Serializable
    data object Back : CategoryFlow()
}

//----------------------------------Category Screen----------------------------------

@Composable
private fun CategoryItem(
    title: String,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF3ECF4),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun CategoryScreen(
    count: Int = 3,
    onNavigate: (CategoryFlow) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F2F7))
            .padding(horizontal = 16.dp)
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Categories",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Box {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = null,
                    modifier = Modifier.size(26.dp)
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 4.dp, y = (-2).dp)
                        .size(16.dp)
                        .background(Color.Red, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = count.toString(),
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(items = categories, key = { item -> item }) { item ->
                CategoryItem(
                    title = item,
                    onClick = { onNavigate(CategoryFlow.ListProduct) }
                )
            }
        }
    }
}

//----------------------------------List Product Screen----------------------------------

@SuppressLint("DefaultLocale")
@Composable
fun ProductRow(
    product: Product,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )

            Text(
                text = "$${String.format("%.2f", product.price)}",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6A4DF7)
                )
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = product.description,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(6.dp))

        HorizontalDivider(Modifier, DividerDefaults.Thickness, color = Color(0xFFE6DDEF))
    }
}

@Preview
@Composable
private fun ListProductScreen(
    products: List<Product> = sampleProductList,
    onNavigate: (CategoryFlow) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F2F7))
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                modifier = Modifier.clickable {
                    onNavigate(CategoryFlow.Back)
                }
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "Products",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        // LIST
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(products) { product ->
                ProductRow(product = product) { onNavigate(CategoryFlow.ProductDetail(product.id)) }
            }
        }
    }
}

//----------------------------------Product Detail Screen----------------------------------

sealed class ProductDetailIntent {
    data class AddToCart(val prodId: Int) : ProductDetailIntent()
    data object ViewCart : ProductDetailIntent()
}

@Composable
fun SpecRow(spec: ProductSpec) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = spec.label,
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
        )
        Text(
            text = spec.value,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
        )
    }

    Divider(color = Color(0xFFE6DDEF))
}

@SuppressLint("DefaultLocale")
@Composable
fun ProductDetailScreen(
    product: Product,
    onIntent: (CartIntent) -> Unit,
    onNavigate: (CategoryFlow) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F2F7))
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                modifier = Modifier.clickable {
                    onNavigate(CategoryFlow.Back)
                }
            )

            Spacer(Modifier.width(12.dp))

            Text(
                text = "Product Details",
                style = MaterialTheme.typography.titleMedium
            )
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {

            item {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "$${String.format("%.2f", product.price)}",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6A4DF7)
                    )
                )

                Spacer(Modifier.height(20.dp))

                Text(
                    text = "DESCRIPTION",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = Color.Gray,
                        letterSpacing = 1.1.sp
                    )
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(Modifier.height(20.dp))
            }

            items(product.specs) { spec ->
                SpecRow(spec)
            }

            item { Spacer(Modifier.height(20.dp)) }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Button(
                onClick = { onIntent(CartIntent.AddToCart(product)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6A4DF7)
                )
            ) {
                Text(text = "ADD TO CART", color = Color.White)
            }

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = { onNavigate(CategoryFlow.Cart) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEDE3FF),
                    contentColor = Color(0xFF6A4DF7)
                )
            ) {
                Text("VIEW CART")
            }
        }
    }
}

//----------------------------------Cart Screen----------------------------------
sealed class CartIntent {
    data class Plus(val prodId: Int) : CartIntent()
    data class Minus(val prodId: Int) : CartIntent()
    data class AddToCart(val prod: Product) : CartIntent()
}

@SuppressLint("DefaultLocale")
@Composable
fun CartItemRow(
    item: CartItem,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = item.product.name,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = "$${String.format("%.2f", item.product.price)}",
                color = Color(0xFF6A4DF7),
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(4.dp))

        Text(
            text = item.specLine,
            color = Color.Gray,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .height(32.dp)
                .background(Color(0xFFE8E0ED), RoundedCornerShape(20.dp))
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                Icons.Default.Remove,
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp)
                    .clickable { onDecrease() }
            )

            Spacer(Modifier.width(10.dp))

            Text(
                text = item.quantity.toString(),
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.width(10.dp))

            Icon(
                Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp)
                    .clickable { onIncrease() }
            )
        }

        Spacer(Modifier.height(8.dp))
        HorizontalDivider(Modifier, DividerDefaults.Thickness, color = Color(0xFFE6DDEF))
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun CartSummary(
    subtotal: Double,
    shipping: Double,
    onCheckout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .background(Color.White)
            .padding(20.dp)
    ) {

        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
            Text("Subtotal")
            Text("$${String.format("%.2f", subtotal)}")
        }

        Spacer(Modifier.height(8.dp))

        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
            Text("Shipping")
            Text("Free")
        }

        Spacer(Modifier.height(14.dp))

        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
            Text(
                "Total",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                "$${String.format("%.2f", subtotal)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = onCheckout,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6A4DF7)
            )
        ) {
            Text("Checkout", color = Color.White)
        }
    }
}

@Composable
private fun CartScreen(
    cartItems: List<CartItem>,
    action: (CartIntent) -> Unit,
    onNavigate: (CategoryFlow) -> Unit
) {

    val subtotal = cartItems.sumOf { it.product.price * it.quantity }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F2F7))
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                modifier = Modifier.clickable {
                    onNavigate(CategoryFlow.Back)
                }
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "Cart",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(cartItems) { item ->
                CartItemRow(
                    item = item,
                    onIncrease = {
                        action(CartIntent.Plus(item.product.id))
                    },
                    onDecrease = {
                        if (item.quantity > 1) {
                            action(CartIntent.Minus(item.product.id))
                        }
                    }
                )
            }
        }

        CartSummary(
            subtotal = subtotal,
            shipping = 0.0,
            onCheckout = {}
        )
    }
}


fun handleNavigation(
    backStack: NavBackStack<NavKey>,
    intent: CategoryFlow
) {
    when (intent) {
        is CategoryFlow.Category -> {}

        is CategoryFlow.ListProduct -> backStack.add(CategoryFlow.ListProduct)

        is CategoryFlow.ProductDetail -> backStack.add(CategoryFlow.ProductDetail(intent.productId))

        is CategoryFlow.Cart -> backStack.add(CategoryFlow.Cart)

        is CategoryFlow.Back -> backStack.removeLastOrNull()
    }
}

fun updateQuantity(id: Int, old: MutableList<CartItem>, increase: Boolean): MutableList<CartItem> {
    val idx = old.indexOfFirst { it.product.id == id }
    if (idx < 0) return old
    if (increase) {
        old[idx] = old[idx].copy(quantity = old[idx].quantity + 1)
    } else {
        old[idx] = old[idx].copy(quantity = old[idx].quantity - 1)
    }
    return old
}

fun reduceState(old: SnapshotStateList<CartItem>, intent: CartIntent) {
    when (intent) {
        is CartIntent.Plus -> updateQuantity(id = intent.prodId, old, true)
        is CartIntent.Minus -> updateQuantity(id = intent.prodId, old, false)
        is CartIntent.AddToCart -> {
            old.add(
                CartItem(
                    product = intent.prod,
                    quantity = 1,
                    intent.prod.specs.map { it.value }.joinToString()
                )
            )
        }
    }
}

@Composable
fun ECommerceApp() {

    val cart = remember { mutableStateListOf<CartItem>() }

    val backStack = rememberNavBackStack(CategoryFlow.Category)

    val navCallback = { intent: CategoryFlow ->
        handleNavigation(backStack, intent)
    }

    val reduceStateCallback = { intent: CartIntent ->
        reduceState(cart, intent)
    }

    BackHandler {
        if (backStack.size <= 1) return@BackHandler
        backStack.removeLastOrNull()
    }

    NavDisplay(
        backStack = backStack,
        entryProvider = entryProvider {

            entry<CategoryFlow.Category> {
                CategoryScreen(count = cart.size, onNavigate = navCallback)
            }

            entry<CategoryFlow.ListProduct> {
                ListProductScreen(onNavigate = navCallback)
            }

            entry<CategoryFlow.ProductDetail> {
                sampleProductList.find { prod -> prod.id == it.productId }?.let { absProd ->
                    ProductDetailScreen(
                        product = absProd,
                        onIntent = reduceStateCallback,
                        onNavigate = navCallback
                    )
                }
            }

            entry<CategoryFlow.Cart> {
                CartScreen(cartItems = cart, action = reduceStateCallback, onNavigate = navCallback)
            }

        }
    )
}

@Preview(showBackground = true)
@Composable
private fun ECommerceAppPreview() {
    AppTheme { ECommerceApp() }
}
