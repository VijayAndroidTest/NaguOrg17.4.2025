package com.example.naguorg.repository

import androidx.compose.runtime.mutableStateListOf
import com.example.naguorg.products.Product

class CartRepository {

    private val cartItems = mutableStateListOf<Product>()

    fun getCartItems(): List<Product> {
        return cartItems
    }

    fun addToCart(product: Product) {

        val existingProduct = cartItems.find {
            it.name == product.name
        }

        if (existingProduct != null) {

            existingProduct.quantity += 1

        } else {

            product.quantity = 1
            cartItems.add(product)
        }
    }

    fun removeFromCart(product: Product) {

        cartItems.remove(product)
    }

    fun increaseQuantity(product: Product) {

        cartItems.find {
            it.name == product.name
        }?.let {

            it.quantity++
        }
    }

    fun decreaseQuantity(product: Product) {

        cartItems.find {
            it.name == product.name
        }?.let {

            if (it.quantity > 1) {

                it.quantity--

            } else {

                cartItems.remove(it)
            }
        }
    }

    fun clearCart() {

        cartItems.clear()
    }

    fun getTotalAmount(): Double {

        return cartItems.sumOf {

            it.DP * it.quantity
        }.toDouble()
    }

    fun getTotalItems(): Int {

        return cartItems.sumOf {

            it.quantity
        }
    }

    fun isCartEmpty(): Boolean {

        return cartItems.isEmpty()
    }
}