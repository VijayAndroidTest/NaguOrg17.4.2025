package com.example.naguorg

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProductViewModel : ViewModel() {
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    val isLoading = mutableStateOf(false)

    fun fetchProducts(category: String) {
        viewModelScope.launch {
            isLoading.value = true
            _products.value = fetchProductsFromFirestore(category)
            isLoading.value = false
        }
    }

    private suspend fun fetchProductsFromFirestore(category: String): List<Product> {
        return try {
            val db = FirebaseFirestore.getInstance()
            val query = if (category == "All category") db.collection("products")
            else db.collection("products").whereEqualTo("category", category)

            val result = query.get().await()
            result.documents.mapNotNull { it.toObject(Product::class.java) }
        } catch (e: Exception) {
            Log.e("Firestore", "Error fetching products", e)
            emptyList()
        }
    }
}