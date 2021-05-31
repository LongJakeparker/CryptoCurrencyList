package com.example.coinhako.repository

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.coinhako.api.ApiClient
import com.example.coinhako.base.Resource
import com.example.coinhako.model.CryptoCurrency
import com.example.coinhako.model.CryptoCurrencyMapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author longtran
 * @since 29/05/2021
 */
@Singleton
@SuppressLint("CheckResult")
class HomeRepository @Inject constructor() {

    /**
     * Get list of all currency
     * @return LiveData
     */
    fun getListCurrency(): LiveData<Resource<List<CryptoCurrency>>> {
        val data = MutableLiveData<Resource<List<CryptoCurrency>>>(Resource.loading(0))
        ApiClient.getInterface().getListCurrency().observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ result ->
                data.value = Resource.success(CryptoCurrencyMapper.transformCollection(result.data))
            }, { throwable ->
                data.value = Resource.error(throwable)
            })
        return data
    }
}