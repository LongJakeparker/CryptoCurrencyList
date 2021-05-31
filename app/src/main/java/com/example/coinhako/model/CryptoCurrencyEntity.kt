package com.example.coinhako.model

import com.google.gson.annotations.SerializedName
/**
 * An entity represents for data from api
 * @author longtran
 * @since 29/05/2021
 */
data class CryptoCurrencyEntity(
    val base: String?,
    val counter: String?,

    @SerializedName("buy_price")
    val buyPrice: String?,

    @SerializedName("sell_price")
    val sellPrice: String?,
    val icon: String?,
    val name: String?,
)