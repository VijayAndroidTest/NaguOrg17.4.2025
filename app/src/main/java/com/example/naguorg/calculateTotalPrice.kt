package com.example.naguorg

fun calculateTotalPrice(cartItems: List<Product>): Double {
    // Since DP is an Int, just multiply it by quantity
    return cartItems.sumOf { (it.DP * it.quantity).toDouble() }
}