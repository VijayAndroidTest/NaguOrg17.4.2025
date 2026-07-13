package com.example.naguorg.feature_products.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.naguorg.feature_products.domain.Product

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertProducts(products: List<Product>) // Bulk Insert

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertProduct(product: Product) // Single Insert

    @Query("SELECT * FROM products WHERE category = :category")
    suspend fun getProductsByCategory(category: String): List<Product>

    @Query("SELECT * FROM products")
    suspend fun getAllProducts(): List<Product>

    @Update
    suspend fun updateProduct(product: Product)

    @Query("DELETE FROM products")
    suspend fun deleteAllProducts()
}