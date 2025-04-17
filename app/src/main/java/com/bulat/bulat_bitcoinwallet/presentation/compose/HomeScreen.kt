package com.bulat.bulat_bitcoinwallet.presentation.compose

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bulat.bulat_bitcoinwallet.R
import com.bulat.bulat_bitcoinwallet.ui.theme.Bulat_BitcoinWalletTheme
import com.bulat.bulat_bitcoinwallet.utils.compose.BtcWalletErrorDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier.fillMaxSize(),
    balance: String,
    address: String,
    recipientAddress: String,
    sum: String,
    onSumChange: (String) -> Unit,
    isSumError: Boolean,
    onRecipientAddressChange: (String) -> Unit,
    onSendBtnClick: (String, String, String) -> Unit,
    showSuccessDialog: Boolean,
    txId: String?,
    onDismissRequest: () -> Unit,
    onConfirmBtnClick: () -> Unit,
    showErrorDialog: Boolean,
    errorMessage: String,
    onHistoryBtnClick: (String) -> Unit,
) {

    val sumSupportingText = if (isSumError) stringResource(R.string.not_enough_funds) else ""

    val transactionId = txId ?: stringResource(R.string.failed_to_load_transaction_id)

    Scaffold(
        modifier = modifier,
        topBar = {
            BtcWalletTopBar(
                title = stringResource(R.string.bitcoin_wallet),
                onHistoryBtnClick = { onHistoryBtnClick(address) },
            )
        }
    ) { paddings ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddings)
                .padding(16.dp)
        ) {

            if (showSuccessDialog) {
                ShowSuccessTransactionDialog(
                    transactionId = transactionId,
                    titleText = stringResource(R.string.your_funds_have_been_sent),
                    text = stringResource(R.string.your_transaction_id_is),
                    onDismissRequest = onDismissRequest,
                    onConfirmBtnClick = onConfirmBtnClick,
                    confirmBtnText = stringResource(R.string.send_more)
                )
            }

            if (showErrorDialog) {
                BtcWalletErrorDialog(
                    titleText = stringResource(R.string.transaction_failed),
                    message = errorMessage,
                    onDismissRequest = onDismissRequest,
                    onConfirmBtnClick = onConfirmBtnClick
                )
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top
            ) {

                // Balance
                Text(
                    text = stringResource(R.string.balance, balance),
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(Modifier.height(15.dp))

                // Address
                Text(
                    text = stringResource(R.string.your_btc_address, address),
                    style = MaterialTheme.typography.titleSmall
                )

                Spacer(Modifier.height(20.dp))

                // Recipient Address Field
                BtcOutlinedTextField(
                    value = recipientAddress,
                    onValueChange = onRecipientAddressChange,
                    labelText = stringResource(R.string.address_to_send)
                )

                // Sum Field
                BtcOutlinedTextField(
                    value = sum,
                    onValueChange = onSumChange,
                    labelText = stringResource(R.string.amount_to_send),
                    isError = isSumError,
                    supportingText = sumSupportingText,
                    keyboardType = KeyboardType.Number
                )

                Spacer(Modifier.weight(1f))

                BtcFilledButton(
                    text = stringResource(R.string.send),
                    enabled = recipientAddress.isNotEmpty() && sum.isNotEmpty() && !isSumError,
                    onClick = { onSendBtnClick(address, recipientAddress, sum) }
                )
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BtcWalletTopBar(
    title: String,
    titleColor: Color = Color.White,
    containerColor: Color = Color(0xFFE3851B),
    modifier: Modifier = Modifier.fillMaxWidth(),
    onHistoryBtnClick: (() -> Unit)? = null,
    onNavigationIconClick: (() -> Unit)? = null
) {

    val title = @Composable {
        Text(
            text = title,
            fontWeight = FontWeight.SemiBold
        )
    }

    CenterAlignedTopAppBar(
        modifier = modifier,
        title = title,
        navigationIcon = {
            if (onNavigationIconClick != null) {

                IconButton(
                    onClick = onNavigationIconClick
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = Icons.AutoMirrored.Rounded.ArrowBack.name,
                        tint = Color.White
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            titleContentColor = titleColor,
            containerColor = containerColor
        ),
        actions =  {
            if (onHistoryBtnClick != null) {
                IconButton(
                    onClick = onHistoryBtnClick
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.Send,
                        contentDescription = Icons.AutoMirrored.Rounded.Send.name,
                        tint = Color.White
                    )
                }
            }
        }
    )
}

@Composable
fun BtcOutlinedTextField(
    modifier: Modifier = Modifier.fillMaxWidth(),
    value: String,
    onValueChange: (String) -> Unit,
    labelText: String,
    isError: Boolean = false,
    supportingText: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    color: Color = Color(0xFFE3851B)
) = OutlinedTextField(
    modifier = modifier,
    value = value,
    onValueChange = onValueChange,
    label = {
        Text(labelText)
    },
    isError = isError,
    supportingText = {
        Text(supportingText)
    },
    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
    colors = OutlinedTextFieldDefaults.colors(
        unfocusedBorderColor = color,
        focusedBorderColor = color
    )
)

@Composable
fun BtcFilledButton(
    text: String,
    modifier: Modifier = Modifier.fillMaxWidth(),
    enabled: Boolean = true,
    containerColor: Color = Color(0xFFE3851B),
    contentColor: Color = Color.White,
    onClick: () -> Unit
) = Button(
    modifier = modifier,
    onClick = onClick,
    enabled = enabled,
    colors = ButtonDefaults.buttonColors(
        containerColor = containerColor,
        contentColor = contentColor
    ),
    content = { Text(text) }
)

@Composable
private fun ShowSuccessTransactionDialog(
    transactionId: String,
    titleText: String,
    text: String,
    onDismissRequest: () -> Unit,
    onConfirmBtnClick: () -> Unit,
    confirmBtnText: String,
) {

    val context = LocalContext.current

    AlertDialog(
        title = {
            Text(
                text = titleText,
                style = MaterialTheme.typography.titleLarge,
            )
        },
        text = {
            Column {
                Text(text)
                Text(
                    text = transactionId,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .clickable {
                            val url = "https://mempool.space/signet/tx/$transactionId"
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        }
                )
            }
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            BtcFilledButton(
                text = confirmBtnText,
                onClick = onConfirmBtnClick
            )
        },
    )
}

@Composable
@Preview(showBackground = true)
private fun PreviewHomeScreen() = Bulat_BitcoinWalletTheme {
    HomeScreen(
        balance = "0.004000324",
        address = "jaldfgjhveklrhvkleshjdflkg32413214feferf",
        recipientAddress = "",
        onRecipientAddressChange = {},
        sum = "",
        onSumChange = {},
        onSendBtnClick = { _, _, _ -> },
        showSuccessDialog = false,
        txId = "hfqwieufg8qu2y3f4iougq34uito8webr34w4t4q35tyghbe5",
        isSumError = false,
        onDismissRequest = {},
        onConfirmBtnClick = {},
        showErrorDialog = false,
        errorMessage = "",
        onHistoryBtnClick = {}
    )
}