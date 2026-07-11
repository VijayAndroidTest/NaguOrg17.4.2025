package com.example.naguorg.view



import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.example.naguorg.viewmodel.CartViewModel
import android.content.Intent
import com.example.naguorg.shareCartToWhatsApp
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    viewModel: CartViewModel,
    onBack: () -> Unit
) {

    val context = LocalContext.current
    val activity = context as? ComponentActivity

    val cartItems = viewModel.cartItems

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
                        textAlign = TextAlign.Center
                    )
                },

                navigationIcon = {

                    IconButton(

                        onClick = {

                            onBack()
                            activity?.finish()
                        }

                    ) {

                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },

        bottomBar = {

            CartBottomBar(

                cartItems = cartItems,

                totalPrice = viewModel.totalAmount,

                onBuyAll = {

                    val intent = Intent(
                        context,
                        CheckoutActivity::class.java
                    )

                    intent.putParcelableArrayListExtra(
                        "cart_items",
                        ArrayList(cartItems)
                    )

                    context.startActivity(intent)
                },

                onShareAll = {

                    shareCartToWhatsApp(

                        context,

                        cartItems,

                        "918838380787"
                    )
                }
            )
        }

    ) { padding ->

        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(padding)

        ) {

            LazyColumn {

                items(cartItems) { product ->

                    CartItem(

                        product = product,

                        onIncrease = {

                            viewModel.increaseQuantity(product)

                        },

                        onDecrease = {

                            viewModel.decreaseQuantity(product)

                        },

                        onDelete = {

                            viewModel.removeFromCart(product)

                        }

                    )
                }
            }
        }
    }
}