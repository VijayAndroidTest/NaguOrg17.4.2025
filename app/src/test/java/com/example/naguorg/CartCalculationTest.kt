package com.example.naguorg

import com.example.naguorg.products.Product
import org.junit.Assert.assertEquals
import org.junit.Test

class CartCalculationTest {

    @Test
    fun testTotalPriceCalculation() {
        val cart = listOf(
            // Note: DP is now an Int (no quotes)
            Product(name = "Honey", DP = 100, quantity = 2), // 200
            Product(name = "Oil", DP = 50, quantity = 1)     // 50
        )
        // Total should be 250.0
        assertEquals(250.0, calculateTotalPrice(cart), 0.001)
    }

    @Test
    fun testInvalidPriceHandlesGracefully() {
        // Test what happens if DP is not a valid number
        val cart = listOf(Product(name = "BadData", DP = 0, quantity = 1))
        assertEquals(0.0, calculateTotalPrice(cart), 0.001)
    }
}