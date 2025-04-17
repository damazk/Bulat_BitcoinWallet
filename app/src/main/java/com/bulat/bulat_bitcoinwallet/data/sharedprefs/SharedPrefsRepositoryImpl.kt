package com.bulat.bulat_bitcoinwallet.data.sharedprefs

import android.content.SharedPreferences
import com.bulat.bulat_bitcoinwallet.domain.repository.SharedPrefsRepository
import javax.inject.Inject

class SharedPrefsRepositoryImpl @Inject constructor(
    private val sharedPrefs: SharedPreferences
): SharedPrefsRepository {

    override fun getString(key: String, defValue: String?) = sharedPrefs.getString(key, defValue)

    override fun putString(key: String, value: String) =
        sharedPrefs.edit().putString(key, value).apply()

    override fun getAddress(defValue: String?) = getString(ADDRESS_KEY, defValue)

    override fun putAddress(address: String) = putString(ADDRESS_KEY, address)

    override fun getPrivateKeyWif(defValue: String?) = getString(PRIVATE_KEY_WIF_KEY, defValue)

    override fun putPrivateKeyWif(key: String) = putString(PRIVATE_KEY_WIF_KEY, key)

    override fun clear() = sharedPrefs.edit().clear().apply()

    private companion object {
        const val ADDRESS_KEY = "address_key"
        const val PRIVATE_KEY_WIF_KEY = "private_key_wif"
    }
}