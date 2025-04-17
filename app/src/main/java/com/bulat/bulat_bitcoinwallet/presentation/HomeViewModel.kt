package com.bulat.bulat_bitcoinwallet.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bulat.bulat_bitcoinwallet.domain.usecase.CreateAndSendTransactionUseCase
import com.bulat.bulat_bitcoinwallet.domain.usecase.GetAddressUseCase
import com.bulat.bulat_bitcoinwallet.domain.usecase.GetBalanceUseCase
import com.bulat.bulat_bitcoinwallet.domain.usecase.GetPrivateKeyWifUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAddressUseCase: GetAddressUseCase,
    private val getPrivateKeyWifUseCase: GetPrivateKeyWifUseCase,
    private val createAndSendTransactionUseCase: CreateAndSendTransactionUseCase,
    private val getBalanceUseCase: GetBalanceUseCase
): ViewModel() {

    var address by mutableStateOf("")
        private set

    var recipientAddress by mutableStateOf("")
        private set

    var sum by mutableStateOf("")
        private set

    var balance by mutableDoubleStateOf(0.0)
        private set

    var transactionId by mutableStateOf<String?>(null)
        private set

    var showSuccessDialog by mutableStateOf(false)
        private set

    var showErrorDialog by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun loadBalance(address: String) = viewModelScope.launch {
        getBalanceUseCase(address).onSuccess {
            balance = it
        }.onFailure {
            showErrorDialog = true
            errorMessage = it.message
        }
    }

    fun loadAddress() = viewModelScope.launch {
        getAddressUseCase().onSuccess {
            address = it
            loadBalance(it)
        }.onFailure {
            showErrorDialog = true
            errorMessage = it.message
        }
    }

    fun onRecipientAddressChange(value: String) { recipientAddress = value }

    fun onSumChange(value: String) { sum = value }

    fun onSendBtnClick(address: String, recipientAddress: String, sum: String) = viewModelScope.launch(Dispatchers.IO) {

        val key = getPrivateKeyWifUseCase()
        val formattedSum = sum.toDoubleOrNull() ?: 0.0
        val satoshi = (formattedSum * 100_000_000).toLong()

        createAndSendTransactionUseCase(key, address, recipientAddress, satoshi).onSuccess {
            transactionId = it
            showSuccessDialog = true
        }.onFailure {
            showErrorDialog = true
            errorMessage = it.message
        }
    }

    fun onDismissRequest() {
        showSuccessDialog = false
        showErrorDialog = false
    }
}