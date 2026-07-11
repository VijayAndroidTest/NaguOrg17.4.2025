package com.example.naguorg.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.naguorg.products.Product
import com.example.naguorg.repository.CartRepository

class CartViewModel(
    private val repository: CartRepository
) : ViewModel() {

    var cartItems by mutableStateOf(
        repository.getCartItems()
    )
        private set

    var totalAmount by mutableStateOf(
        repository.getTotalAmount()
    )
        private set

    var totalItems by mutableStateOf(
        repository.getTotalItems()
    )
        private set

    private fun refreshCart() {
        cartItems = repository.getCartItems()
        totalAmount = repository.getTotalAmount()
        totalItems = repository.getTotalItems()
    }

    fun addToCart(product: Product) {

        repository.addToCart(product)
        refreshCart()
    }

    fun removeFromCart(product: Product) {

        repository.removeFromCart(product)
        refreshCart()
    }

    fun increaseQuantity(product: Product) {

        repository.increaseQuantity(product)
        refreshCart()
    }

    fun decreaseQuantity(product: Product) {

        repository.decreaseQuantity(product)
        refreshCart()
    }

    fun clearCart() {

        repository.clearCart()
        refreshCart()
    }

    fun isCartEmpty(): Boolean {

        return repository.isCartEmpty()
    }
}