package com.example.naguorg.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.compose.runtime.remember

import com.example.naguorg.products.Product
import com.example.naguorg.cart.getCartItems
import com.example.naguorg.repository.CartRepository
import com.example.naguorg.cart.saveCartItems
import com.example.naguorg.viewmodel.CartViewModel

class CartActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            val repository = remember {
                CartRepository()
            }

            val viewModel = remember {
                CartViewModel(repository)
            }

            // Load saved cart only once
            remember {

                val savedCart = getCartItems(this)

                savedCart.forEach {
                    viewModel.addToCart(it)
                }

                true
            }

            CartScreen(

                viewModel = viewModel,

                onBack = {

                    saveCartItems(
                        this,
                        viewModel.cartItems
                    )

                    val resultIntent = Intent().apply {

                        putParcelableArrayListExtra(
                            "updatedCartItems",
                            ArrayList<Product>(viewModel.cartItems)
                        )
                    }

                    setResult(RESULT_OK, resultIntent)
                    finish()
                }
            )
        }
    }
}



