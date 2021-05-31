package com.example.coinhako.screen.search

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.coinhako.base.SingleEventLiveData
import com.example.coinhako.model.CryptoCurrency
import com.example.coinhako.screen.home.CurrencyAdapter

/**
 * @author longtran
 * @since 31/05/2021
 */
class SearchCurrencyViewModel : ViewModel() {
    val currencyAdapter = CurrencyAdapter()
    //Arraylist which save the origin currency list
    private var originalListCurrency = ArrayList<CryptoCurrency>()

    //The data flows
    private val _listCurrency = MutableLiveData<ArrayList<CryptoCurrency>>(ArrayList())
    val listCurrency: LiveData<ArrayList<CryptoCurrency>> = _listCurrency

    private val _keyword = MutableLiveData<String>()
    val keyword: LiveData<String> = _keyword

    //The single LiveData represent for click events
    private val _eventLuckyCoin = SingleEventLiveData<Int>()
    val eventLuckyCoin: LiveData<Int> = _eventLuckyCoin

    private val _eventBack = SingleEventLiveData<Int>()
    val eventBack: LiveData<Int> = _eventBack

    //Listeners for adapters
    val clickLuckyListener = object : CurrencyAdapter.CurrencyListener {
        override fun onClickLucky(item: CryptoCurrency) {
            _eventLuckyCoin.setValue(1)
        }
    }

    val searchKeywordListener = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            _keyword.value = s.toString().trim()
            //Filters the coins has name contain the keyword
            //show all currency if keyword is empty
            val newList = if (_keyword.value!!.isNotEmpty()) {
                ArrayList(originalListCurrency.filter { item ->
                    if (item.name.isNullOrEmpty()) false else item.name.contains(
                        _keyword.value!!,
                        ignoreCase = true
                    )
                })
            } else {
                originalListCurrency
            }
            _listCurrency.value = newList
        }

        override fun afterTextChanged(s: Editable?) {

        }

    }

    val clickBackListener = View.OnClickListener {
        _eventBack.setValue(1)
    }

    /**
     * Sets data to list currency
     * @param listCurrency
     */
    fun setData(listCurrency: ArrayList<CryptoCurrency>) {
        _listCurrency.value = listCurrency
        originalListCurrency.apply {
            clear()
            addAll(listCurrency)
        }
    }
}