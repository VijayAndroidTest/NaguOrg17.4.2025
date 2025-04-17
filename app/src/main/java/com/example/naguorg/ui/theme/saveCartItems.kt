package com.example.naguorg.ui.theme

import android.content.Context
import android.content.SharedPreferences
import com.example.naguorg.Product
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun saveCartItems(context: Context, cartItems: List<Product>) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("CartPrefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    val json = Gson().toJson(cartItems)
    editor.putString("cart_items", json)
    editor.apply()
}

fun getCartItems(context: Context): List<Product> {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("CartPrefs", Context.MODE_PRIVATE)
    val json = sharedPreferences.getString("cart_items", null)
    return if (json != null) {
        val type = object : TypeToken<List<Product>>() {}.type
        Gson().fromJson(json, type)
    } else {
        emptyList()
    }
}