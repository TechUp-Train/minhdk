package com.apero.composetraining.session5.demos

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.apero.composetraining.common.*
import kotlinx.serialization.Serializable

// ============================================================
// DEMO 1: NavHost cơ bản — 2 screens
// ============================================================

@Composable
fun BasicNavigationDemo() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Welcome) {
        composable<Welcome> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("👋 Welcome!", fontSize = 32.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { navController.navigate(Home) }) {
                    Text("Get Started")
                }
            }
        }

        composable<Home> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("🏠 Home", fontSize = 32.sp, fontWeight = FontWeight.Bold)
                Text("Welcome back!", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(onClick = { navController.popBackStack() }) {
                    Text("← Go Back")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BasicNavigationDemoPreview() {
    AppTheme { BasicNavigationDemo() }
}

// ============================================================
// DEMO 2: Navigation với Arguments (Type-safe routes)
// ============================================================

@Serializable data class DemoProduct(val productId: Int)

@Composable
fun NavigationWithArgsDemo() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = SessionList) {
        composable<SessionList> {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Products", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(8.dp))
                SampleData.products.take(5).forEach { product ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        onClick = { navController.navigate(DemoProduct(product.id)) }
                    ) {
                        Text(
                            "${product.name} — $${product.price}",
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }

        composable<DemoProduct> { backStackEntry ->
            val route = backStackEntry.toRoute<DemoProduct>()
            val product = SampleData.products.find { it.id == route.productId }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text("Product #${route.productId}", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                product?.let {
                    Text(it.name, fontSize = 24.sp)
                    Text("$${it.price}", color = MaterialTheme.colorScheme.primary)
                    Text(it.description)
                }
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(onClick = { navController.popBackStack() }) {
                    Text("← Back")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NavigationWithArgsDemoPreview() {
    AppTheme { NavigationWithArgsDemo() }
}

// ============================================================
// DEMO 3: Bottom Navigation
// ============================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationDemo() {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    data class BottomNavItem(
        val label: String,
        val icon: @Composable () -> Unit,
        val route: Any
    )

    val items = listOf(
        BottomNavItem("Home", { Icon(Icons.Default.Home, "Home") }, Home),
        BottomNavItem("Search", { Icon(Icons.Default.Search, "Search") }, Search),
        BottomNavItem("Profile", { Icon(Icons.Default.Person, "Profile") }, Profile),
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEach { item ->
                    NavigationBarItem(
                        icon = item.icon,
                        label = { Text(item.label) },
                        selected = false, // Simplified for demo
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Home,
            modifier = Modifier.padding(padding)
        ) {
            composable<Home> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("🏠 Home Tab", fontSize = 24.sp)
                }
            }
            composable<Search> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("🔍 Search Tab", fontSize = 24.sp)
                }
            }
            composable<Profile> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("👤 Profile Tab", fontSize = 24.sp)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomNavigationDemoPreview() {
    AppTheme { BottomNavigationDemo() }
}
