package com.example.naguorg.feature_products.domain

import com.example.naguorg.feature_products.domain.Product
import com.example.naguorg.feature_products.domain.ProductRepository

class GetProductsByCategoryUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(category: String): List<Product> {
        // The repository handles the Firestore query logic based on category
        return repository.fetchProducts(category)
    }
}

