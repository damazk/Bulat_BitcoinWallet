package com.bulat.bulat_bitcoinwallet.presentation.model

data class TransactionUiItem(
    val txid: String,
    val timestamp: Long,
    val amount: Long,
    val isReceived: Boolean
)