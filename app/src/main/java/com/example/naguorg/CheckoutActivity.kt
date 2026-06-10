package com.example.naguorg

import android.R.id.message
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.naguorg.ui.theme.NaguOrgTheme
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.cashfree.pg.api.CFPaymentGatewayService
import com.cashfree.pg.core.api.CFSession

import com.cashfree.pg.core.api.callback.CFCheckoutResponseCallback
import com.cashfree.pg.core.api.exception.CFException
import com.cashfree.pg.core.api.utils.CFErrorResponse

import com.cashfree.pg.ui.api.CFDropCheckoutPayment
import org.json.JSONObject
import java.io.OutputStream

class CheckoutActivity : ComponentActivity(), CFCheckoutResponseCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Register callback within the class
        CFPaymentGatewayService.getInstance().setCheckoutCallback(this)

        val cartItems: ArrayList<Product>? = intent.getParcelableArrayListExtra("cart_items")
        setContent {
            NaguOrgTheme {
                CheckoutScreen(
                    cartItems = cartItems ?: emptyList(),
                    upiId = "sthennarasu1996s@okaxis",
                    onPayClick = { total -> startPayment(total) }
                )
            }
        }
    }

    private fun startPayment(amount: Double) {

        RetrofitClient.api.createOrder(
            OrderRequest(amount)
        ).enqueue(object : retrofit2.Callback<OrderResponse> {

            override fun onResponse(
                call: retrofit2.Call<OrderResponse>,
                response: retrofit2.Response<OrderResponse>
            ) {

                if (response.isSuccessful && response.body() != null) {

                    val order = response.body()!!

                    try {

                        val session = CFSession.CFSessionBuilder()
                            .setEnvironment(CFSession.Environment.SANDBOX)
                            .setOrderId(order.orderId)
                            .setPaymentSessionID(order.paymentSessionId)
                            .build()

                        val payment = CFDropCheckoutPayment
                            .CFDropCheckoutPaymentBuilder()
                            .setSession(session)
                            .build()

                        CFPaymentGatewayService
                            .getInstance()
                            .doPayment(this@CheckoutActivity, payment)

                    } catch (e: CFException) {
                        e.printStackTrace()

                        Toast.makeText(
                            this@CheckoutActivity,
                            e.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }

                } else {

                    Toast.makeText(
                        this@CheckoutActivity,
                        "Failed to create order",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(
                call: retrofit2.Call<OrderResponse>,
                t: Throwable
            ) {

                Toast.makeText(
                    this@CheckoutActivity,
                    t.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
//

    override fun onPaymentVerify(orderID: String) {
        Toast.makeText(this, "Payment Successful", Toast.LENGTH_LONG).show()
    }

    override fun onPaymentFailure(
        p0: CFErrorResponse?,
        p1: String?
    ) {
        Toast.makeText(this, "Payment Failed: ", Toast.LENGTH_LONG).show()
    }

    fun captureScreenAndSave() {
        // Ensure saveImageToGallery is defined elsewhere in your project
        val view = window.decorView.rootView
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(bitmap)
        view.draw(canvas)
        saveImageToGallery(this, bitmap, "Checkout_Screenshot")
    }
}
@Composable
fun CheckoutScreen(cartItems: List<Product>, upiId: String, onPayClick: (Double) -> Unit) {
    var qrCodeBitmap by remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current
    val totalAmount = cartItems.sumOf { it.DP * it.quantity }
    var isLoading by remember { mutableStateOf(false) }

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

