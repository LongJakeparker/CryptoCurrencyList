package com.example.coinhako

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

/**
 * @author longtran
 * @since 29/05/2021
 */
class CoinHakoApplication : Application() {

    companion object {
        lateinit var instance: CoinHakoApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        if (SharedPreferencesManager.isContainKeyNightMode()) {
            if (SharedPreferencesManager.isNightMode()) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }
}