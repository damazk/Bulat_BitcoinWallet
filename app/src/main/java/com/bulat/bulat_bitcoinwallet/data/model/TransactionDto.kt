package com.bulat.bulat_bitcoinwallet.data.model

import com.google.gson.annotations.SerializedName

data class TransactionDto(
    val txid: String,
    val status: TxStatus,
    val vin: List<TxInput>,
    val vout: List<TxOutput>
)

data class TxStatus(
    val confirmed: Boolean,
    @SerializedName("block_time")
    val blockTime: Long?
)

data class TxInput(
    val prevout: TxOutput
)

data class TxOutput(
    @SerializedName("scriptpubkey_address")
    val scriptPubKeyAddress: String?,
    val value: Long
)
