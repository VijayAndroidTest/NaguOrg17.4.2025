package com.example.naguorg

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface CashfreeApi {

    @POST("create-order")
    fun createOrder(
        @Body request: OrderRequest
    ): Call<OrderResponse>
}