package com.example.naguorg

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext



@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun NaguOrganicsApp() {
    Text(text = "welcomeMessage")
    val isRefreshing = remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    // var selectedCategory by remember { mutableStateOf("All Products") }
    var selectedCategory by remember { mutableStateOf("All category") }
    var products by remember { mutableStateOf<List<Product>>(emptyList()) }
    val context = LocalContext.current
    val forceRecompose = remember { mutableStateOf(false) }


    LaunchedEffect(selectedCategory) {

        isLoading = true
        products = fetchProducts("All category") // Initial Load
        isLoading = false
        products = fetchProducts(selectedCategory)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = saveToRoomDatabase(context, products) // Pass 'context' and 'products'
                // Handle the result
            } catch (e: Exception) {
                // Handle the exception
            }
        }

        isLoading = false
    }

    val filteredProducts = products.filter { product ->
        val matchesSearch = product.name.contains(searchQuery, ignoreCase = true)
        val matchesCategory = selectedCategory == "All category" || product.category == selectedCategory
        matchesSearch && matchesCategory
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = Color(0xFFFFA000), fontWeight = FontWeight.ExtraBold)) {
                                append("NAGU")
                            }
                            withStyle(style = SpanStyle(color = Color(0xFF004AAD), fontWeight = FontWeight.ExtraBold)) {
                                append(" ORGANICS")
                            }
                        },
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                actions = {
//                    Icon(
//                        imageVector = Icons.Default.ShoppingCart,
//                        contentDescription = "Cart",
//                        modifier = Modifier
//                            .padding(end = 16.dp)
//                            .clickable {
//                                val intent = Intent(context, CartActivity::class.java)
//                                context.startActivity(intent)
//                            }
//                    )

                    OutlinedButton(
                        onClick = { inviteFriends(context) },
                        modifier = Modifier
                            .padding(end = 4.dp) // Reduced padding
                            .size(70.dp, 28.dp), // Slightly increased height for better fit
                        shape = RoundedCornerShape(8.dp), // Slightly rounded corners
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.Black), // Black border
                        contentPadding = PaddingValues(0.dp) // Remove extra padding inside button
                    ) {
                        Text(
                            text = "Invite",
                            color = Color.Blue, // Blue text color
                            fontStyle = FontStyle.Italic,
                            fontSize = 10.sp, // Adjusted font size
                            fontWeight = FontWeight.Bold, // Bold text
                            modifier = Modifier.align(Alignment.CenterVertically) // Center text vertically
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val intent = Intent(context, CartActivity::class.java)
                    context.startActivity(intent)
                },
                containerColor = Color(0xFF4CAF50)
            ) {
                Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "Shop")
            }
        }
    ) {

            paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            // ✅ Search Box
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search") },
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon") },
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                shape = MaterialTheme.shapes.medium
            )
            Log.d("Firestore", "Selected category: $selectedCategory")
            // ✅ Category Selection
            CategoryDropdown(selectedCategory) { category ->
                selectedCategory = category
                isLoading = true
                CoroutineScope(Dispatchers.Main).launch {
                    products = fetchProducts(selectedCategory)
                    isLoading = false
                }
            }

            // ✅ Swipe-to-Refresh
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing = isRefreshing.value),
                onRefresh = {
                    isRefreshing.value = true
                    CoroutineScope(Dispatchers.Main).launch {
                        val currentCategory = selectedCategory // Store the current category
                        products = fetchProducts("All category") // Fetch all products
                        selectedCategory = currentCategory // Restore the current category
                        isRefreshing.value = false
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
            {
                if (isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    val cartList = remember { mutableStateListOf<Product>() }

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (filteredProducts.isNotEmpty()) {
                            items(filteredProducts) { product ->
                                ProductItem(product) { selectedProduct ->
                                    Log.d("Cart", "Product added: ${selectedProduct.name}")
                                    cartList.add(selectedProduct)
                                }
                            }
                        } else {
                            item {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Text("No products found", color = Color.Gray)
                                }
                            }
                        }
                    }
                    LaunchedEffect(forceRecompose.value) {
                        // This will be triggered whenever forceRecompose.value changes
                        // You might want to add logic here to update the products list
                        // or perform other actions before the recomposition
                    }
                }
            }
        }
    }
}
fun inviteFriends(context: Context) {
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, "Check out Nagu Organics! Download the app now: https://i.diawi.com/eZ843H")
        type = "text/plain"
    }
    context.startActivity(Intent.createChooser(shareIntent, "Invite via"))
}
suspend fun fetchProducts(category: String): List<Product> {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    return try {
        // Initialize Room database (without changing function signature)
        val roomDb = ProductDatabase.getDatabase(MyApplication.instance)
        val dao = roomDb.productDao()

        // Check internet connection
        if (!isInternetAvailable()) {
            Log.d("Offline", "No internet, loading from Room Database")
            return dao.getAllProducts() // Load cached data from Room if offline
        }

        val query = when (category) {
            "All category" -> db.collection("products")
            else -> db.collection("products").whereEqualTo("category", category)
        }

        val result = query.get().await()
        val fetchedProducts = result.documents.mapNotNull { document ->
            document.toObject(Product::class.java)?.copy(
                description = document.getString("description") ?: ""
            )
        }
        //    val fetchedProducts = result.documents.mapNotNull { it.toObject(Product::class.java) }

        // Save fetched data to Room for offline use
        CoroutineScope(Dispatchers.IO).launch {
            dao.insertProducts(fetchedProducts)
        }

        fetchedProducts
    } catch (e: Exception) {
        Log.e("Firestore", "Error fetching products: ${e.message}")
        val roomDb = ProductDatabase.getDatabase(MyApplication.instance)
        val dao = roomDb.productDao()
        dao.getAllProducts() // Return cached data in case of error
    }
}

fun saveToRoomDatabase(context: Context, fetchedProducts: List<Product>) {
    val roomDb = ProductDatabase.getDatabase(context)
    val dao = roomDb.productDao()

    CoroutineScope(Dispatchers.IO).launch {
        fetchedProducts.forEach { product ->
            dao.insertProduct(product) // Insert each product one by one
        }
    }
}
fun isInternetAvailable(): Boolean {
    val connectivityManager = MyApplication.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetworkInfo
    return activeNetwork != null && activeNetwork.isConnected
}
