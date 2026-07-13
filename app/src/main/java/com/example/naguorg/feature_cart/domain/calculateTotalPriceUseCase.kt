package com.example.naguorg.feature_cart.domain

import com.example.naguorg.feature_products.domain.Product

// written for Unit test
fun calculateTotalPriceUseCase(cartItems: List<Product>): Double {
    // Since DP is an Int, just multiply it by quantity
    return cartItems.sumOf { (it.DP * it.quantity).toDouble() }
}