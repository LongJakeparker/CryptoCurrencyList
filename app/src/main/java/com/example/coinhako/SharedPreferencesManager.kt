package com.example.coinhako

import android.content.Context
import com.google.gson.Gson
import java.util.*
import kotlin.collections.ArrayList
import com.google.gson.reflect.TypeToken

/**
 * Manager class of SharedPreferences
 * @author longtran
 * @since 30/05/2021
 */
object SharedPreferencesManager {
    private const val PREF_NAME = BuildConfig.APPLICATION_ID + "app.local.Preferences"
    private val sharedPreferences = CoinHakoApplication.instance.applicationContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private const val PREF_KEY_LUCKY_COINS = "pref_key_lucky_coins"
    private const val PREF_KEY_NIGHT_MODE = "pref_key_night_mode"

    /**
     * Puts favorite coin to local, remove if already exist
     * @param coinBase
     */
    fun putLuckyCoins(coinBase: String) {
        val listLuckyCoin = getLuckyCoins()
        if (listLuckyCoin.contains(coinBase)) {
            listLuckyCoin.remove(coinBase)
        } else {
            listLuckyCoin.add(coinBase)
        }
        val json = Gson().toJson(listLuckyCoin)
        val editor = sharedPreferences.edit()
        editor.putString(PREF_KEY_LUCKY_COINS, json)
        editor.apply()
    }

    /**
     * Removes favorite coin from local
     * @param coinBase
     */
    fun removeLuckyCoins(coinBase: String) {
        val listLuckyCoin = getLuckyCoins()
        if (listLuckyCoin.contains(coinBase)) {
            listLuckyCoin.remove(coinBase)
        }
        val json = Gson().toJson(listLuckyCoin)
        val editor = sharedPreferences.edit()
        editor.putString(PREF_KEY_LUCKY_COINS, json)
        editor.apply()
    }

    /**
     * Gets list favorite coin from local
     * @return ArrayList<String>
     */
    fun getLuckyCoins(): ArrayList<String> {
        if (sharedPreferences.contains(PREF_KEY_LUCKY_COINS)) {
            val myType = object : TypeToken<ArrayList<String>>() {}.type
            return Gson().fromJson(sharedPreferences.getString(PREF_KEY_LUCKY_COINS, null), myType)
        }
        return ArrayList()
    }

    /**
     * Checks if coin is already exist
     * @param coinBase
     */
    fun isContainCoin(coinBase: String): Boolean {
        val array = getLuckyCoins()
        return array.contains(coinBase)
    }

    fun setNightMode(isNightMode: Boolean) {
        sharedPreferences.edit().putBoolean(PREF_KEY_NIGHT_MODE, isNightMode).apply()
    }

    fun isNightMode(): Boolean {
        return sharedPreferences.getBoolean(PREF_KEY_NIGHT_MODE, false)
    }

    fun isContainKeyNightMode() = sharedPreferences.contains(PREF_KEY_NIGHT_MODE)
}