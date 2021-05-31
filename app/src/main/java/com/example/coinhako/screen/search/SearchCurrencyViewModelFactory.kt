package com.example.coinhako.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.coinhako.repository.HomeRepository
import javax.inject.Inject

/**
 * @author longtran
 * @since 31/05/2021
 */
class SearchCurrencyViewModelFactory @Inject constructor() : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchCurrencyViewModel() as T
    }
}