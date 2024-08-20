package com.example.foodlist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodlist.ui.theme.FoodListTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FoodListTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ProductApp()
                }
            }
        }
    }
}

@Composable
fun ProductApp() {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        ProductFilterScreen()
    }
}

@Composable
fun ProductFilterScreen() {
    var searchQuery by remember { mutableStateOf("") }
    var onlyInStock by remember { mutableStateOf(false) }

    Column {
        // Search Field
        BasicTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            decorationBox = { innerTextField ->
                if (searchQuery.isEmpty()) {
                    Text(text = "Search...", fontSize = 16.sp, color = Color.Gray)
                }
                innerTextField()
            }
        )

        // Stock Checkbox
        Row(modifier = Modifier.padding(bottom = 16.dp)) {
            Checkbox(
                checked = onlyInStock,
                onCheckedChange = { onlyInStock = it }
            )
            Text(text = "Only show products in stock")
        }

        // Product Table
        ProductTable(products, searchQuery, onlyInStock)
    }
}

@Composable
fun ProductTable(products: List<Product>, searchQuery: String, onlyInStock: Boolean) {
    Column {
        // Table Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Name", fontWeight = FontWeight.Bold)
            Text(text = "Price", fontWeight = FontWeight.Bold)
        }

        // Grouped Products by Category
        val filteredProducts = products.filter {
            (it.name.contains(searchQuery, ignoreCase = true)) &&
                    (!onlyInStock || it.stocked)
        }.groupBy { it.category }

        filteredProducts.forEach { (category, productsInCategory) ->
            Text(text = category, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 8.dp))
            productsInCategory.forEach { product ->
                ProductRow(product)
            }
        }
    }
}

@Composable
fun ProductRow(product: Product) {
    val textColor = if (!product.stocked) Color.Red else Color.Black

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = product.name, color = textColor)
        Text(text = product.price)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FoodListTheme {
        ProductApp()
    }
}

data class Product(val category: String, val price: String, val stocked: Boolean, val name: String)

val products = listOf(
    Product("Fruits", "$1", true, "Apple"),
    Product("Fruits", "$1", true, "Dragonfruit"),
    Product("Fruits", "$2", false, "Passionfruit"),
    Product("Vegetables", "$2", true, "Spinach"),
    Product("Vegetables", "$4", false, "Pumpkin"),
    Product("Vegetables", "$1", true, "Peas")
)
