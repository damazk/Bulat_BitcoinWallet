package com.bulat.bulat_bitcoinwallet.domain.usecase

import com.bulat.bulat_bitcoinwallet.domain.repository.SharedPrefsRepository
import org.bitcoinj.base.Address
import org.bitcoinj.base.ScriptType
import org.bitcoinj.crypto.DumpedPrivateKey
import org.bitcoinj.crypto.ECKey
import org.bitcoinj.params.SigNetParams
import org.bitcoinj.params.TestNet3Params
import javax.inject.Inject

class GetAddressUseCase @Inject constructor(
    private val sharedPrefs: SharedPrefsRepository
) {

    operator fun invoke(): Result<String> {

        val wif = sharedPrefs.getPrivateKeyWif(null)

        val key = if (wif != null) {
            DumpedPrivateKey.fromBase58(TestNet3Params.get(), wif).key
        } else {
            ECKey().also {
                sharedPrefs.putPrivateKeyWif(it.getPrivateKeyEncoded(TestNet3Params.get()).toString())
            }
        }

        val cachedAddress = sharedPrefs.getAddress(defValue = null)

        return if (cachedAddress != null) {
            Result.success(cachedAddress.toString())
        } else {
            val address = Address.fromKey(SigNetParams.get(), key, ScriptType.P2WPKH)
            sharedPrefs.putAddress(address.toString())
            Result.success(address.toString())
        }
    }
}