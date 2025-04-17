package com.example.naguorg

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.naguorg.ui.theme.getCartItems
import com.example.naguorg.ui.theme.saveCartItems
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.text.font.FontStyle

class CartActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val cartItems: ArrayList<Product>? = intent.getParcelableArrayListExtra("cartItems")

        enableEdgeToEdge()
        setContent {
            CartScreen(getCartItems(this)) { updatedCart ->
                saveCartItems(this, updatedCart) // Save updated cart back to SharedPreferences

                val resultIntent = Intent().apply {
                    putParcelableArrayListExtra("updatedCartItems", ArrayList(updatedCart))
                }
                setResult(Activity.RESULT_OK, resultIntent)
                finish() // Close CartActivity
            }

        Log.d("CartActivity", "Received Cart Items: ${cartItems?.size}")
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(cartItems: List<Product>, onCartUpdated: (List<Product>) -> Unit) {
    val activity = (LocalContext.current as? ComponentActivity)

    var items by remember { mutableStateOf(cartItems.distinctBy { it.name }.map { it.copy(quantity = 1) }) }
    var totalPrice by remember { mutableStateOf(0.0) }

    // Recalculate total price whenever cart updates
    LaunchedEffect(items) {
        totalPrice = items.sumOf { it.DP.toDouble() * it.quantity }
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
                        fontSize = 24.sp, // Increase size
                        fontWeight = FontWeight.ExtraBold, // Make it bold
                        letterSpacing = 1.sp, // Add spacing like DARJUV9
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center // Center alignment
                    )

                  //  Text("My Cart")

                        },
                navigationIcon = {
                    IconButton(onClick = {
                        onCartUpdated(items) // Send updated cart back
                        activity?.finish()
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = { CartBottomBar(totalPrice, items, LocalContext.current) }
    ) { paddingValues ->
        Column(

            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(items) { product ->
                    CartItem(
                        product = product,
                        onQuantityChange = { updatedProduct ->
                            items = items.map { if (it.name == updatedProduct.name) updatedProduct else it }
                        },
                        onDelete = {
                            items = items.filter { it.name != product.name }
                        }
                    )
                }
            }
        }
    }
}

    @Composable
    fun CartItem(
        product: Product,
        onQuantityChange: (Product) -> Unit,
        onDelete: () -> Unit
    ) {
        val context = LocalContext.current

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color.LightGray)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = rememberAsyncImagePainter(product.image),
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp) // Smaller image
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = product.name,
                        style = MaterialTheme.typography.bodyMedium,

                        color = Color.Blue
                    )
                    Text(
                        text = "Price:₹ ${product.DP}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .height(32.dp) // Adjust the height as needed
                            .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(10.dp))
                            .padding(horizontal = 4.dp, vertical = 2.dp) // Reduce internal padding
                    ) {
                        IconButton(
                            onClick = { onQuantityChange(product.copy(quantity = product.quantity - 1)) },
                            enabled = product.quantity > 1
                        ) {
                            Icon(imageVector = Icons.Filled.Remove, contentDescription = "Decrease", tint = Color.Red)
                        }

                        Text(text = "${product.quantity}", style = MaterialTheme.typography.bodyMedium)

                        IconButton(
                            onClick = { onQuantityChange(product.copy(quantity = product.quantity + 1)) }
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Increase", tint = Color.Green)
                        }
                    }

                }

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.End
                ) {
                    IconButton(
                        onClick = { shareCartToWhatsApp(context, listOf(product), "918838380787") }
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = "Share", tint = Color(0xFF128C7E))
                    }

                    IconButton(
                        onClick = {
                            val updatedCart = getCartItems(context).filterNot { it.name == product.name }
                            saveCartItems(context, updatedCart)
                            onDelete()
                        }
                    ) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                    }
                }
            }
        }
    }
    @Composable
    fun CartBottomBar(totalPrice: Double, cartItems: List<Product>, context: Context) {
        val isCartEmpty = cartItems.isEmpty()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
                .navigationBarsPadding()
        ) {
            Text(
                text = "Total Price: ₹${String.format("%.2f", totalPrice)}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic // Make text italic
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        if (!isCartEmpty) {
                            val intent = Intent(context, CheckoutActivity::class.java)

                            intent.putParcelableArrayListExtra("cart_items", ArrayList(cartItems))
                            startActivity(intent)
//                            val intent = Intent(context, CheckoutActivity::class.java)
//                            context.startActivity(intent)
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF004AAD)),
                    enabled = !isCartEmpty
                )
                {
                    Icon(


                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Buy",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Buy All", color = Color.White, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.width(8.dp)) // Space between the buttons

                Button(
                    onClick = {
                        if (!isCartEmpty) {
                            shareCartToWhatsApp(context, cartItems, "918838380787")
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF004AAD)),
                    enabled = !isCartEmpty
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Share, // Default Share Icon
                            contentDescription = "Share",
                            tint = Color.White, // White color to match text
                            modifier = Modifier.size(20.dp) // Adjust size as needed
                        )
                        Spacer(modifier = Modifier.width(8.dp)) // Space between icon and text
                        Text("Share All", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }

            if (isCartEmpty) {
                Text(
                    text = "Your cart is empty! Add items before proceeding.",
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }

//    fun shareCartToWhatsApp(context: Context, cartItems: List<Product>, phoneNumber: String) {
//        val message = buildString {
//            append("Order Details:\n")
//            cartItems.forEach { product ->
//                append("- ${product.name} (Qty: ${product.quantity}) - ₹${product.DP}\n")
//            }
//            append("Total: ₹${cartItems.sumOf { it.DP * it.quantity }}")
//        }
//
//        val uri = Uri.parse("https://api.whatsapp.com/send?phone=$phoneNumber&text=${Uri.encode(message)}")
//        val intent = Intent(Intent.ACTION_VIEW, uri)
//        try {
//            context.startActivity(intent)
//        } catch (e: Exception) {
//            Log.e("WhatsApp", "WhatsApp not installed or error occurred", e)
//        }
//    }

}
