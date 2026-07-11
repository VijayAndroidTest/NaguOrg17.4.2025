package com.example.naguorg.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.naguorg.products.Product
import com.example.naguorg.repository.ProductRepository
import kotlinx.coroutines.launch

class ProductViewModel(
    private val repository: ProductRepository
) : ViewModel() {

    var products by mutableStateOf<List<Product>>(emptyList())
        private set

    var loading by mutableStateOf(false)
        private set

    var error by mutableStateOf("")
        private set

    var selectedCategory by mutableStateOf("All category")
        private set

    fun loadProducts(
        category: String = selectedCategory
    ) {

        selectedCategory = category

        viewModelScope.launch {

            loading = true

            try {

                products = repository.fetchProducts(category)

            } catch (e: Exception) {

                error = e.message ?: "Unable to load products"

            } finally {

                loading = false
            }
        }
    }

    fun refresh() {

        loadProducts(selectedCategory)
    }

    fun changeCategory(
        category: String
    ) {

        selectedCategory = category

        loadProducts(category)
    }

    fun clearError() {

        error = ""
    }
}