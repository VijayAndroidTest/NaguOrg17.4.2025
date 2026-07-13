package com.example.naguorg.feature_products.domain

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "products", indices = [
    Index("category")
]) // Room entity

data class Product(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // Auto-generate ID
    val name: String = "",
    val disc: Int =  0,
    val image: String = "",
    val category : String = "",
    val MRP: Int = 0,  // Changed from String to Int
    val DP: Int = 0,
    var quantity: Int = 1,// Allow quantity changes
    val description: String = "" // Add this field
) : Parcelable