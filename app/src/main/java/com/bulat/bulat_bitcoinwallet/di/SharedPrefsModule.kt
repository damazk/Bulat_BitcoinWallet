package com.bulat.bulat_bitcoinwallet.di

import android.content.Context
import com.bulat.bulat_bitcoinwallet.data.sharedprefs.SharedPrefsRepositoryImpl
import com.bulat.bulat_bitcoinwallet.domain.repository.SharedPrefsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SharedPrefsProvider {

    @Provides
    @Singleton
    fun provideSharedPrefs(@ApplicationContext context: Context) =
        context.getSharedPreferences("btc_wallet_shared_prefs", Context.MODE_PRIVATE)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class SharedPrefsBinder {

    @Binds
    @Singleton
    abstract fun bindSharedPrefsRepository(repository: SharedPrefsRepositoryImpl): SharedPrefsRepository
}