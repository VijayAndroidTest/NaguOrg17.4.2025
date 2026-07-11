package com.example.naguorg.view

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.mutableStateListOf
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
import com.example.naguorg.products.Product
import com.example.naguorg.products.ProductItem
import com.example.naguorg.viewmodel.ProductViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NaguOrganicsApp(
    viewModel: ProductViewModel
) {

    val context = LocalContext.current

    val products = viewModel.products
    val isLoading = viewModel.loading

    var searchQuery by remember {
        mutableStateOf("")
    }

    var selectedCategory by remember {
        mutableStateOf("All category")
    }

    val refreshing = remember {
        mutableStateOf(false)
    }

    val cartList: SnapshotStateList<Product> =
        remember {
            mutableStateListOf()
        }

    LaunchedEffect(Unit) {
        viewModel.loadProducts("All category")
    }

    val filteredProducts =
        products.filter {

            val matchSearch =
                it.name.contains(
                    searchQuery,
                    true
                )

            val matchCategory =
                selectedCategory == "All category" ||
                        it.category == selectedCategory

            matchSearch && matchCategory
        }

    Scaffold(

        topBar = {

            TopAppBar(

                title = {

                    Text(

                        text = buildAnnotatedString {

                            withStyle(
                                SpanStyle(
                                    color = Color(0xFFFFA000),
                                    fontWeight = FontWeight.ExtraBold
                                )
                            ) {
                                append("NAGU")
                            }

                            withStyle(
                                SpanStyle(
                                    color = Color(0xFF004AAD),
                                    fontWeight = FontWeight.ExtraBold
                                )
                            ) {
                                append(" ORGANICS")
                            }
                        },

                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },

                actions = {

                    OutlinedButton(

                        onClick = {
                            inviteFriends(context)
                        },

                        modifier = Modifier
                            .padding(end = 4.dp)
                            .size(width = 70.dp, height = 28.dp),

                        shape = RoundedCornerShape(8.dp),

                        border = BorderStroke(
                            1.dp,
                            Color.Black
                        ),

                        contentPadding = PaddingValues(0.dp)

                    ) {

                        Text(
                            "Invite",
                            color = Color.Blue,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Italic
                        )
                    }
                }
            )
        },

        floatingActionButton = {

            FloatingActionButton(

                onClick = {

                    context.startActivity(
                        Intent(
                            context,
                            CartActivity::class.java
                        )
                    )

                },

                containerColor = Color(0xFF4CAF50)

            ) {

                Icon(
                    Icons.Default.ShoppingCart,
                    contentDescription = null
                )
            }
        }

    ) { padding ->

        Column(

            modifier = Modifier
                .padding(padding)
                .fillMaxSize()

        ) {

            OutlinedTextField(

                value = searchQuery,

                onValueChange = {
                    searchQuery = it
                },

                label = {
                    Text("Search")
                },

                leadingIcon = {

                    Icon(
                        Icons.Default.Search,
                        null
                    )

                },

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)

            )

            CategoryDropdown(
                selectedCategory = selectedCategory
            ) {

                selectedCategory = it

                viewModel.loadProducts(it)

            }

            SwipeRefresh(

                state = rememberSwipeRefreshState(
                    refreshing.value
                ),

                onRefresh = {

                    refreshing.value = true

                    viewModel.loadProducts(
                        selectedCategory
                    )

                    refreshing.value = false
                }

            ) {

                when {

                    isLoading -> {

                        Box(
                            Modifier.fillMaxSize(),
                            Alignment.Center
                        ) {

                            CircularProgressIndicator()

                        }
                    }

                    filteredProducts.isEmpty() -> {

                        Box(
                            Modifier.fillMaxSize(),
                            Alignment.Center
                        ) {

                            Text(
                                "No products found"
                            )

                        }
                    }

                    else -> {

                        LazyVerticalGrid(

                            columns = GridCells.Fixed(2),

                            contentPadding = PaddingValues(8.dp),

                            verticalArrangement =
                                Arrangement.spacedBy(12.dp),

                            horizontalArrangement =
                                Arrangement.spacedBy(12.dp)

                        ) {

                            items(filteredProducts) { product ->

                                ProductItem(product) {

                                    cartList.add(it)

                                }

                            }

                        }

                    }

                }

            }

        }

    }

}

fun inviteFriends(
    context: Context
) {

    val intent = Intent(
        Intent.ACTION_SEND
    )

    intent.type = "text/plain"

    intent.putExtra(
        Intent.EXTRA_TEXT,
        "Check out Nagu Organics! Download the app now: https://i.diawi.com/eZ843H"
    )

    context.startActivity(
        Intent.createChooser(
            intent,
            "Invite via"
        )
    )

}