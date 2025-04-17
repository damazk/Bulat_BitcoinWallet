package com.bulat.bulat_bitcoinwallet.domain.usecase

import com.bulat.bulat_bitcoinwallet.data.model.TransactionDto
import com.bulat.bulat_bitcoinwallet.domain.repository.BtcApiServiceRepository
import com.bulat.bulat_bitcoinwallet.presentation.model.TransactionUiItem
import javax.inject.Inject

class GetTransactionsUseCase @Inject constructor(
    private val btcApiServiceRepository: BtcApiServiceRepository,
) {

    suspend operator fun invoke(address: String): Result<List<TransactionUiItem>> {
        val transactions = btcApiServiceRepository.getTransactions(address).getOrNull() ?: return Result.failure(
            Exception("Failed to load transaction for address: $address")
        )

        val txItems = transactions.map { it.toTransactionItem(address) }

        return Result.success(txItems)
    }

    fun TransactionDto.toTransactionItem(userAddress: String): TransactionUiItem {
        val isSent = vin.any { it.prevout.scriptPubKeyAddress == userAddress }
        val isReceived = vout.any { it.scriptPubKeyAddress == userAddress } && !isSent

        val amount = when {
            isSent -> {
                -vin.filter { it.prevout.scriptPubKeyAddress == userAddress }
                    .sumOf { it.prevout.value }
            }
            isReceived -> {
                vout.filter { it.scriptPubKeyAddress == userAddress }
                    .sumOf { it.value }
            }
            else -> 0L
        }

        return TransactionUiItem(
            txid = txid,
            timestamp = status.blockTime ?: (System.currentTimeMillis() / 1000),
            amount = amount,
            isReceived = isReceived
        )
    }
}