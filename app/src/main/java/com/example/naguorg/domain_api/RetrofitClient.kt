package com.example.naguorg.domain_api

import com.example.naguorg.domain_api.CashfreeApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://cashfreebackend-fkf3.onrender.com/"

    val api: CashfreeApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
            .create(CashfreeApi::class.java)
    }
}