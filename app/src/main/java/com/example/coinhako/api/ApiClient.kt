package com.example.coinhako.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author longtran
 * @since 29/05/2021
 */

/**
 * A class use to setup retrofit
 */
object ApiClient {
    private const val SERVER_URL = "https://www.coinhako.com/api/"
    private val retrofit: Retrofit

    init {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .method(original.method(), original.body())
                .build()
            val response = chain.proceed(request)
            response
        }
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        httpClient.readTimeout(60, TimeUnit.SECONDS)
        httpClient.connectTimeout(60, TimeUnit.SECONDS)
        httpClient.addInterceptor(logging)

        val client = httpClient.build()
        val server = SERVER_URL
        retrofit = Retrofit.Builder()
            .baseUrl(server)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    /**
     * Function get the interface contain the list of APIs
     * @return RestApi class
     */
    fun getInterface(): RestApi {
        return retrofit.create(RestApi::class.java)
    }
}