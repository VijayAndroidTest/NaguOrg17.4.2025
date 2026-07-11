package com.example.naguorg.products

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

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