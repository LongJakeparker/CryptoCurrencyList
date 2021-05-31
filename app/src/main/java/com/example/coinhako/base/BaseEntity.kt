package com.example.coinhako.base

/**
 * A base entity to handles the general structure of api's response
 * @author longtran
 * @since 29/05/2021
 */
data class BaseEntity<T>(
    var data: T? = null
)