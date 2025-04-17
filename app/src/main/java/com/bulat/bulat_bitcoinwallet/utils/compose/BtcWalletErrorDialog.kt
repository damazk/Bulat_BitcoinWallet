package com.bulat.bulat_bitcoinwallet.utils.compose

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.bulat.bulat_bitcoinwallet.R
import com.bulat.bulat_bitcoinwallet.presentation.compose.BtcFilledButton

@Composable
fun BtcWalletErrorDialog(
    titleText: String,
    message: String,
    onDismissRequest: () -> Unit,
    onConfirmBtnClick: () -> Unit
) {

    AlertDialog(
        title = {
            Text(
                text = titleText,
                style = MaterialTheme.typography.titleLarge,
            )
        },
        text = {
            Text(stringResource(R.string.error, message))
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            BtcFilledButton(
                text = stringResource(R.string.ok),
                onClick = onConfirmBtnClick
            )
        },
    )
}