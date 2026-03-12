package com.example.myfirstkmp.migration.session3.exercises

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apero.composetraining.common.SampleData
import com.example.myfirstkmp.theme.AppTheme

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

fun formatCurrency(total: Double): String {
    val parts = total.toString().split(".")
    val intPart = parts[0]
    val decimalPart = parts.getOrNull(1)?.padEnd(2, '0')?.take(2) ?: "00"

    val formattedInt = intPart.reversed().chunked(3).joinToString(",").reversed()

    return "$$formattedInt.$decimalPart"
}

@Composable
fun ShoppingCartScreen() {
    val products = SampleData.products.take(5)

    val cartItems = remember {
        mutableStateListOf(*products.map { CartItem(it.id, it.name, it.price, 0) }.toTypedArray())
    }

    val total by remember { derivedStateOf { cartItems.sumOf { it.price * it.quantity } } }

    Scaffold(
        topBar = {
            CartHeader(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            )
        },
        bottomBar = {
            CheckoutSummary(
                total = total
            ) {

            }
        }
    ) { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding()
            ) {
                items(
                    items = cartItems,
                    key = { item -> item.productId }
                ) { product ->
                    CardItem(
                        modifier = Modifier,
                        name = product.name,
                        price = product.price.toString(),
                        amount = product.quantity,
                        clickMinus = {
                            if (product.quantity > 0) {
                                val idx = cartItems.indexOf(product)
                                cartItems[idx] = product.copy(quantity = product.quantity - 1)
                            }
                        },
                        clickPlus = {
                            val idx = cartItems.indexOf(product)
                            cartItems[idx] = product.copy(quantity = product.quantity + 1)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CheckoutSummary(
    total: Double,
    onCheckoutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Total:",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = formatCurrency(total),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color(0xFF1E88E5),
                    fontWeight = FontWeight.Bold
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onCheckoutClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF5AA8F7)
            )
        ) {
            Text(text = "Checkout →")
        }
    }
}

@Composable
fun CartHeader(
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {

        Image(
            imageVector = Icons.Default.Store,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(30.dp)
        )

        Text(
            text = "Shopping Cart",
            color = Color.Black,
            fontSize = 20.sp,
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp)
        )
    }
}

@Composable
fun RowScope.CardItemTitle(
    modifier: Modifier = Modifier,
    title: String,
    price: String
) {

    Column(
        modifier = modifier
    ) {
        Text(
            text = title,
            color = Color.Black,
            fontSize = 18.sp,
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
        )

        Text(
            text = price,
            color = Color.Black,
            fontSize = 14.sp,
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Normal,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
        )
    }

}

@Composable
private fun RowScope.CaredItemButtons(
    modifier: Modifier = Modifier,
    amount: Int,
    clickMinus: () -> Unit,
    clickPlus: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
    ) {

        Box(
            modifier = Modifier
                .wrapContentSize()
                .background(
                    color = Color.LightGray,
                    shape = CircleShape
                )
                .clickable {
                    clickMinus()
                }
                .padding(5.dp)
        ) {
            Image(
                imageVector = Icons.Default.Remove,
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
        }

        Text(
            text = amount.toString(),
            color = Color.Black,
            fontSize = 18.sp,
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 10.dp)
        )

        Box(
            modifier = Modifier
                .wrapContentSize()
                .background(
                    color = Color.Green.copy(alpha = 0.85f),
                    shape = CircleShape
                )
                .clickable {
                    clickPlus()
                }
                .padding(5.dp)
        ) {
            Image(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
        }

    }
}

@Composable
fun CardItem(
    modifier: Modifier = Modifier,
    name: String,
    price: String,
    amount: Int,
    clickMinus: () -> Unit,
    clickPlus: () -> Unit
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = Color.White, shape = RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {

        CardItemTitle(
            title = name,
            price = price,
            modifier = Modifier
                .weight(1f)
                .wrapContentHeight()
                .padding(start = 12.dp)
        )

        CaredItemButtons(
            amount = amount,
            clickPlus = clickPlus,
            clickMinus = clickMinus
        )

    }
}

@Preview(showBackground = false)
@Composable
private fun ListItemPreview() {
    CardItem(
        name = "Doan Khac Minh",
        price = "$2301",
        amount = 23,
        clickMinus = {},
        clickPlus = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun ShoppingCartScreenPreview() {
    AppTheme { ShoppingCartScreen() }
}

