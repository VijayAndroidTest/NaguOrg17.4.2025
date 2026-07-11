package com.example.naguorg.repository

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import com.example.naguorg.products.Product
import com.example.naguorg.products.ProductDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ProductRepository(
    private val context: Context,
    private val firestore: FirebaseFirestore,
    private val database: ProductDatabase
) {

    private val productDao = database.productDao()

    suspend fun fetchProducts(
        category: String
    ): List<Product> {

        return try {

            if (!isInternetAvailable()) {

                Log.d("Repository", "Offline -> Room Database")

                return productDao.getAllProducts()
            }

            val query = if (category == "All category") {

                firestore.collection("products")

            } else {

                firestore.collection("products")
                    .whereEqualTo("category", category)
            }

            val result = query.get().await()

            Log.d(
                "Repository",
                "Documents = ${result.documents.size}"
            )

            val products = result.documents.mapNotNull { document ->

                document.toObject(Product::class.java)?.copy(
                    description =
                        document.getString("description") ?: ""
                )
            }

            saveProducts(products)

            products

        } catch (e: Exception) {

            Log.e("Repository", "Firestore Error", e)

            productDao.getAllProducts()
        }
    }

    private suspend fun saveProducts(
        products: List<Product>
    ) {

        withContext(Dispatchers.IO) {

            productDao.insertProducts(products)
        }
    }

    suspend fun getOfflineProducts(): List<Product> {

        return withContext(Dispatchers.IO) {

            productDao.getAllProducts()
        }
    }

    private fun isInternetAvailable(): Boolean {

        val connectivityManager =
            context.getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager

        val networkInfo =
            connectivityManager.activeNetworkInfo

        return networkInfo != null &&
                networkInfo.isConnected
    }
}