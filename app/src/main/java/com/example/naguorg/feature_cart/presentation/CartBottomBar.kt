package com.example.naguorg.feature_cart.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.naguorg.feature_products.domain.Product

@Composable
fun CartBottomBar(

    cartItems: List<Product>,

    totalPrice: Double,

    onBuyAll: () -> Unit,

    onShareAll: () -> Unit

) {

    val isCartEmpty = cartItems.isEmpty()

    Column(

        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
            .navigationBarsPadding()

    ) {

        Text(

            text = "Total Price : ₹${String.format("%.2f", totalPrice)}",

            style = MaterialTheme.typography.titleLarge,

            fontWeight = FontWeight.Bold,

            fontStyle = FontStyle.Italic

        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(

            modifier = Modifier.fillMaxWidth(),

            horizontalArrangement = Arrangement.SpaceEvenly

        ) {

            Button(

                onClick = onBuyAll,

                enabled = !isCartEmpty,

                modifier = Modifier.weight(1f),

                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF004AAD)
                )

            ) {

                Icon(

                    imageVector = Icons.Default.ShoppingCart,

                    contentDescription = "Buy",

                    tint = Color.White,

                    modifier = Modifier.size(20.dp)

                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(

                    text = "Buy All",

                    color = Color.White,

                    fontWeight = FontWeight.Bold

                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(

                onClick = onShareAll,

                enabled = !isCartEmpty,

                modifier = Modifier.weight(1f),

                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF004AAD)
                )

            ) {

                Row(

                    verticalAlignment = Alignment.CenterVertically

                ) {

                    Icon(

                        imageVector = Icons.Default.Share,

                        contentDescription = "Share",

                        tint = Color.White,

                        modifier = Modifier.size(20.dp)

                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(

                        text = "Share All",

                        color = Color.White,

                        fontWeight = FontWeight.Bold

                    )
                }
            }
        }
    }
}