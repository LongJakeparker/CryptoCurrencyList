package com.example.coinhako.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.coinhako.repository.HomeRepository
import javax.inject.Inject

/**
 * @author longtran
 * @since 29/05/2021
 */
class HomeViewModelFactory @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(homeRepository) as T
    }
}