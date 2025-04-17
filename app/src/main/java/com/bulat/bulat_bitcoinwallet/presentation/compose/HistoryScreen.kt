package com.bulat.bulat_bitcoinwallet.presentation.compose

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bulat.bulat_bitcoinwallet.R
import com.bulat.bulat_bitcoinwallet.presentation.model.TransactionUiItem
import com.bulat.bulat_bitcoinwallet.utils.compose.BtcWalletErrorDialog
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HistoryScreen(
    transactions: List<TransactionUiItem>,
    navigateUp: () -> Unit,
    showErrorDialog: Boolean,
    errorMessage: String,
    onDismissRequest: () -> Unit,
    onConfirmBtnClick: () -> Unit,
    modifier: Modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            BtcWalletTopBar(
                title = stringResource(R.string.transactions_history),
                onNavigationIconClick = navigateUp
            )
        }
    ) { paddings ->

        Box(modifier.padding(paddings)) {

            if (showErrorDialog) {
                BtcWalletErrorDialog(
                    titleText = stringResource(R.string.failed_to_load_transactions),
                    message = errorMessage,
                    onDismissRequest = onDismissRequest,
                    onConfirmBtnClick = onConfirmBtnClick
                )
            }

            LazyColumn(Modifier.fillMaxSize()) {
                items(transactions) { tx ->
                    TransactionItem(tx)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TransactionItem(tx: TransactionUiItem) {
    val direction = if (tx.isReceived) stringResource(R.string.received) else stringResource(R.string.sent)

    val color = if (tx.isReceived) Color(0xFF4CAF50) else Color(0xFFF44336)

    val formattedDate = Instant.ofEpochSecond(tx.timestamp)
        .atZone(ZoneId.systemDefault())
        .format(DateTimeFormatter.ofPattern("dd MMM HH:mm"))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = direction, color = color)
            Text(
                text = stringResource(R.string.transaction_id, tx.txid),
            )
            Text(text = stringResource(R.string.date, formattedDate))
            Text(text = stringResource(R.string.sum_tbtc, tx.amount.toDouble() / 100_000_000))
        }
    }
}