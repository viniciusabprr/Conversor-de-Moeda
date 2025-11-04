package com.example.wallet.model

import com.google.gson.annotations.SerializedName

class ApiResponse {
}

data class CoinInfo(
    @SerializedName("code")
    val code: String,
    @SerializedName("codein")
    val codein: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("bid")
    val bid: String
)