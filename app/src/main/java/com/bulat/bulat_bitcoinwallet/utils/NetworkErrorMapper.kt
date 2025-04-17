package com.bulat.bulat_bitcoinwallet.utils

import retrofit2.Response

fun <T> Response<T>.mapError(): NetworkError {
    require(!isSuccessful) { "Response is successful, no error to map." }

    return when (code()) {
        in 400..499 -> NetworkError.ClientError(code(), message())
        in 500..599 -> NetworkError.ServerError(code(), message())
        else -> NetworkError.UnknownError(message())
    }
}

sealed class NetworkError(override val message: String?) : Exception(message) {

    data class ClientError(
        val statusCode: Int,
        val errorMessage: String
    ) : NetworkError(errorMessage)

    data class ServerError(
        val statusCode: Int,
        val errorMessage: String
    ) : NetworkError(errorMessage)

    data class UnknownError(
        val errorMessage: String
    ) : NetworkError(errorMessage)
}