package com.bulat.bulat_bitcoinwallet

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.bulat.bulat_bitcoinwallet.presentation.compose.HOME_SCREEN_ROUTE
import com.bulat.bulat_bitcoinwallet.presentation.compose.historyScreen
import com.bulat.bulat_bitcoinwallet.presentation.compose.homeScreen
import com.bulat.bulat_bitcoinwallet.presentation.compose.navigateToHistoryScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BtcWalletNavHost() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = HOME_SCREEN_ROUTE
    ) {

        homeScreen(navController::navigateToHistoryScreen)

        historyScreen(navController::navigateUp)
    }
}