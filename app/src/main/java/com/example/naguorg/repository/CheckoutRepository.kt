package com.example.naguorg.repository

import com.example.naguorg.domain_api.OrderRequest
import com.example.naguorg.domain_api.OrderResponse
import com.example.naguorg.domain_api.RetrofitClient

class CheckoutRepository {

    suspend fun createOrder(amount: Double): OrderResponse {
        return RetrofitClient.api.createOrder(
            OrderRequest(amount)
        )
    }
}