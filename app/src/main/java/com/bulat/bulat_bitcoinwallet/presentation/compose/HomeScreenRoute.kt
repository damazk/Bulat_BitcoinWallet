package com.bulat.bulat_bitcoinwallet.presentation.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.bulat.bulat_bitcoinwallet.R
import com.bulat.bulat_bitcoinwallet.presentation.HomeViewModel

const val HOME_SCREEN_ROUTE = "home_screen_route"

fun NavGraphBuilder.homeScreen(navigateToHistoryScreen: () -> Unit) =
    composable(HOME_SCREEN_ROUTE) {
        HomeScreenRoute(navigateToHistoryScreen)
    }

@Composable
fun HomeScreenRoute(
    navigateToHistoryScreen: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val address = viewModel.address
    val balance = viewModel.balance
    val recipientAddress = viewModel.recipientAddress
    val sum = viewModel.sum
    val showSuccessDialog = viewModel.showSuccessDialog
    val txId = viewModel.transactionId
    val showErrorDialog = viewModel.showErrorDialog

    val errorMessage = viewModel.errorMessage?.let {
        if (it.isNotEmpty()) stringResource(R.string.unknown_error)
        else it
    } ?: stringResource(R.string.unknown_error)

    LaunchedEffect(Unit) {
        viewModel.loadAddress()
    }

    HomeScreen(
        balance = balance.toString(),
        address = address,
        recipientAddress = recipientAddress,
        onRecipientAddressChange = viewModel::onRecipientAddressChange,
        sum = sum,
        onSumChange = viewModel::onSumChange,
        isSumError = balance < (sum.toDoubleOrNull() ?: 0.0),
        onSendBtnClick = viewModel::onSendBtnClick,
        showSuccessDialog = showSuccessDialog,
        txId = txId,
        onDismissRequest = viewModel::onDismissRequest,
        onConfirmBtnClick = viewModel::onDismissRequest,
        showErrorDialog = showErrorDialog,
        errorMessage = errorMessage,
        onHistoryBtnClick = { navigateToHistoryScreen() }
    )
}