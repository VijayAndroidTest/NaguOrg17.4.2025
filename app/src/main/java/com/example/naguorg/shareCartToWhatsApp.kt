package com.example.naguorg

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.example.naguorg.products.Product
import com.google.firebase.auth.FirebaseAuth


fun shareCartToWhatsApp(context: Context, cartItems: List<Product>, shopPhoneNumber: String) {
    val (userName, userPhoneNumber) = getStoredUserDetails(context)
    val message = buildCartMessage(cartItems, userName, userPhoneNumber)

    val uri = Uri.parse("https://api.whatsapp.com/send?phone=$shopPhoneNumber&text=${Uri.encode(message)}")
    val intent = Intent(Intent.ACTION_VIEW, uri)
// Logging for debugging
    Log.d("WhatsAppShare", "User Name: $userName, User Phone: $userPhoneNumber")
    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        Log.e("WhatsAppShare", "WhatsApp is not installed", e)
    }
}

fun buildCartMessage(cartItems: List<Product>, userName: String, userPhoneNumber: String): String {
    val messageBuilder = StringBuilder()

    // Customer details
    messageBuilder.append("\n🏠 *Customer Details:*\n")
    messageBuilder.append("📌 *Name:* $userName\n")
    messageBuilder.append("📞 *Phone:* $userPhoneNumber\n")

    // Header
    messageBuilder.append("🛒 *Order Summary* 📦\n━━━━━━━━━━━━━━━━━━━━━━\n")

    // Cart Items
    for (product in cartItems) {
        messageBuilder.append("🔹 *${product.name}*\n")
        messageBuilder.append("   ₹${product.DP} × ${product.quantity}\n")
        messageBuilder.append("━━━━━━━━━━━━━━━━━━━━━━\n")
    }

    // Total Price
    val totalPrice = cartItems.sumOf { it.DP.toDouble() * it.quantity }
    messageBuilder.append("\n✅ *Total Price:* ₹$totalPrice\n")



    // Delivery Address
    messageBuilder.append("\n📍 *Delivery Address:*\n")
    messageBuilder.append("🏬 5/124, Sundaram Plaza,\n")
    messageBuilder.append("📍 Near Golden Trendz,\n")
    messageBuilder.append("📌 B.S Sundaram Street,\n")
    messageBuilder.append("📍 Avinashi - 641654\n")

    // Contact details
    messageBuilder.append("\n📞 *Shop Contact:* +91 8838380787\n")
    messageBuilder.append("━━━━━━━━━━━━━━━━━━━━━━\n")
    messageBuilder.append("🔔 *Thank you for shopping with us!* 🎉\n")

    return messageBuilder.toString()
}

fun getStoredUserDetails(context: Context): Pair<String, String> {
    val sharedPref = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    val userName = sharedPref.getString("userName", "Unknown") ?: "Unknown"
    val userMobile = sharedPref.getString("userMobile", "Unknown") ?: "Unknown"
    Log.d("WhatsAppShare", "Retrieved User: $userName, Phone: $userMobile")
    return Pair(userName, userMobile)
}