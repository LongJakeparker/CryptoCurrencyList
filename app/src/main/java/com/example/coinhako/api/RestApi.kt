package com.example.coinhako.api

import com.example.coinhako.base.BaseEntity
import com.example.coinhako.model.CryptoCurrencyEntity
import io.reactivex.Observable
import retrofit2.http.GET

/**
 * @author longtran
 * @since 29/05/2021
 */

/**
 * Interface contain list of APIs
 */
interface RestApi {
    @GET("v3/price/all_prices_for_mobile?counter_currency=USD")
    fun getListCurrency(): Observable<BaseEntity<List<CryptoCurrencyEntity?>>>
}