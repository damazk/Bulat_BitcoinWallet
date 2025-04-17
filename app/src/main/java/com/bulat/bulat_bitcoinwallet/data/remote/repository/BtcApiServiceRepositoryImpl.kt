package com.bulat.bulat_bitcoinwallet.data.remote.repository

import com.bulat.bulat_bitcoinwallet.data.remote.api.BtcApiService
import com.bulat.bulat_bitcoinwallet.domain.repository.BtcApiServiceRepository
import com.bulat.bulat_bitcoinwallet.utils.mapError
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class BtcApiServiceRepositoryImpl @Inject constructor(
    private val btcApiService: BtcApiService
): BtcApiServiceRepository {

    override suspend fun getAddressInfo(address: String) =
        try {
            val response = btcApiService.getAddressInfo(address)
            if (response.isSuccessful)
                Result.success(response.body())
            else
                throw response.mapError()
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun getUtxos(address: String) =
        try {
            val response = btcApiService.getUtxos(address)
            if (response.isSuccessful)
                Result.success(response.body())
            else
                throw response.mapError()
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun broadcastTransaction(transactionHex: String) =
        try {
            val request = transactionHex.toRequestBody(TEXT_PLAIN.toMediaType())
            val response = btcApiService.broadcastTransaction(request)
            if (response.isSuccessful)
                Result.success(response.body())
            else
                throw response.mapError()
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun getTransactions(address: String) =
        try {
            val response = btcApiService.getTransactions(address)
            if (response.isSuccessful)
                Result.success(response.body())
            else
                throw response.mapError()
        } catch (e: Exception) {
            Result.failure(e)
        }

    private companion object {
        const val TEXT_PLAIN = "text/plain"
    }
}