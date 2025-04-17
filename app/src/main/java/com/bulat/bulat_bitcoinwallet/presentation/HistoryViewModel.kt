package com.bulat.bulat_bitcoinwallet.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bulat.bulat_bitcoinwallet.domain.usecase.GetAddressUseCase
import com.bulat.bulat_bitcoinwallet.domain.usecase.GetTransactionsUseCase
import com.bulat.bulat_bitcoinwallet.presentation.model.TransactionUiItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel@Inject constructor(
    private val getAddressUseCase: GetAddressUseCase,
    private val getTransactionsUseCase: GetTransactionsUseCase
): ViewModel() {

    var transactions by mutableStateOf<List<TransactionUiItem>>(emptyList())
        private set

    var showErrorDialog by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var address by mutableStateOf("")
        private set

    private fun getTransactions(address: String) = viewModelScope.launch(Dispatchers.IO) {
        getTransactionsUseCase(address).onSuccess {
            transactions = it
        }.onFailure {
            errorMessage = it.message
            showErrorDialog = true
        }
    }

    fun loadAddress() = viewModelScope.launch(Dispatchers.IO) {
        getAddressUseCase().onSuccess {
            address = it
            getTransactions(it)
        }.onFailure {
            showErrorDialog = true
            errorMessage = it.message
        }
    }

    fun onDismissRequest() { showErrorDialog = false }
}