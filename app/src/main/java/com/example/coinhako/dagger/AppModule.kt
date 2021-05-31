package com.example.coinhako.dagger

import android.content.Context
import com.example.coinhako.CoinHakoApplication
import dagger.Module
import dagger.Provides

/**
 * @author longtran
 * @since 29/05/2021
 */

@Module
object AppModule {
    @Provides
    @JvmStatic
    fun provideContext(): Context {
        return CoinHakoApplication.instance.applicationContext
    }
}