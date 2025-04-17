package com.bulat.bulat_bitcoinwallet.presentation.compose

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.bulat.bulat_bitcoinwallet.R
import com.bulat.bulat_bitcoinwallet.presentation.HistoryViewModel

const val HISTORY_SCREEN_ROUTE = "history_screen_route"

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.historyScreen(navigateUp: () -> Unit) =
    composable(HISTORY_SCREEN_ROUTE) {
        HistoryScreenRoute(navigateUp)
    }

fun NavController.navigateToHistoryScreen() =
    navigate(
        route = HISTORY_SCREEN_ROUTE,
        builder = { launchSingleTop = true }
    )

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HistoryScreenRoute(
    navigateUp: () -> Unit,
    viewModel: HistoryViewModel = hiltViewModel()
) {

    val transactions = viewModel.transactions
    val showErrorDialog = viewModel.showErrorDialog

    val errorMessage = viewModel.errorMessage?.let {
        if (it.isNotEmpty()) stringResource(R.string.unknown_error)
        else it
    } ?: stringResource(R.string.unknown_error)

    LaunchedEffect(Unit) {
        viewModel.loadAddress()
    }

    HistoryScreen(
        transactions = transactions,
        navigateUp = navigateUp,
        onDismissRequest = viewModel::onDismissRequest,
        onConfirmBtnClick = viewModel::onDismissRequest,
        errorMessage = errorMessage,
        showErrorDialog = showErrorDialog
    )
}