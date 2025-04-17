package com.bulat.bulat_bitcoinwallet.domain.usecase

import com.bulat.bulat_bitcoinwallet.data.model.UtxoDto
import com.bulat.bulat_bitcoinwallet.domain.repository.BtcApiServiceRepository
import org.bitcoinj.base.Address
import org.bitcoinj.base.AddressParser
import org.bitcoinj.base.BitcoinNetwork
import org.bitcoinj.base.Coin
import org.bitcoinj.base.ScriptType
import org.bitcoinj.base.Sha256Hash
import org.bitcoinj.base.exceptions.AddressFormatException
import org.bitcoinj.core.Transaction
import org.bitcoinj.core.TransactionOutPoint
import org.bitcoinj.crypto.ECKey
import org.bitcoinj.params.SigNetParams
import org.bitcoinj.script.ScriptBuilder
import javax.inject.Inject

class CreateAndSendTransactionUseCase @Inject constructor(
    private val btcApiServiceRepository: BtcApiServiceRepository
) {

    val params = SigNetParams.get()

    @OptIn(ExperimentalStdlibApi::class)
    suspend operator fun invoke(
        privateKey: ECKey,
        fromAddressStr: String,
        recipientAddressStr: String,
        amountToSend: Long
    ): Result<String?> {

        val utxos = btcApiServiceRepository.getUtxos(fromAddressStr).getOrNull() ?: return Result.failure(
            Exception("Failed to load UTXOs")
        )

        val network = BitcoinNetwork.SIGNET

        val toAddress = try {
            val addressParser = AddressParser.getDefault(network)
            addressParser.parseAddress(recipientAddressStr)
        } catch (e: AddressFormatException) {
            val exception = Exception("invalid recipient address ${e.message}")
            return Result.failure(exception)
        }

        val fee = calculateFee(utxos, privateKey, amountToSend, toAddress)

        val fromAddress = Address.fromKey(params, privateKey, ScriptType.P2WPKH)
        val scriptPubKey = ScriptBuilder.createP2WPKHOutputScript(privateKey)

        val coinsToSend = Coin.valueOf(amountToSend)
        val totalInput = utxos.sumOf { it.value }
        val tempTx = Transaction()

        tempTx.addOutput(coinsToSend, toAddress)

        val finalTx = Transaction()
        finalTx.addOutput(coinsToSend, toAddress)

        val change = Coin.valueOf(totalInput - amountToSend - fee)

        val dustLimit = 546L
        if (change.value >= dustLimit) {
            finalTx.addOutput(change, fromAddress)
        }

        utxos.forEachIndexed { index, utxo ->
            val outPoint = TransactionOutPoint(utxo.vout.toLong(), Sha256Hash.wrap(utxo.txid))

            finalTx.addSignedInput(
                outPoint,
                scriptPubKey,
                Coin.valueOf(utxo.value),
                privateKey,
                Transaction.SigHash.ALL,
                false
            )
        }

        val txHex = finalTx.serialize().toHexString()

        return btcApiServiceRepository.broadcastTransaction(txHex)
    }

    private fun calculateFee(utxos: List<UtxoDto>, privKey: ECKey, amountToSend: Long, toAddress: Address): Long {
        val tempTx = Transaction()
        val scriptPubKey = ScriptBuilder.createP2WPKHOutputScript(privKey)

        tempTx.addOutput(Coin.valueOf(amountToSend), toAddress)

        utxos.forEach { utxo ->
            val outPoint = TransactionOutPoint(utxo.vout.toLong(), Sha256Hash.wrap(utxo.txid))
            tempTx.addSignedInput(
                outPoint,
                scriptPubKey,
                Coin.valueOf(utxo.value),
                privKey
            )
        }

        val feeRate = 2.0
        val estimatedSize = tempTx.serialize().size

        return (feeRate * estimatedSize).toLong()
    }
}