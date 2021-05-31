package com.example.coinhako.dagger

import com.example.coinhako.screen.home.HomeViewModelFactory
import com.example.coinhako.screen.search.SearchCurrencyViewModelFactory
import dagger.Component
import javax.inject.Singleton

/**
 * @author longtran
 * @since 29/05/2021
 */

@Component(
    modules = [
        AppModule::class
    ]
)
@Singleton
interface AppComponent {
    fun getHomeViewModelFactory(): HomeViewModelFactory
    fun getSearchCurrencyViewModelFactory(): SearchCurrencyViewModelFactory
}