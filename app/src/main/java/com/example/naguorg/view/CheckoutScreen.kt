package com.example.naguorg.view

import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.naguorg.feature_cart.data.generateQRCode
import com.example.naguorg.feature_products.domain.Product
import com.example.naguorg.feature_cart.data.saveImageToGallery
import com.example.naguorg.viewmodel.CheckoutViewModel

@Composable
fun CheckoutScreen(
    cartItems: List<Product>,
    upiId: String,
    viewModel: CheckoutViewModel,
    onPayClick: (Double) -> Unit
) {
    val context = LocalContext.current
    var qrCodeBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val totalAmount = cartItems.sumOf { it.DP * it.quantity }.toDouble()

    // 1. Observe the ViewModel state for Cashfree Session
    LaunchedEffect(viewModel.paymentSession) {
        viewModel.paymentSession?.let { order ->
            (context as? CheckoutActivity)?.startCashfreePayment(order)
        }
    }

    // Use a Scaffold structure to lock action buttons cleanly at the bottom
    Scaffold(
        bottomBar = {
            Surface(
                tonalElevation = 8.dp,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .navigationBarsPadding() // Ensures safe area spacing for system gestures
                ) {
                    if (qrCodeBitmap == null) {
                        Button(
                            onClick = {
                                Log.d("CheckoutScreen", "Button Clicked!")
                                isLoading = true

                                val validCartItems = cartItems.filter { it.DP.toString().toDoubleOrNull() != null }
                                val totalAmount = validCartItems.sumOf { (it.DP.toString().toDouble()) * it.quantity }

                                if (totalAmount <= 0) {
                                    Toast.makeText(context, "Invalid amount", Toast.LENGTH_SHORT).show()
                                    isLoading = false
                                    return@Button
                                }

                                val formattedAmount = String.format("%.2f", totalAmount)
                                val upiPaymentLink = "upi://pay?pa=$upiId&pn=NaguOrganics&mc=0000&tid=123456789&tr=123456789&tn=Payment&am=$formattedAmount&cu=INR&url=https://naguorganics.com"

                                try {
                                    val bitmap = generateQRCode(upiPaymentLink)
                                    qrCodeBitmap = bitmap
                                    isLoading = false
                                } catch (e: Exception) {
                                    Toast.makeText(context, "QR Code generation failed!", Toast.LENGTH_LONG).show()
                                    isLoading = false
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006400)),
                            enabled = !isLoading
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                            } else {
                                Text("Generate UPI QR Code", fontWeight = FontWeight.Bold)
                            }
                        }
                    } else {
                        // Core Payment Execution Trigger
                        Button(
                            onClick = { onPayClick(totalAmount.toDouble()) },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006400))
                        ) {
                            Text("Pay ₹$totalAmount Now", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        // The main content area scrolls independently of the bottom payment actions
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
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
            }

            // Cart Items Breakout Header
            item {
                Text(
                    text = "Order Summary",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray
                )
            }

            // Inline item lists mapped into the LazyColumn architecture safely
            items(cartItems) { product ->
                val itemTotal = product.DP * product.quantity
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = product.name,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f),
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "₹${product.DP} x ${product.quantity} = ₹$itemTotal",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            item {
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Total Amount Due:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "₹$totalAmount",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF004AAD)
                    )
                }
            }

            // QR Utilities Render Block inside scroll view
            qrCodeBitmap?.let { bitmap ->
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Card(
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = "QR Code",
                                modifier = Modifier
                                    .size(220.dp)
                                    .padding(12.dp)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { saveImageToGallery(context, bitmap, "QR_Code") },
                                modifier = Modifier.weight(1.5f)
                            ) {
                                Text("Download QR", fontSize = 12.sp)
                            }

                            OutlinedButton(
                                onClick = { (context as? CheckoutActivity)?.captureScreenAndSave() },
                                modifier = Modifier.weight(2f)
                            ) {
                                Text("Capture Receipt", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}