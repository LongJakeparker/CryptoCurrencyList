package com.example.coinhako.screen.home

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.coinhako.CoinHakoApplication
import com.example.coinhako.R
import com.example.coinhako.SharedPreferencesManager
import com.example.coinhako.base.Resource
import com.example.coinhako.base.SingleEventLiveData
import com.example.coinhako.model.CryptoCurrency
import com.example.coinhako.repository.HomeRepository

/**
 * @author longtran
 * @since 29/05/2021
 */
class HomeViewModel internal constructor(private val homeRepository: HomeRepository) : ViewModel() {
    val currencyAdapter = CurrencyAdapter()
    val luckyCoinsAdapter = LuckyCoinsAdapter()

    //The data flows
    private val _listCurrency = MutableLiveData<ArrayList<CryptoCurrency>>(ArrayList())
    val listCurrency: LiveData<ArrayList<CryptoCurrency>> = _listCurrency

    private val _listLuckyCoins = MutableLiveData<ArrayList<CryptoCurrency>>(ArrayList())
    val listLuckyCoins: LiveData<ArrayList<CryptoCurrency>> = _listLuckyCoins

    private val _textTitleLuckyCoins = MutableLiveData<String>()
    val textTitleLuckyCoins: LiveData<String> = _textTitleLuckyCoins

    //The single LiveData represent for click events
    private val _eventSearch = SingleEventLiveData<Int>()
    val eventSearch: LiveData<Int> = _eventSearch

    private val _eventSettingNightMode = SingleEventLiveData<Int>()
    val eventSettingNightMode: LiveData<Int> = _eventSettingNightMode

    private val _eventReTry = SingleEventLiveData<Int>()
    val eventReTry: LiveData<Int> = _eventReTry

    //On-click events is set to view through data-binding
    //Sets LiveData value to notify to Fragment/Activity
    val clickSearchListener = View.OnClickListener {
        _eventSearch.setValue(1)
    }

    val clickSettingNightModeListener = View.OnClickListener {
        _eventSettingNightMode.setValue(1)
    }

    val clickReTryListener = View.OnClickListener {
        _eventReTry.setValue(1)
    }

    //Listeners for adapters
    val clickLuckyListener = object : CurrencyAdapter.CurrencyListener {
        override fun onClickLucky(item: CryptoCurrency) {
            val newList = ArrayList(_listLuckyCoins.value)
            if (_listLuckyCoins.value!!.contains(item)) {
                newList.remove(item)
            } else {
                newList.add(0, item)
            }
            _listLuckyCoins.value = newList
        }
    }

    val removeLuckyCoinListener = object : LuckyCoinsAdapter.RemoveLuckyCoinListener {
        override fun onRemove(item: CryptoCurrency, newList: ArrayList<CryptoCurrency>) {
            val index = currencyAdapter.currentList.indexOf(item)
            currencyAdapter.currentList[index].isLucky = false
            currencyAdapter.notifyItemChanged(index)

            _listLuckyCoins.value = newList
        }
    }

    /**
     * Sets data to list currency
     * @param data
     */
    fun setDataListCurrency(data: ArrayList<CryptoCurrency>) {
        _listCurrency.value = data
        loadLuckyCoins()
    }

    /**
     * Load lucky coin list based on currency list and local lucky coin list
     */
    private fun loadLuckyCoins() {
        //Get saved list in local storage
        val listBaseName = SharedPreferencesManager.getLuckyCoins()

        if (listBaseName.isNullOrEmpty()) return

        //Marks all item in currency list which is Lucky Coin
        //then filter to get the list favorite
        val result = _listCurrency.value?.map { item ->
            if (listBaseName.contains(item.base)) item.isLucky = true
            return@map item
        }?.filter { item -> item.isLucky }

        _listLuckyCoins.value = ArrayList(result)
    }

    fun getDataListCurrency(): LiveData<Resource<List<CryptoCurrency>>> {
        return homeRepository.getListCurrency()
    }

    /**
     * Sets value of text Title of Favorite list section
     * @param isShow
     */
    fun showLuckyCoinsText(isShow: Boolean) {
        _textTitleLuckyCoins.value = if (isShow) {
            CoinHakoApplication.instance.applicationContext.getString(R.string.text_title_lucky_coins)
        } else {
            CoinHakoApplication.instance.applicationContext.getString(R.string.text_title_collect_lucky_coins)
        }
    }
}