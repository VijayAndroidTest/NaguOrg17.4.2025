package com.example.naguorg.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.naguorg.products.Product
import com.example.naguorg.shareCartToWhatsApp

@Composable
fun CartItem(

    product: Product,

    onIncrease: () -> Unit,

    onDecrease: () -> Unit,

    onDelete: () -> Unit

) {

    val context = LocalContext.current

    Card(

        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),

        shape = RoundedCornerShape(8.dp),

        elevation = CardDefaults.cardElevation(4.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),

        border = BorderStroke(
            1.dp,
            Color.LightGray
        )
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
                    .size(120.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(
                        1.dp,
                        Color.Gray,
                        RoundedCornerShape(8.dp)
                    ),

                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Blue
                )

                Text(
                    text = "Price : ₹${product.DP}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(

                    verticalAlignment = Alignment.CenterVertically,

                    modifier = Modifier
                        .height(36.dp)
                        .border(
                            1.dp,
                            Color.Gray,
                            RoundedCornerShape(10.dp)
                        )

                ) {

                    IconButton(

                        enabled = product.quantity > 1,

                        onClick = onDecrease

                    ) {

                        Icon(
                            Icons.Default.Remove,
                            contentDescription = "Decrease",
                            tint = Color.Red
                        )
                    }

                    Text(
                        text = product.quantity.toString()
                    )

                    IconButton(

                        onClick = onIncrease

                    ) {

                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Increase",
                            tint = Color.Green
                        )
                    }
                }
            }

            Column(

                horizontalAlignment = Alignment.End,

                verticalArrangement = Arrangement.Center

            ) {

                IconButton(

                    onClick = {

                        shareCartToWhatsApp(

                            context,

                            listOf(product),

                            "918838380787"

                        )
                    }

                ) {

                    Icon(

                        imageVector = Icons.Default.Share,

                        contentDescription = "Share",

                        tint = Color(0xFF128C7E)

                    )
                }

                IconButton(

                    onClick = onDelete

                ) {

                    Icon(

                        imageVector = Icons.Default.Delete,

                        contentDescription = "Delete",

                        tint = Color.Red

                    )
                }
            }
        }
    }
}