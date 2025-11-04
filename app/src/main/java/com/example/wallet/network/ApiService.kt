package com.example.wallet.network

import com.example.wallet.model.CoinInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
interface ApiService {
    @GET("last/{pair}")
    suspend fun getConversionRate(
        @Path("pair") pair: String
    ): Response<Map<String, CoinInfo>>
}