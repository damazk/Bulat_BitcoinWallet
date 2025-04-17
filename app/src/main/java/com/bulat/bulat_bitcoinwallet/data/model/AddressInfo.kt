package com.bulat.bulat_bitcoinwallet.data.model

data class AddressInfo(
    val chain_stats: ChainStats
)

data class ChainStats(
    val funded_txo_sum: Long,
    val spent_txo_sum: Long
)