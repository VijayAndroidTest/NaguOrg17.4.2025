package com.example.naguorg.feature_products.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.naguorg.feature_products.domain.GetProductsByCategoryUseCase
import com.example.naguorg.feature_products.domain.Product
import com.example.naguorg.feature_products.domain.ProductRepository
import kotlinx.coroutines.launch

// File: com/example/naguorg/feature_products/presentation/ProductViewModel.kt
class ProductViewModel(
    private val getProductsByCategoryUseCase: GetProductsByCategoryUseCase // Injected UseCase
) : ViewModel() {

    var selectedCategory by mutableStateOf("All category")
        private set

    var products by mutableStateOf<List<Product>>(emptyList())
        private set
    var loading by mutableStateOf(false)
        private set

    fun loadProducts(category: String = "All category") {
        viewModelScope.launch {
            loading = true
            try {
                // Use the UseCase instead of calling the repository directly
                products = getProductsByCategoryUseCase(category)
            } catch (e: Exception) {
                // Handle error
            } finally {
                loading = false
            }
        }
    }
}