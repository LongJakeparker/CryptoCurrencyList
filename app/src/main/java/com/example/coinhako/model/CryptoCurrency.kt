package com.example.coinhako.model

import com.example.coinhako.base.IComparable
import java.io.Serializable

/**
 * Model used to represents currency objects in project
 * @author longtran
 * @since 29/05/2021
 */
data class CryptoCurrency(
    val base: String? = "",
    val counter: String? = "",
    val buyPrice: String? = "",
    val sellPrice: String? = "",
    val icon: String? = "",
    val name: String? = "",
    var isLucky: Boolean = false,
) : IComparable<CryptoCurrency>, Serializable {
    override fun isTheSame(item: CryptoCurrency): Boolean {
        return this.base == item.base
    }

    override fun areContentsTheSame(item: CryptoCurrency): Boolean {
        return this.name == item.name
                && this.icon == item.icon
                && this.counter == item.counter
                && this.buyPrice == item.buyPrice
                && this.sellPrice == item.sellPrice
                && this.isLucky == item.isLucky
    }
}