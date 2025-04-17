package com.example.naguorg

data class CartItem(
    val id: String,
    val name: String,
    val price: String,
    val image: String,
    var quantity: Int = 1
)