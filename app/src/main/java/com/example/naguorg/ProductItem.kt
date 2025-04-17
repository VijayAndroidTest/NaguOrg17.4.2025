package com.example.naguorg

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.CreationExtras
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.naguorg.ui.theme.getCartItems
import com.example.naguorg.ui.theme.saveCartItems
import com.google.gson.Gson
import kotlin.time.times

// Declare cartList globally or inside a ViewModel
val cartList = mutableListOf<Product>()

@Composable
fun ProductItem(product: Product,  onAddToCart: (Product) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxWidth()
            .padding(2.dp) // Add padding to the Box
    )

    {


        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDialog = true }
                .padding(1.dp),
            shape = RoundedCornerShape(2.dp),
            elevation = CardDefaults.cardElevation(0.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        )

        {
            Column(
                modifier = Modifier.padding(1.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            )

            {
                ProductImage(image = product.image, onClick = { showDialog = true })

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = product.name.ifBlank { "\n" }, // Ensures at least one empty line if text is blank
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color(0xFF0665B9), // Vibrant Blue (Material Design Primary Blue)
                        fontSize = 11.sp, // Slightly larger for better readability
                        fontWeight = FontWeight.SemiBold, // Softer than Bold, looks elegant
                        fontStyle = FontStyle.Italic, // 🔥 Makes the font italic
                        lineHeight = 11.sp, // Adjust spacing for clarity
                        letterSpacing = 0.2.sp // Slight spacing for a refined look
                    ),
                    minLines = 2, // Ensures at least 2 lines
                    maxLines = 2, // Limits to 2 lines
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(2.dp))


                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = buildAnnotatedString {
                            append("MRP: ")
                            withStyle(style = SpanStyle(color = Color.Black, textDecoration = TextDecoration.LineThrough)) {
                                append("₹${product.MRP}")
                            }
                        },
                        fontSize = 10.sp
                    )


                    Text(
                        text = buildAnnotatedString {
                            append("DP: ")
                            withStyle(style = SpanStyle(color = Color(0xFF121212))) {
                                append("₹${product.DP}")
                            }
                        },
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                // Add to Cart Button
                val context = LocalContext.current
                var cartItems by rememberSaveable { mutableStateOf<List<Product>>(emptyList()) }

                val cartLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartActivityForResult()
                ) { result ->
                    if (result.resultCode == Activity.RESULT_OK) {
                        val updatedCart: ArrayList<Product>? =
                            result.data?.getParcelableArrayListExtra("updatedCartItems")
                        updatedCart?.let {
                            cartItems = it // Assign a new list to trigger recomposition
                        }
                    }
                }

                Button(onClick = {
                    val newProduct = Product(
                        name = product.name,
                        DP = product.DP,
                        MRP =product.MRP,
                        disc = product.disc,
                        image = product.image,
                        description = product.description,

                        quantity = 1 // Ensure quantity is initialized

                    )

                    // Retrieve existing cart items and add a new product
                    val updatedCart = getCartItems(context) + newProduct
                    saveCartItems(context, updatedCart) // Save to SharedPreferences

                    val intent = Intent(context, CartActivity::class.java).apply {
                        putParcelableArrayListExtra("cartItems", ArrayList(updatedCart))
                    }
                    // Show a toast message
                    Toast.makeText(context, "Item added to cart", Toast.LENGTH_LONG).show()
                    // cartLauncher.launch(intent)
                },
                    modifier = Modifier
                        .height(35.dp) // Slightly compact height
                        .padding(horizontal = 1.dp),
                    shape = RoundedCornerShape(12.dp), // Smooth rounded corners
                    colors = ButtonDefaults.buttonColors(
                        //  colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006400))
                        containerColor = Color(0xFF006400), // Attractive Orange color
                        contentColor = Color.White // White text for contrast
                    ),
                    elevation = ButtonDefaults.buttonElevation(1.dp) // Light elevation for depth
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Cart",
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp)) // Space between icon and text
                    Text(text = "Add to cart", color = Color.White, fontWeight = FontWeight.Bold, maxLines = 1,
                        softWrap = false)
                }
            }
        }

        // 🔥 Discount Label at the Top-Right Corner
        // 🔥 Discount Label at the Top-Right Corner
        if (product.disc > 0) {  // Check if discount is greater than 0
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .background(Color(0xFFC62828), shape = RoundedCornerShape(bottomStart = 4.dp)) // Light red
                    .padding(horizontal = 2.dp, vertical = 1.dp) // Smaller padding
            ) {
                Text(
                    text = "${product.disc}% OFF",
                    color = Color.White, // Darker red text for contrast
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 10.sp, // Smaller font size
                    modifier = Modifier
                        .padding(horizontal = 3.dp, vertical = 1.dp) // Minimized size
                )
            }


        }

    }

    if (showDialog) {
        ZoomableImageDialog(imageUrl = product.image, description = product.description ?: "", onDismiss = { showDialog = false })
    }


}




@Composable
fun ProductImage(image: String?, onClick: () -> Unit) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(image)
            .crossfade(true)
            .error(R.drawable.nagu_organics_logo)
            .placeholder(R.drawable.nagu_organics_logo)
            .build(),
        contentDescription = "Product Image",
        modifier = Modifier
            .size(170.dp) // Increased size for visibility
            .clip(RoundedCornerShape(5.dp))
            .background(Color.LightGray.copy(alpha = 0.3f))
            .clickable { onClick() } // Clickable effect
            .shadow(8.dp, shape = RoundedCornerShape(3.dp)), // Shadow for highlighting
        contentScale = ContentScale.Crop
    )


}

@Composable
fun ZoomableImageDialog(imageUrl: String, description: String, onDismiss: () -> Unit) {
    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.8f))
                .clickable { onDismiss() }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .pointerInput(Unit) {
                            detectTransformGestures { _, pan, zoom, _ ->
                                scale = (scale * zoom).coerceIn(1f, 3f)
                                offsetX = (offsetX + pan.x).coerceIn(-200f, 200f)
                                offsetY = (offsetY + pan.y).coerceIn(-200f, 200f)
                            }
                        }
                        .graphicsLayer(
                            scaleX = scale,
                            scaleY = scale,
                            translationX = offsetX,
                            translationY = offsetY
                        )
                ) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp)),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f) // Allows scrolling if needed
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp)
                )
                {
                    Text(
                        text = description.ifBlank { "No description available" },
                        color = Color.White, // Use proper color instead of DEFAULT_ARGS_KEY
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        fontStyle = FontStyle.Italic, // Makes the text italic
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}

