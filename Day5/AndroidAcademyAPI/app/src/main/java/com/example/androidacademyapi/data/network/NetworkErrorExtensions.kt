package com.example.androidacademyapi.data.network

import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.nio.channels.UnresolvedAddressException

fun Throwable.isNoInternetError(): Boolean {
    return generateSequence(this) { it.cause }.any { error ->
        error is UnknownHostException ||
            error is ConnectException ||
            error is SocketTimeoutException ||
            error is UnresolvedAddressException
    }
}
