package com.example.naguorg.domain_api

import retrofit2.http.Body
import retrofit2.http.POST

interface CashfreeApi {

    @POST("create-order")
    suspend fun createOrder(
        @Body request: OrderRequest
    ): OrderResponse
}