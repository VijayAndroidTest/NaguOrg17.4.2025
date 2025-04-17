package com.example.naguorg

import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.naguorg.ui.theme.NaguOrgTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.Image // Keep this import
import androidx.compose.ui.graphics.asImageBitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import java.io.OutputStream

class CheckoutActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cartItems: ArrayList<Product>? = intent.getParcelableArrayListExtra("cart_items")
        setContent {
            NaguOrgTheme {
                Log.d("CheckoutActivity", "cart_items: $cartItems")
                CheckoutScreen(cartItems ?: emptyList(), "sthennarasu1996s@okaxis")
            }
        }
    }

    fun captureScreenAndSave() {
        val view = window.decorView.rootView
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(bitmap)
        view.draw(canvas)

        // Save the captured screenshot to the gallery
        saveImageToGallery(this, bitmap, "Checkout_Screenshot")
    }
}

@Composable
fun CheckoutScreen(cartItems: List<Product>, upiId: String) {
    var qrCodeBitmap by remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current
    val totalAmount = cartItems.sumOf { it.DP * it.quantity }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
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

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(cartItems) { product ->
                val itemTotal = product.DP * product.quantity
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = product.name,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "₹${product.DP} x ${product.quantity} = ₹$itemTotal",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Text(
            text = "Total Amount: ₹$totalAmount",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(20.dp))

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
                    Log.d("Checkout", "Formatted Amount: $formattedAmount")

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
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006400)),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White)
                } else {
                    Text("Generate UPI QR Code")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        qrCodeBitmap?.let { bitmap ->
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "QR Code",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Button(
                onClick = {
                    saveImageToGallery(context, bitmap, "QR_Code")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Download QR")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    (context as? CheckoutActivity)?.captureScreenAndSave()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Capture & Save Screen")
            }
        }
    }
}

fun generateQRCode(content: String): Bitmap {
    val size = 500
    val bitMatrix: BitMatrix = MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, size, size)
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)
    for (x in 0 until size) {
        for (y in 0 until size) {
            bitmap.setPixel(x, y, if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
        }
    }
    return bitmap
}

fun saveImageToGallery(context: Context, bitmap: Bitmap, fileName: String?) {
    val safeFileName = fileName?.takeIf { it.isNotBlank() } ?: "NaguOrganics_Image"

    val values = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "${safeFileName}_${System.currentTimeMillis()}.png")
        put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/NaguOrganics")
    }

    val uri: Uri? = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    uri?.let {
        val outputStream: OutputStream? = context.contentResolver.openOutputStream(it)
        outputStream?.use { stream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            Toast.makeText(context, "Image saved to gallery", Toast.LENGTH_SHORT).show()
        }
    } ?: run {
        Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show()
    }
}