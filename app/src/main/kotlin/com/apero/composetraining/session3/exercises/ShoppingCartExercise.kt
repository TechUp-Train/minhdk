package com.apero.composetraining.session3.exercises

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apero.composetraining.common.AppTheme
import com.apero.composetraining.common.SampleData

/**
 * ⭐⭐ BÀI TẬP 2: Shopping Cart (Medium)
 *
 * Yêu cầu:
 * - LazyColumn: 5 products (Name + Price + Quantity selector [- count +])
 * - Bottom bar: "Total: $XXX" (derivedStateOf tính tổng)
 * - State hoisting: CartScreen(items, onQuantityChange)
 * - Total tự update khi thay đổi quantity
 * - Quantity không < 0
 * - Stateless product item component
 */

data class CartItem(
    val productId: Int,
    val name: String,
    val price: Double,
    val quantity: Int = 0
)

@Composable
fun ShoppingCartScreen() {
    val products = SampleData.products.take(5)

    // TODO: [Session 3] Bài tập 2 - Tạo state cho cart items
    // val cartItems = remember {
    //     mutableStateListOf(*products.map { CartItem(it.id, it.name, it.price, 0) }.toTypedArray())
    // }

    // TODO: [Session 3] Bài tập 2 - Tính total bằng derivedStateOf
    // val total by remember { derivedStateOf { cartItems.sumOf { it.price * it.quantity } } }

    Scaffold(
        bottomBar = {
            // TODO: [Session 3] Bài tập 2 - Bottom bar hiển thị "Total: $XXX"
            // Surface với tonalElevation, Row chứa Text "Total" + Text "$total"
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            // TODO: [Session 3] Bài tập 2 - LazyColumn hiển thị cart items
            // Mỗi item: CartItemRow(item, onIncrease, onDecrease)

            // Placeholder
            Text("Bắt đầu code Shopping Cart ở đây!", modifier = Modifier.padding(16.dp))
        }
    }
}

// TODO: [Session 3] Bài tập 2 - Tạo CartItemRow composable (STATELESS)
// Params: cartItem: CartItem, onIncrease: () -> Unit, onDecrease: () -> Unit
// Layout: Row chứa Column(name, price) + Row(Button "-" + Text quantity + Button "+")

@Preview(showBackground = true)
@Composable
private fun ShoppingCartScreenPreview() {
    AppTheme { ShoppingCartScreen() }
}
