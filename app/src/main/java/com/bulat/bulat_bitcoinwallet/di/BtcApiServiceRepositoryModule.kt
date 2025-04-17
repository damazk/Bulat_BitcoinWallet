package com.bulat.bulat_bitcoinwallet.di

import com.bulat.bulat_bitcoinwallet.data.remote.repository.BtcApiServiceRepositoryImpl
import com.bulat.bulat_bitcoinwallet.domain.repository.BtcApiServiceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class BtcApiServiceRepositoryBinder {

    @Binds
    abstract fun bindBtcApiServiceRepository(
        repository: BtcApiServiceRepositoryImpl
    ): BtcApiServiceRepository
}